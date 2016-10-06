import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Model of percolation system (N-by-N grid of sites). Each site is either open or blocked. A full site is an open site
 * that can be connected to an open site on top row via a chain of neighboring (left, right, up, down) open site.
 * The system percolates if there is a full site in the bottom row.
 *
 * Please, pay attention that row and column numeration is from 1 to N (both sides inclusively).
 *
 * The constructor takes time proportional to N^2. All methods take constant time plus a constant number of calls
 * to union-find methods union(), find(), connected() and count().
 */
public class Percolation
{
    private WeightedQuickUnionUF mainTree;  // Weighted quick union-find tree

    private int gridSize;                   // Model grid size (N)
    private boolean percolates;             // Shows if model has full site in the bottom row. False as default value.
    private byte[] mask;                    // Used to protect model from backwash and to increase performance
                                            // 1st bit - 1 if site is open
                                            // 2nd bit - 1 if site is full
                                            // 3rd bit - 1 if site is connected to top row
                                            // 4th bit - 1 if site is connected to bottom row

    /**
     * Creates new percolation system model
     * @param n grid size
     * @throws NullPointerException if grid size is less or equal to 0
     */
    public Percolation(int n)
    {
        if (n <= 0)
        {
            throw new IllegalArgumentException("Grid size should be more than 0");
        }

        mainTree = new WeightedQuickUnionUF(n*n); // Initialize tree that contains sites from n-by-n grid
        gridSize = n;                             // Save grid size

        mask = new byte[n*n];                     // Initialize mask array by 0 values
    }

    /**
     * Opens specified site if it is not open already
     * @param i row index (from 1 to grid size inclusively)
     * @param j column index (from 1 to grid size icnlusively)
     * @throws IndexOutOfBoundsException if row index or column index is less than 1 or more than grid size
     */
    public void open(int i, int j)
    {
        if (i < 1 || i > gridSize || j < 1 || j > gridSize)
        {
            throw new IndexOutOfBoundsException("Impossible to open element out of grid bound");
        }

        int index = grid2array(i, j);                   // Recalculate grid row and column index to array index

        byte status = 1;                                // Create status var and set 'Open' byte as 1 (0b0001)
        mask[index] = (byte) (mask[index] | status);    // Set 'Open' byte value on current site mask

        if (index >= 0 && index <= gridSize-1)          // If site is in top row
        {
            status = setConnectedToTop(status);         // Set 'Connected to top' byte of status as 1
            status = setFull(status);                   // Set 'Full' byte of status as 1
        }
        if (index < gridSize*gridSize && index >= gridSize*(gridSize-1))    // If site is in bottom row
        {
            status = setConnectedToBottom(status);      // Set 'Connected to bottom' byte of status as 1
        }

        // Check neighbours (up, down, left, right)
        if (i != 1)         // If there is a site on the top
        {
            status = connectNeighbour(index, grid2array(i-1, j), status);   // check top neighbour
        }
        if (i != gridSize)  // If there is a site on the bottom
        {
            status = connectNeighbour(index, grid2array(i+1, j), status);   // check bottom neighbour
        }
        if (j != 1)         // If there is a site on the left
        {
            status = connectNeighbour(index, grid2array(i, j-1), status);   // check left neighbour
        }
        if (j != gridSize)  // If there is a site on the right
        {
            status = connectNeighbour(index, grid2array(i, j+1), status);   // check right neighbour
        }

        byte rootStatus = (byte) (mask[index] | status);    // Merge result status and current site status
        mask[mainTree.find(index)] = rootStatus;            // Set result status on root site
        if (isConnectedToBottom(rootStatus) && isConnectedToTop(rootStatus))    // Check if system percolates
        {
            percolates = true;
        }
    }

    /**
     * If neighbour site is open, updates status and connects current site and neighbour in union-find tree
     * @param mainSiteIndex current site index (in array)
     * @param neighbourIndex current site's neighbour index (in array)
     * @param status current status
     * @return updated status (if neighbour is open) or initial status
     */
    private byte connectNeighbour(int mainSiteIndex, int neighbourIndex, byte status)
    {
        if (isOpen(mask[neighbourIndex]))                    // If neighbour is open
        {

            status = (byte) (status | mask[mainTree.find(neighbourIndex)]); // Find neighbour site's root in UF tree
                                                            // and merge current site and root site statuses
            mainTree.union(mainSiteIndex, neighbourIndex);  // Connect current and root sites in UF tree
        }
        return status;
    }

    /**
     * Checks if site is open
     * @param i row index (from 1 to grid size inclusively)
     * @param j column index (from 1 to grid size icnlusively)
     * @throws IndexOutOfBoundsException if row index or column index is less than 1 or more than grid size
     */
    public boolean isOpen(int i, int j)
    {
        if (i < 1 || i > gridSize || j < 1 || j > gridSize)
        {
            throw new IndexOutOfBoundsException("Impossible to check element out of grid bound");
        }
        return isOpen(mask[grid2array(i, j)]);  // Recalculate grid indexes to array index and check 'Open' byte
    }

    /**
     * Checks if site is full
     * @param i row index (from 1 to grid size inclusively)
     * @param j column index (from 1 to grid size icnlusively)
     * @throws IndexOutOfBoundsException if row index or column index is less than 1 or more than grid size
     */
    public boolean isFull(int i, int j)
    {
        if (i < 1 || i > gridSize || j < 1 || j > gridSize)
        {
            throw new IndexOutOfBoundsException("Impossible to open element out of grid bound");
        }
        return isFull(mask[mainTree.find(grid2array(i, j))]);   // Recalculate grid indexes to array index
                                                                // Find root site for it and check 'Full' byte
    }

    /**
     * Checks if there is full site in the bottom row
     * @return true if system percolates
     */
    public boolean percolates()
    {
        return percolates;
    }

    /**
     * Recalculates grid row and column indexes to array index
     * @param row row index (from 1 to grid size inclusively)
     * @param col column index (from 1 to grid size icnlusively)
     * @return array index (from 0 to grid_size*grid_size-1)
     */
    private int grid2array(int row, int col)
    {
        return (row-1) * gridSize + col - 1;
    }

    /**
     * Checks 'Open' byte
     * @param number site status
     * @return true if site is open
     */
    private boolean isOpen(int number)
    {
        return (number & 1) == 1;
    }

    /**
     * Sets 'Full' byte as 1
     * @param number site status
     * @return updated site status
     */
    private byte setFull(int number)
    {
       return (byte) (number | 2);
    }

    /**
     * Checks 'Full' byte
     * @param number site status
     * @return true if site is full
     */
    private boolean isFull(int number)
    {
        return (number & 2) == 2;
    }

    /**
     * Sets 'Connected to top' byte as 1
     * @param number site status
     * @return updated site status
     */
    private byte setConnectedToTop(int number)
    {
        return (byte) (number | 4);
    }

    /**
     * Checks 'Connected to top' byte
     * @param number site status
     * @return true if site is connected to top
     */
    private boolean isConnectedToTop(int number)
    {
        return (number & 4) == 4;
    }

    /**
     * Sets 'Connected to bottom' byte as 1
     * @param number site status
     * @return updated site status
     */
    private byte setConnectedToBottom(int number)
    {
        return (byte) (number | 8);
    }

    /**
     * Checks 'Connected to bottom' byte
     * @param number site status
     * @return true if site is connected to bottom
     */
    private boolean isConnectedToBottom(int number)
    {
        return (number & 8) == 8;
    }

    public static void main(String[] args)
    {
    }
}
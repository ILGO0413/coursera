import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Monte-Carlo simulation for percolation model.
 * System initializes all sites to be blocked, until system percolates, chooses a site uniformly among all blocked
 * sites. The fraction of sites that are opened when the system percolates provides an estimate of the
 * percolation threshold.
 *
 * To get more accurate estimate of percolation threshold, system repeats the computational experiment.
 */
public class PercolationStats
{
    private double[] results;   // Keeps estimate threshold of every trial
    private double trials;      // Expected trials count

    /**
     * Creates new Monte-Carlo simulation of percolation model (N-by-N grid).
     * The system makes several trials to get more accurate results.
     * @param n percolation model grid size
     * @param trials amount of trials
     * @throws IllegalArgumentException if grid size or amount of trials is less or equal to 0
     */
    public PercolationStats(int n, int trials)
    {
        if (n <= 0)
        {
            throw new IllegalArgumentException("Grid size should be more than 0");
        }
        if (trials <= 0)
        {
            throw new IllegalArgumentException("Trials count should be more than 0");
        }

        results = new double[trials];   // Initialize new array for every trial result
        this.trials = trials;           // Save amount of trials

        for (int i = 0; i < trials; i++)
        {
            results[i] = testPercolation(n); // Calculate simulation result for every trial
        }
    }

    /**
     * Performs Monte-Carlo simulation of percolation model.
     * @param gridSize percolation model grid size
     * @return the fraction of sites that are opened when system percolates
     */
    private double testPercolation(int gridSize)
    {
        Percolation test = new Percolation(gridSize);   // Initialize new percolation model

        double openedSites = 0;                         // Initialize open sites counter
        double allSites = gridSize*gridSize;            // Calculate all sites count

        while (!test.percolates())                      // While model does not percolate
        {
            int row = StdRandom.uniform(gridSize)+1;    // Uniformly choose row and column of the model
            int col = StdRandom.uniform(gridSize)+1;
            if (test.isOpen(row, col))                  // If the site is already open, skip this iteration
                continue;
            test.open(row, col);                        // Otherwise open the site and increase open sites counter
            openedSites++;
        }
        return openedSites/allSites;                    // Calculate fraction of open sites
    }

    /**
     * Returns sample mean of percolation threshold
     * @return sample mean
     */
    public double mean()
    {
        return StdStats.mean(results);
    }

    /**
     * Returns standard deviation of percolation threshold
     * @return sample standard deviation
     */
    public double stddev()
    {
        return StdStats.stddev(results);
    }

    /**
     * Returns low endpoint of 95% confidence interval
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo()
    {
        double mean = mean();
        double stddev = stddev();
        return mean - (1.96d * stddev / Math.sqrt(trials));
    }

    /**
     * Returns high endpoint of 95% confidence interval
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi()
    {
        double mean = mean();
        double stddev = stddev();
        return mean + (1.96d * stddev / Math.sqrt(trials));
    }

    /**
     * Test method. It takes two command-line arguments: grid size and amount of trials, performs trials
     * independently and prints the mean, standard deviation and 95% confidence interval for the percolation
     * threshold.
     * @param args command-line arguments. args[0] should be a grid size, args[1] should be an amount of trials
     * @throws IllegalArgumentException if less than 2 command-line arguments are provided
     */
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            throw new IllegalArgumentException("Please, provide grid size and trials count as arguments");
        }
        int gridSize = Integer.parseInt(args[0]);   // Parse grid size
        int trials = Integer.parseInt(args[1]);     // Parse amount of trials
        PercolationStats stats = new PercolationStats(gridSize, trials);    // Create new Monte-Carlo simulation

        // Output results
        StdOut.println("mean = "+stats.mean());
        StdOut.println("stddev = "+stats.stddev());
        StdOut.println("95% confidence interval = "+stats.confidenceLo()+", "+stats.confidenceHi());
    }
}
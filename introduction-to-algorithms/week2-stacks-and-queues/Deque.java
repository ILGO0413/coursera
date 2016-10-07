import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements double-ended queue, generalization of a stack and a queue that supports adding and removing items
 * from either the front or the back of the data structure.
 * This deque implementation does not support adding {@code null} elements.
 *
 * Supports each operation in constant worst-case time.
 * A deque containing N items uses less than 48*N + 192 bytes of memory.
 *
 * @param <Item> the type of elements held in this data structure
 */
public class Deque<Item> implements Iterable<Item>
{
    private Entry<Item> first;  // Reference to the first element of deque, default value is null
    private Entry<Item> last;   // Reference to the last element of deque, default value is null
    private int size;           // Deque size, default value is 0

    /**
     * Creates new empty deque
     */
    public Deque()
    {
    }

    /**
     * Checks if deque is empty
     * @return true if deque does not contain elements
     */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Returns the number of elements in this deque.
     * @return deque size
     */
    public int size()
    {
        return size;
    }

    /**
     * Inserts the specified element at the front of this deque.
     * @param item element that should be added
     * @throws NullPointerException if element is null
     */
    public void addFirst(Item item)
    {
        if (item == null)
        {
            throw new NullPointerException("Cannot add null to Deque");
        }

        if (size == 0)                                      // First initialization
        {
            first = new Entry<Item>(item);                  // Create new deque entry and set it as first element
            last = first;                                   // Set it as last deque element as well
        }
        else
        {                                                   // Deque contains elements at the moment
            Entry<Item> newItem = new Entry<Item>(item);    // Create new deque entry
            newItem.next = first;                           // As we insert in the front of the deque, we should set
            first.prev = newItem;                           // references between new entry and first deque entry
            first = newItem;                                // And now we set new queue entry as first
        }
        size++;                                             // Increase deque size
    }

    /**
     * Inserts the specific element at the back of the queue
     * @param item element taht should be added
     * @throws NullPointerException if element is null
     */
    public void addLast(Item item)
    {
        if (item == null)
        {
            throw new NullPointerException("Cannot add null to Deque");
        }

        if (size == 0)                                      // First initialization
        {
            first = new Entry<Item>(item);                  // Create new deque entry and set it as first element
            last = first;                                   // Set it as last deque element as well
        }
        else
        {                                                   // Deque contains elements at the moment
            Entry<Item> newItem = new Entry<Item>(item);    // Create new deque entry
            newItem.prev = last;                            // As we insert in the back of the deque, we should set
            last.next = newItem;                            // references between new entry and last deque entry
            last = newItem;                                 // And now we set new deque entry as last
        }
        size++;                                             // Increase deque size
    }

    /**
     * Retrieves and removes the first element of this deque.
     * @return first element of the deque
     * @throws NoSuchElementException if deque is empty
     */
    public Item removeFirst()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Deque is empty");
        }

        Item result = first.item;                           // Get first deque entry's value
        first = first.next;                                 // Set next entry of the first entry as the first entry
        if (first != null)                                  // If deque is not empty (first entry had next entry),
            first.prev = null;                              // then nullify reference to removed element
        else
            last = null;                                    // else nullify reference to last element
        size--;                                             // Decrease deque size
        return result;
    }

    /**
     * Retrieves and removes the last element of this deque
     * @return last element of the deque
     * @throws NoSuchElementException if deque is empty
     */
    public Item removeLast()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Deque is empty");
        }

        Item result = last.item;                            // Get last deque entry's value
        last = last.prev;                                   // Set prev. entry of the last entry as the last entry
        if (last != null)                                   // If deque is not empty (last entry had prev. entry),
            last.next = null;                               // then nullify reference to removed element
        else
            first = null;                                   // else nullify reference to first element
        size--;
        return result;
    }

    /**
     * Returns an iterator over the elements in this deque in proper sequence.
     * The elements will be returned in order from first (front) to last (back).
     *
     * Iterator supports each operation in constant worst-case time.
     *
     * @return an iterator
     */
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    public static void main(String[] args)
    {
    }

    /**
     * Deque entry.
     * Contains reference to next and previous entry of the deque, and item that should be stored in deque
     * @param <Item> the type of elements held in this data structure
     */
    private class Entry<Item>
    {
        private Entry<Item> next;                           // Reference to the next deque entry
        private Entry<Item> prev;                           // Reference to the previous deque entry
        private Item item;                                  // Item that should be stored in deque

        /**
         * Creates new deque element
         * @param item item that should be stored in deque
         */
        public Entry(Item item)
        {
            this.item = item;
        }
    }

    /**
     * The iterator over the elements in this deque in proper sequence.
     * The elements will be returned in order from first (front) to last (back).
     */
    private class DequeIterator implements Iterator<Item>
    {
        private Entry<Item> currentItem = first;            // Current deque entry

        @Override
        /**
         * Shows if iterator has next element of the deque
         */
        public boolean hasNext()
        {
            return currentItem != null;
        }

        @Override
        /**
         * Returns next element of the deque
         * @return next element of the deque
         * @throws NoSuchElementException if there are no more items to return
         */
        public Item next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException("No next item in iterator");
            }

            Item result = currentItem.item;                 // Get next item
            currentItem = currentItem.next;                 // Set next element as current element
            return result;
        }

        @Override
        /**
         * This iterator does not support remove() operation.
         * @throws UnsupportedOperationException always
         */
        public void remove()
        {
            throw new UnsupportedOperationException("Prohibited to remove items from current iterator");
        }
    }
}
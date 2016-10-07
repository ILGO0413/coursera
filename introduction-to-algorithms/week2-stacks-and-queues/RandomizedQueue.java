import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements randomized queue, specific implementation of queue that allows you to enqueue uniformly random elements
 * only.
 * This queue implementation does not support adding {@code null} elements.
 *
 * This implementation supports each randomized queue operation (besides creating an iterator) in constant amortized
 * time.
 * A randomized queue containing n items uses less than 48n + 192 bytes of memory.
 *
 * @param <Item> the type of elements held in this data structure
 */
public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] items;                                   // Queue elements
    private int currentSize;                                // Current number of the elements
    private int fullSize;                                   // Current queue capacity

    /**
     * Creates new empty randomized queue
     */
    public RandomizedQueue()
    {
        fullSize = 1;                                       // Start from capacity = 1
        items = (Item[]) new Object[fullSize];              // Initialize empty items array
    }

    /**
     * Checks if queue is empty
     * @return true if queue is empty
     */
    public boolean isEmpty()
    {
        return currentSize == 0;
    }

    /**
     * Returns the number of elements in this deque.
     * @return queue size
     */
    public int size()
    {
        return currentSize;
    }

    /**
     * Adds item to the randomized queue
     * @param item element that should be added
     * @throws NullPointerException if element is null
     */
    public void enqueue(Item item)
    {
        if (item == null)
        {
            throw new NullPointerException("Cannot add null item");
        }
        if (currentSize == fullSize)                        // If queue is full
        {
            resize(2d);                                     // Double queue size
        }
        items[currentSize] = item;                          // Add new item to the randomized queue
        currentSize++;                                      // Increase size counter
    }

    /**
     * Resize queue array. New array size = current size * coefficient
     * @param coeff coefficient
     */
    private void resize(double coeff)
    {
        fullSize = (int) (fullSize*coeff);                  // Calculate new array capacity
        Item[] newArray = (Item[]) new Object[fullSize];    // Create new array
        for (int i = 0; i < currentSize; i++)               // Copy every item from old array to new array
        {
            newArray[i] = items[i];
        }
        items = newArray;                                   // Change old array to new array
    }

    /**
     * Remove and return random element of the queue
     * @return random removed queue element
     * @throws NoSuchElementException if queue is empty
     */
    public Item dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty");
        }

        int index = StdRandom.uniform(currentSize);         // Get uniformly random element's index of the queue
        Item result = items[index];                         // Get element value
        items[index] = items[--currentSize];                // Set last element on empty space, decrease element number
        items[currentSize] = null;                          // Nullify last element reference to prevent loitering
        if (fullSize >= 4 && fullSize/currentSize >= 4)     // If the queue is quarter full
        {
            resize(0.5d);                                   // Halve queue size
        }
        return result;
    }

    /**
     * Returns (but does not removes) a random element of the queue
     * @return random queue element
     * @throws NoSuchElementException if queue is empty
     */
    public Item sample()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty");
        }

        return items[StdRandom.uniform(currentSize)];         // return uniformly random element's index of the queue
    }

    /**
     * Returns an iterator over the elements in this queue in random order.
     * Iterator implementation does not support remove() operation.
     *
     * @return an iterator over the elements in this deque in random order
     */
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args)
    {
    }

    /**
     * Implementation of the iterator over the elements in this queue.
     * Iterator implementation supports operations next() and hasNext() in constant worst-case time and construction
     * in linear time. Iterator uses a linear amount of extra memory.
     */
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int[] indexes;                              // Keeps indexes of the queue in random order
        private int counter;                                // Iterator counter

        /**
         * Creates (and shuffles) new iterator
         */
        public RandomizedQueueIterator()
        {
            shuffle();
        }

        @Override
        /**
         * Checks if iterator has next element
         * @return true if there is at least one more element
         */
        public boolean hasNext()
        {
            return counter < indexes.length;
        }

        /**
         * Shuffles indexes array for random order to randomzied queue values
         */
        private void shuffle()
        {
            indexes = new int[currentSize];                 // Initialize new indexes array
            for (int i = 0; i < currentSize; i++)           // Set values from 0 to current queue size (exclusively)
            {
                indexes[i] = i;
            }
            StdRandom.shuffle(indexes);                     // Shuffle indexes
        }

        @Override
        /**
         * Returns next random element of the randomized queue
         * @return random element
         * @throws NoSuchElementException if there are no more items to return
         */
        public Item next()
        {
            if (counter >= indexes.length || size() == 0)
                throw new NoSuchElementException("Iterator has not next element");
            return items[indexes[counter++]];               // Get next index and get queue item by the index
        }

        @Override
        /**
         * This iterator does not support remove() operation.
         * @throws UnsupportedOperationException always
         */
        public void remove()
        {
            throw new UnsupportedOperationException("Prohibited to remove objects from iterator");
        }
    }
}
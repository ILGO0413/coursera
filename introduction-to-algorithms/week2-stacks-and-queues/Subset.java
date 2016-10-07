import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * A client program that takes a command-line integer k, reads in sequence of N string from standart input
 * using StdIn.readString(); and prints out exactly k of them, uniformly at random. Each item from the sequence
 * may be printed out at most once.
 * Assume that 0 <= k <= N, where N is the number of standard input.
 * The running time of Subset is linear in the size of the input. It uses constant amount of memory and one
 * RandomizedQueue object of maximum size at most k.
 */
public class Subset
{
    /**
     * Reads N strings and writes k random strings from the input.
     * It is expected that args[0] is k (number of random strings to output)
     * @param args command-line args
     */
    public static void main(String[] args)
    {
        RandomizedQueue<String> rq = new RandomizedQueue<>();   // Create new RandomizedQueue
        int counter = 0;                                        // Input strings counter
        int k = Integer.parseInt(args[0]);                      // Number of random strings to output

        /**
         * It is possible to solve the task using only one RandomizedQueue of maximum size of k.
         * To do so, we should add first k elements to the queue. For every i-th element (where i >= k) we should
         * make some calculations to decide should we replace random queue element or not.
         * Moreover, we should guarantee that every element has the same possibility to be in the queue when it's done.
         *
         * When we add i-th element, we pick uniformly random value from 0 to i (both inclusively) and check if it is
         * less than k. If so, we remove random element from the queue and add new one.
         * Possibility to be added: k / (i + 1).
         *
         * To calculate possibility to stay in final queue, we should calculate possibility to be removed from it.
         * The possibility for every next element j to be added is k / (j + 1), the possibility to remove exactly this
         * element from the queue is 1 / k. The possibility to be removed by element j is 1 / (j + 1).
         * The possibility to stay in the array after element j check is 1 - (1 / (j + 1)) = j / (j + 1)
         *          *
         * Probability for every i element to remain in the final queue is:
         * possibility to be added * possibility to stay after i+1 * possibility to stay after i+2 * ...
         * or
         * k / (i + 1)  * (i + 1)/(i + 2) * (i + 2)/(i + 3) * ... * N / (N + 1)
         * Result possibility is k / (N + 1) for every element. It is uniform.
         *
         */

        while (!StdIn.isEmpty())                            // While we have elements
        {
            String s = StdIn.readString();                  // Read next element

            if (counter < k)                                // If the queue has less than k elements
            {
                rq.enqueue(s);                              // Just add it to randomized queue
            }
            else
            {
                if (StdRandom.uniform(0, counter+1) < k)    // or pick uniformly random value from 0 to i (inclusively)
                {                                           // (or from 0 to i+1 exclusively like here)
                    rq.dequeue();                           // if it is less than k, remove random queue element
                    rq.enqueue(s);                          // and add new element to the queue
                }
            }
            counter++;                                      // increase input strings counter
        }


        for (int i = 0; i < k; i++)                         // output K random strings
        {
            StdOut.println(rq.dequeue());
        }
    }
}
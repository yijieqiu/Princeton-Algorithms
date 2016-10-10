import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * A client that takes a command-line integer k; reads in a sequence of N
 * strings from standard input using StdIn.readString(); and prints out exactly
 * k (0 <= k <= n) of them, uniformly at random. Each item from the sequence can
 * be printed out at most once.
 *
 * @author Yijie Qiu
 */
public class Subset {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            queue.enqueue(str);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }

}

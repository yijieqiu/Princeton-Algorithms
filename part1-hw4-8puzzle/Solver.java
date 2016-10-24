import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * A solver client for 8-Puzzle questions using A* algorithm
 *
 * @author  Yijie Qiu
 */
public class Solver {

    private MinPQ<SearchNode> queue;
    private MinPQ<SearchNode> twinQueue;
    private List<Board> progress;
    private SearchNode original;
    private SearchNode twin;

    /**
     * Find a solution to the initial board (using the A* algorithm)
     * @param initial Initial board
     */
    public Solver(Board initial) {
        // Initialization
        original = new SearchNode(null, initial, 0);
        twin = new SearchNode(null, initial.twin(), 0);
        queue = new MinPQ<>();
        twinQueue = new MinPQ<>();
        progress = new ArrayList<>();
        queue.insert(original);
        twinQueue.insert(twin);
        // Alternate between original and twin game tree
        boolean alternate = true;

        // Attempt to solve the board
        while (true) {
            if (alternate) {
                // Make progress in the game tree of original board
                original = queue.delMin();
                if (original.current.isGoal()) {
                    progress.add(original.current);
                    break;
                }
                progress.add(original.current);
                for (Board neighbor : original.current.neighbors()) {
                    if (!neighbor.equals(original.previous)) {
                        queue.insert(new SearchNode(original.current, neighbor,
                                original.steps + 1));
                    }
                }

                alternate = false;
            } else {
                // Make progress in the game tree of twin board
                twin = twinQueue.delMin();
                if (twin.current.isGoal()) {
                    break;
                }
                for (Board neighbor : twin.current.neighbors()) {
                    if (!neighbor.equals(twin.previous)) {
                        twinQueue.insert(new SearchNode(twin.current, neighbor,
                                twin.steps + 1));
                    }
                }

                alternate = true;
            }
        }
    }

    /**
     * Is the intial board solvable?
     * @return True if the initial board is solvable, false otherwise
     */
    public boolean isSolvable() {
        // The original board is unsolvable if we reached solution in twin
        // game tree instead
        return !(twin.current.isGoal());
    }

    /**
     * Minimum number of moves to solve initial board. -1 if unsolvable
     * @return Min number of moves, or -1 for unsolvable boards
     */
    public int moves() {
        if (isSolvable()) {
            return original.steps;
        } else {
            return -1;
        }
    }

    /**
     * Get sequence of boards in the shortest solution. Null if unsolvable
     * @return All intermediate boards, or null for unsolvable boards
     */
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return progress;
        } else {
            return null;
        }
    }

    // Test client provided in assignment write-up
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board current;
        private Board previous;
        private int steps;

        SearchNode(Board prev, Board curr, int n) {
            current = curr;
            previous = prev;
            steps = n;
        }

        public int compareTo(SearchNode node) {
            int m1 = this.manhattanPriority();
            int m2 = node.manhattanPriority();
            if (m1 != m2) {
                return Integer.compare(m1, m2);
            } else {
                return Integer.compare(this.hammingPriority(), node
                        .hammingPriority());
            }
        }

        public int manhattanPriority() {
            return current.manhattan() + steps;
        }

        public int hammingPriority() {
            return current.hamming() + steps;
        }
    }
}

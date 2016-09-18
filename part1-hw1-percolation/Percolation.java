import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Class containing all API implementations for modeling the percolation
 * problem using union-find
 *
 * FIXME: The current implementation does not resolve the "backwash" issue
 * @Author Yijie Qiu
 */
public class Percolation {

    private int n;
    private WeightedQuickUnionUF uf;
    // 2D grid flattened into an 1d array to accomodate the UnionFind API
    private boolean[] grid;

    /**
     * Initialize an n-by-n grid, with all sites blocked
     * @param n Dimension of the grid
     * @throws IllegalArgumentException if n <= 0
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid n. n must be " +
                    "greater than 0");
        }

        this.n = n;
        // Include two virtual sites, one at the top, another at the bottom
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.grid = new boolean[n * n + 2];
        for (int i = 1; i < grid.length - 1; i++) {
            grid[i] = false;
        }
        grid[0] = true;
        grid[grid.length - 1] = true;
    }

    /**
     * Open site (row i, column j) if it is not open already
     * @param i Row number
     * @param j Column number
     */
    public void open(int i, int j) {
        int index = calculateIndex(i, j);
        grid[index] = true;

        // Connect all open sites around the newly opened cell
        if (i != 1 && isOpen(i - 1, j)) {
            uf.union(index, calculateIndex(i - 1, j));
        }
        if (i != this.n && isOpen(i + 1, j)) {
            uf.union(index, calculateIndex(i + 1, j));
        }
        if (j != 1 && isOpen(i, j - 1)) {
            uf.union(index, calculateIndex(i, j - 1));
        }
        if (j != n && isOpen(i, j + 1)) {
            uf.union(index, calculateIndex(i, j + 1));
        }

        // Connect to top and bottom virtual sites if the newly opened cell is
        // on the first or last row
        if (i == 1) {
            uf.union(index, 0);
        }
        if (i == this.n) {
            uf.union(index, grid.length - 1);
        }
    }

    /**
     * Is site (row i, column j) open?
     * @param i Row number
     * @param j Column number
     * @return true if the cell is open, false otherwise
     */
    public boolean isOpen(int i, int j) {
        int index = calculateIndex(i, j);
        return grid[index];
    }

    /**
     * Is site (row i, column j) full?
     * Def: A full site is an open site that can be connected to an open site
     * in the top row via a chain of neighboring (left, right, up, down)
     * open sites
     * @param i Row number
     * @param j Column number
     * @return true if the cell is full, false otherwise
     */
    public boolean isFull(int i, int j) {
        int index = calculateIndex(i, j);
        return isOpen(i, j) && uf.connected(0, index);
    }

    /**
     * Does the system percolate?
     * Def: the system percolates if there is a full site in the bottom row
     * @return true if the system percolates, false otherwise
     */
    public boolean percolates() {
        return uf.connected(0, grid.length - 1);
    }

    /**
     * Given (row i, column j), calculate the corresponding index in grid array
     * @param i Row number
     * @param j Column number
     * @return index in 1D grid array
     * @throws ArrayIndexOutOfBoundsException when i or j is beyond grid bounds
     */
    private int calculateIndex(int i, int j) {
        validateInput(i, j);
        return (i - 1) * this.n + j;
    }

    /**
     * Check if the given row and column is within grid bounds
     * @param i Row number
     * @param j Column number
     * @throws ArrayIndexOutOfBoundsException when i or j is beyond grid bounds
     */
    private void validateInput(int i, int j) {
        if (i <= 0 || i > this.n) {
            throw new ArrayIndexOutOfBoundsException("Row number can only be " +
                    "between 1 and n (inclusive)");
        }
        if (j <= 0 || j > this.n) {
            throw new ArrayIndexOutOfBoundsException("Column number can only " +
                    "be between 1 and n (inclusive)");
        }
    }


}

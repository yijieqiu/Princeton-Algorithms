import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Immutable n x n board representing the state of 8-Puzzle game at a given time
 *
 * @author Yijie Qiu
 */
public class Board {

    private int blankRow;
    private int blankCol;
    private final int mDimension;
    private final int[] mBlocks;

    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new NullPointerException("Input cannot be null");
        }
        mDimension = blocks.length;
        mBlocks = new int[mDimension * mDimension];

        int index = 0;
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                mBlocks[index++] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
    }

    /**
     * @return Board dimension
     */
    public int dimension() {
        return mDimension;
    }

    /**
     * @return number of blocks out of place
     */
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < mBlocks.length; i++) {
            if (mBlocks[i] == 0) continue;
            if (i != mBlocks[i] - 1) {
                hamming++;
            }
        }
        return hamming;
    }

    /**
     * Compute sum of Manhattan distances between blocks and goal
     * @return Sum of Manhattan distances
     */
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < mBlocks.length; i++) {
            if (mBlocks[i] == 0) continue;

            int goal = mBlocks[i] - 1;
            if (i != goal) {
                manhattan += Math.abs(getRow(goal) - getRow(i));
                manhattan += Math.abs(getCol(goal) - getCol(i));
            }
        }
        return manhattan;
    }

    /**
     * Is the board the goal board ?
     * Goal board is defined as a board on which numbers 1 to 8 are in order
     * 1 2 3
     * 4 5 6
     * 7 8 x
     *
     * @return True if the board is goal board, false otherwise
     */
    public boolean isGoal() {
        for (int i = 0; i < mBlocks.length; i++) {
            if (mBlocks[i] == 0) continue;
            if (i != mBlocks[i] - 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Construct a board that is obtained by exchanging any pair of blocks
     * @return A twin board of the current board
     */
    public Board twin() {
        int[] twin = deepCopy(mBlocks);

        // Since twin board can be obtained by exchanging any pair of blocks,
        // we will exchange the first eligible pair (pair without blank)
        for (int i = 0; i < twin.length - 1; i++) {
            if (twin[i] != 0 && twin[i + 1] != 0) {
                swap(twin, i, i + 1);
                break;
            }
        }
        return new Board(to2DArray(twin));
    }

    /**
     * Get all neighbor boards of the current board
     * Neighbor board is defined as a board that can be reached in one move from
     * the current board
     *
     * @return All neighbor boards
     */
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        int[] copy;

        int blankIndex = getIndex(blankRow, blankCol);
        if (blankRow > 0) {
            // Neighbor board with one tile moved down
            copy = deepCopy(mBlocks);
            swap(copy, blankIndex, getIndex(blankRow - 1, blankCol));
            neighbors.add(new Board(to2DArray(copy)));
        }
        if (blankRow + 1 < mDimension) {
            // Neighbor board with one tile moved up
            copy = deepCopy(mBlocks);
            swap(copy, blankIndex, getIndex(blankRow + 1, blankCol));
            neighbors.add(new Board(to2DArray(copy)));
        }
        if (blankCol > 0) {
            // Neighbor board with one tile moved right
            copy = deepCopy(mBlocks);
            swap(copy, blankIndex, getIndex(blankRow, blankCol - 1));
            neighbors.add(new Board(to2DArray(copy)));
        }
        if (blankCol + 1 < mDimension) {
            // Neighbor board with one tile moved left
            copy = deepCopy(mBlocks);
            swap(copy, blankIndex, getIndex(blankRow, blankCol + 1));
            neighbors.add(new Board(to2DArray(copy)));
        }

        return neighbors;
    }

    @Override
    public boolean equals(Object y) {
        if (y == null || !(y instanceof Board)) return false;
        Board that = (Board) y;
        return Arrays.equals(mBlocks, that.mBlocks);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mDimension + "\n");
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                int index = getIndex(i, j);
                sb.append(String.format("%2d ", mBlocks[index]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Convert the 1D board array to a n x n 2D array
     * @return 2D board
     */
    private int[][] to2DArray(int[] arr) {
        int[][] result = new int[mDimension][mDimension];
        for (int i = 0; i < arr.length; i++) {
            result[getRow(i)][getCol(i)] = arr[i];
        }
        return result;
    }

    /**
     * @return Deep copy of a 1D array
     */
    private int[] deepCopy(int[] original) {
        int[] copy = new int[original.length];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i];
        }
        return copy;
    }


    private void swap(int[] board, int index1, int index2) {
        int temp = board[index1];
        board[index1] = board[index2];
        board[index2] = temp;
    }

    /**
     * Given coordinate <i, j>, find the corresponding index in board array
     * @param i x-coordinate
     * @param j y-coordinate
     * @return index
     */
    private int getIndex(int i, int j) {
        return i * mDimension + j;
    }

    /**
     * Get row number in 2D board array corresponding to the given 1D index
     * @param index
     * @return
     */
    private int getRow(int index) {
        return index / mDimension;
    }

    /**
     * Get col number in 2D board array corresponding to the given 1D index
     * @param index
     * @return
     */
    private int getCol(int index) {
        return index % mDimension;
    }

}

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Class that allows one to perform Monte Carlo simulation of some number of
 * trails on a n-by-n grid to gain an estimate for the percolation threshold
 *
 * @Author Yijie Qiu
 */
public class PercolationStats {

    private static final double Z95 = 1.96;

    private int n;
    private int trials;
    private double[] results;

    /**
     * Perform x trials independent experiments on an n-by-n grid
     * @param n Grid dimension
     * @param trials Number of trials
     * @throws IllegalArgumentException if n <= 0 or trials <= 0
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Grid dimension and number of " +
                    "trails should both be greater than 0");
        }

        this.n = n;
        this.trials = trials;
        this.results = new double[this.trials];

        for (int t = 0; t < this.trials; t++) {
            results[t] = getPercolationThresholdForTrial();
        }
    }

    /**
     * @return Sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * @return Sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * @return Low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - (Z95 * stddev() / Math.sqrt(this.trials));
    }

    /**
     * @return High endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + (Z95 * stddev() / Math.sqrt(this.trials));
    }


    /**
     * Test client to take two parameters from command line and run the
     * simulation experiment
     * @param args 1) n - Grid dimension 2) trails - number of trials to run
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0].trim());
        int trials = Integer.parseInt(args[1].trim());

        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println("95% confidence interval = "
                            + stats.confidenceLo()
                            + ", " + stats.confidenceHi());

    }

    /**
     * Initialize a n-by-n grid and open sites randomly until the system
     * percolates
     * @return Percolation threshold for the trial
     */
    private double getPercolationThresholdForTrial() {
        Percolation attempt = new Percolation(this.n);
        int openSites = 0;
        do {
            int i = StdRandom.uniform(1, this.n + 1);
            int j = StdRandom.uniform(1, this.n + 1);
            if (!attempt.isOpen(i, j)) {
                attempt.open(i, j);
                openSites++;
            }
        } while (!attempt.percolates());

        return (double) openSites / (this.n * this.n);
    }

}

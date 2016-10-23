import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Examines 4 points at a time and checks whether they all lie on the same
 * line segment, returning all such line segments, using brute force algorithm
 *
 * @author Yijie Qiu
 */
public class BruteCollinearPoints {

    private int mSegmentCount;
    private LineSegment[] mSegments;

    /**
     * Constructor. Given array of {@link Point}, find and list all collinear
     * {@link LineSegment}
     *
     * @param points Array of points
     */
    public BruteCollinearPoints(Point[] points) {
        // Input validation
        if (points == null) {
            throw new NullPointerException("Constructor input cannot be null");
        }

        /*
         * Sort input points by y-axis, tie-break by x-axis, such that all
         * line segment candidates visited below will be constructed in one
         * direction. Thus avoid over-counting (e.g segment p->s and s->p)
         */
        Arrays.sort(points);
        // Continue input validation
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException("Input point cannot be null");
            }
            if (i == points.length - 1) break;

            if (points[i + 1] != null &&
                    points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Cannot have repeated " +
                        "points");
            }
        }

        List<LineSegment> segments = new ArrayList<>();

        // Iterate and evaluate all possibilities, O(n^4)
        for (int p = 0; p < points.length - 3; p++) {
            for (int q = p + 1; q < points.length - 2; q++) {
                for (int r = q + 1; r < points.length - 1; r++) {
                    for (int s = r + 1; s < points.length; s++) {
                        double slopePQ = points[p].slopeTo(points[q]);
                        double slopePR = points[p].slopeTo(points[r]);
                        double slopePS = points[p].slopeTo(points[s]);
                        if (slopePQ == slopePR && slopePQ == slopePS) {
                            segments.add(new LineSegment(points[p], points[s]));

                        }
                    }
                }
            }
         }

         mSegmentCount = segments.size();
         mSegments = segments.toArray(new LineSegment[mSegmentCount]);
    }

    /**
     * @return Number of line segments in the given set of points
     */
    public int numberOfSegments() {
        return mSegmentCount;
    }

    /**
     * All possible line segments in the given set of points
     * - No duplicates (p -> s and s -> p are considered the same)
     * - No subsegments
     * @return Array of line segmetns
     */
    public LineSegment[] segments() {
        return mSegments;
    }

    /**
     * Test client supplied in assignment writeup
     * @param args
     */
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

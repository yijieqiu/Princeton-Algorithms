import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A faster, non brute force approach for finding line segments that connects
 * 4 or more of the given points in a plane.
 */
public class FastCollinearPoints {

    private int mSegmentCount;
    private LineSegment[] mSegments;

    /**
     * Constructor, given an array of {@link Point} in plane, find all line
     * segments with the polar sort based, non brute force approach
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        // Input validation
        if (points == null) {
            throw new NullPointerException("Constructor input cannot be null");
        }
        Arrays.sort(points);
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
        for (int i = 0; i < points.length - 3; i++) {
            // (Re)sort input array to restore natural order by y-axis value
            Arrays.sort(points);
            // Now sort the input array by slope with respect to reference point
            // Reference point is points[i] for each iteration
            Arrays.sort(points, points[i].slopeOrder());

            // Start iteration from index 1, because index 0 is always the
            // reference point after sort
            for (int j = 1; j < points.length - 2; j++) {
                if (collinear(points[0], points[j], points[j + 2]) &&
                        !collinear(points[0], points[j], points[j - 1])) {
                    /*
                     * Found a potential line segment with 4 collinear points
                     * 1. Check if there's the line segment involves more points
                     * 2. Need to make sure we are searching in direction of
                     * increasing y (to avoid double-counting)
                     */

                    if (points[0].compareTo(points[j]) < 0) {
                        int index  = j + 2; // At least 4 points
                        for (int k = j + 3; k < points.length; k++) {
                            if (collinear(points[0], points[j], points[k])) {
                                index++;
                            } else {
                                break;
                            }
                        }
                        // Reached longest line segment
                        segments.add(new LineSegment(points[0], points[index]));
                    }
                }
            }
        }

        mSegmentCount = segments.size();
        mSegments = segments.toArray(new LineSegment[segments.size()]);
    }

    public int numberOfSegments() {
        return mSegmentCount;
    }

    public LineSegment[] segments() {
        return mSegments;
    }

    private boolean collinear(Point p, Point q, Point r) {
        return p.slopeTo(q) == p.slopeTo(r);
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

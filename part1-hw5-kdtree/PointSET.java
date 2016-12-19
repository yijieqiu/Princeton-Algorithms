import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Brute-force approach for solving range search and nearestNeighbor neighbor search
 * problems, with the following performance guarantee:
 * insert, contains: O(logN)
 * nearestNeighbor, range: O(N)
 *
 * @author yijieqiu
 */
public class PointSET {

    private Set<Point2D> points;
    private RectHV boundary;

    /**
     * Construct an empty set of points
     */
    public PointSET() {
        this.points = new TreeSet<>();
        this.boundary = new RectHV(0.0, 0.0, 1.0, 1.0);
    }

    /**
     * Is the set empty?
     * @return true if the set is empty, false otherwise
     */
    public boolean isEmpty() {
       return this.points.isEmpty();
    }

    /**
     * @return Number of points in the set
     */
    public int size() {
        return this.points.size();
    }

    /**
     * Add a point to the set (if it is not already in the set)
     * @param p Point to be added
     * @throws NullPointerException if the input point is null
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Cannot insert null point into set");
        }
        if (!contains(p)) {
            this.points.add(p);
        }
    }

    /**
     * Does the set contains point p?
     * @param p Point to be searched for in set
     * @return true if set contains p, false otherwise
     */
    public boolean contains(Point2D p) {
        return this.points.contains(p);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        this.boundary.draw();
        for (Point2D p : this.points) {
            p.draw();
        }
    }

    /**
     * Get all points in the set that are within the given rectangle
     * @param rect Rectangle upon which the range search is performed
     * @return Collection of all points in the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> result = new ArrayList<>();
        for (Point2D p : this.points) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     * @param p Point upon which the nearest neighbor search is performed
     * @return null if set is empty; otherwise return p's nearest neighbor
     */
    public Point2D nearest(Point2D p) {
        Point2D nearest = null;
        double currSquaredDistance;
        double minSquaredDistance = Double.MAX_VALUE;

        for (Point2D point : this.points) {
            currSquaredDistance = p.distanceSquaredTo(point);
            if (currSquaredDistance < minSquaredDistance) {
                nearest = point;
                minSquaredDistance = currSquaredDistance;
            }
        }
        return nearest;
    }
}

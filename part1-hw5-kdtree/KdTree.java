import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * 2d-Tree approach for solving range search and nearestNeighbor neighbor search
 * problems
 *
 *@author yijieqiu
 */
public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private Node root;
    private int size;

    /**
     * Construct an empty 2d tree
     */
    public KdTree() { }

    /**
     * Is the set empty?
     * @return true if the set is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * @return Number of points in the set
     */
    public int size() {
        return this.size;
    }

    /**
     * Add a point to the set (if it is not already in the set)
     * @param p Point to be added
     * @throws NullPointerException if the input point is null
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Cannot insert a null point");
        }
        RectHV rect;
        if (this.root == null) {
            rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        } else {
            rect = root.rect;
        }

        this.root = insert(p, this.root, VERTICAL, rect);
    }

    /**
     * Does the set contains point p?
     * @param p Point to be searched for in set
     * @return true if set contains p, false otherwise
     */
    public boolean contains(Point2D p) {
        return search(p, this.root);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        draw(this.root);
    }

    /**
     * Get all points in the set that are within the given rectangle
     * @param rect Rectangle upon which the range search is performed
     * @return Collection of all points in the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> result = new ArrayList<>();
        range(rect, this.root, result);
        return result;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     * @param p Point upon which the nearest neighbor search is performed
     * @return null if set is empty; otherwise return p's nearest neighbor
     */
    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        return nearestNeighbor(p, this.root, this.root.p);
    }

    /**
     * Helper function to recursively insert a new point into the 2d tree.
     * @param p Point to be inserted
     * @param currentNode Current parent node
     * @param division division to use for the new node
     */
    private Node insert(Point2D p, Node currentNode, boolean division,
                        RectHV rect) {
        if (currentNode == null) {
            // Base case: Reached a leaf, add a new node
            currentNode = new Node(p, division, rect);
            this.size++;
            return currentNode;
        }
        // Dedupe
        if (currentNode.p.x() == p.x() && currentNode.p.y() == p.y()) {
            return currentNode;
        }

        // Recursive case: Search down a subtree to find insert position
        if (currentNode.division == VERTICAL) {
            // Choose subtree based on x-coordinate
            if (p.x() <= currentNode.p.x()) {
                RectHV rectLb = currentNode.lb != null ?
                                currentNode.lb.rect :
                                new RectHV(rect.xmin(), rect.ymin(),
                                        currentNode.p.x(), rect.ymax());
                currentNode.lb = insert(p, currentNode.lb, HORIZONTAL, rectLb);
            } else {
                RectHV rectRt = currentNode.rt != null ?
                                currentNode.rt.rect :
                                new RectHV(currentNode.p.x(), rect.ymin(),
                                        rect.xmax(), rect.ymax());
                currentNode.rt = insert(p, currentNode.rt, HORIZONTAL, rectRt);
            }
        } else {
            // Choose subtree based on y-coordinate
            if (p.y() <= currentNode.p.y()) {
                RectHV rectLb = currentNode.lb != null ?
                        currentNode.lb.rect :
                        new RectHV(rect.xmin(), rect.ymin(),
                                rect.xmax(), currentNode.p.y());
                currentNode.lb = insert(p, currentNode.lb, VERTICAL, rectLb);
            } else {
                RectHV rectRt = currentNode.rt != null ?
                        currentNode.rt.rect :
                        new RectHV(rect.xmin(), currentNode.p.y(),
                                rect.xmax(), rect.ymax());
                currentNode.rt = insert(p, currentNode.rt, VERTICAL, rectRt);
            }
        }

        return currentNode;
    }

    /**
     * Helper function to recursively search for a point in the 2d tree
     * @param p Point to be searched
     * @param currentNode Current node in the tree
     */
    private boolean search(Point2D p, Node currentNode) {
        // Base case 1: Point not found
        if (currentNode == null) {
            return false;
        }
        // Base case 2: Found point in tree
        if (currentNode.p.equals(p)) {
            return true;
        }

        // Recursive case: Recursively search subtrees
        if (currentNode.division == VERTICAL) {
            if (p.x() <= currentNode.p.x()) {
                return search(p, currentNode.lb);
            } else {
                return search(p, currentNode.rt);
            }
        } else {
            if (p.y() <= currentNode.p.y()) {
                return search(p, currentNode.lb);
            } else {
                return search(p, currentNode.rt);
            }
        }
    }

    /**
     * Helper function to recursively draw the 2d tree as a colored unit block
     * @param currentNode Current node to be drawn
     */
    private void draw(Node currentNode) {
        if (currentNode != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            currentNode.p.draw();

            StdDraw.setPenRadius();
            if (currentNode.division) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(currentNode.p.x(), currentNode.rect.ymin(),
                             currentNode.p.x(), currentNode.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(currentNode.rect.xmin(), currentNode.p.y(),
                        currentNode.rect.xmax(), currentNode.p.y());
            }
        }
    }

    /**
     * Helper function to recursively perform range search for given rectangle
     * @param rect Rectangle range
     * @param currentNode Current node to be evaluated
     * @param result List of points contained in the given rectangle
     */
    private void range(RectHV rect, Node currentNode, List<Point2D> result) {
        if (currentNode == null) {
            // Base case: Reached leaf node
            return;
        }
        if (!currentNode.rect.intersects(rect)) {
            // Pruning rule: No need to search if rectangles don't intersect
            return;
        }
        // Recursive cases (in-order tree traversal)
        range(rect, currentNode.lb, result);
        if (rect.contains(currentNode.p)) {
            result.add(currentNode.p);
        }
        range(rect, currentNode.rt, result);
    }

    /**
     * Helper function to perform nearest neighbor search recursively
     * node to a given point
     * @param p Point2D instance to find nearest neighbor  for
     * @param currentNode Current node in the tree
     * @param nearest Current nearest neighbor
     */
    private Point2D nearestNeighbor(Point2D p, Node currentNode,
                                    Point2D nearest) {
        if (currentNode == null) return nearest;

        double currentMinSquaredDist = nearest.distanceSquaredTo(p);
        // Prune search scope using distance between p and underlying rectangle
        if (currentNode.rect.distanceSquaredTo(p) >= currentMinSquaredDist) {
            return nearest;
        }

        // Update closest neighbor is applicable
        if (currentNode.p.distanceSquaredTo(p) < currentMinSquaredDist) {
            nearest = currentNode.p;
        }

        // Recursive case: When there're two subtrees, search the subtree on
        // the same side of splitting line as the query point first
        if ((currentNode.division == VERTICAL && p.x() < currentNode.p.x()) ||
                (currentNode.division == HORIZONTAL &&
                        p.y() < currentNode.p.y())) {
            // Search left subtree first
            nearest = nearestNeighbor(p, currentNode.lb, nearest);
            nearest = nearestNeighbor(p, currentNode.rt, nearest);
        } else {
            // Search right subtree first
            nearest = nearestNeighbor(p, currentNode.rt, nearest);
            nearest = nearestNeighbor(p, currentNode.lb, nearest);
        }

        return nearest;
    }

    /**
     * Helper class for representing a 2D point and the rectangle related to
     * it in a 2d tree
     */
    private class Node {
        private Point2D p;   // the point
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree
        private RectHV rect; // the axis-aligned rectangle
        // In the context of 2d tree, VERTICAL (true) means that
        // the left/right subtree decision is made based on x-coordinate values
        private boolean division;

        private Node(Point2D point, boolean division, RectHV rect) {
            this.p = point;
            this.division = division;
            this.rect = rect;
        }
    }
}

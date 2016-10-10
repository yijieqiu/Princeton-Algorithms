import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of deque (double-ended queue) using linked list
 *
 * @author Yijie Qiu
 */

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    /**
     * Construct an empty deque
     */
    public Deque() {
    }

    /**
     * Check if the deque is empty
     * @return true if deque is empty, false otherwise
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Get number of items in the deque
     * @return Number of items
     */
    public int size() {
        return size;
    }

    /**
     * Add an item to front of the deque
     * @param item Item to be added through the front
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item to deque");
        }

        if (isEmpty()) {
            head = new Node(item);
            tail = head;
        } else {
            Node oldHead = head;
            head = new Node(item);
            head.next = oldHead;
            oldHead.previous = head;
        }
        size++;
    }

    /**
     * Add an item to end of the deque
     * @param item Item to be added through the back
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item to deque");
        }

        if (isEmpty()) {
            tail = new Node(item);
            head = tail;
        } else {
            Node oldTail = tail;
            tail = new Node(item);
            tail.previous = oldTail;
            oldTail.next = tail;
        }
        size++;
    }

    /**
     * Remove an item from front of the deque
     * @return The removed item
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty deque");
        }

        Item item = head.item;
        head = head.next;
        if (head != null) {
            // Nullify the pointer to old head
            head.previous = null;
        } else {
            // Clear all pointer access if deque is empty
            tail = null;
        }
        size--;
        return item;
    }

    /**
     * Remove an item from end of the deque
     * @return The removed item
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty deque");
        }

        Item item = tail.item;
        tail = tail.previous;
        if (tail != null) {
            // Nullify the pointer to old tail
            tail.next = null;
        } else {
            // Clear all pointer access if deque is empty
            head = null;
        }
        size--;

        return item;
    }

    /**
     * Get an iterator over items in order from front to end
     * @return Iterator instance
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }


    private class Node {
        private Item item;
        private Node previous;
        private Node next;

        private Node(Item item) {
            this.item = item;
        }
    }

    /**
     * Get representation of the deque as a String. For unit tests only
     * @return String representation of the deque
     */
    protected String getDequeAsString() {
        DequeIterator i = (DequeIterator) iterator();
        StringBuilder sb = new StringBuilder();
        while (i.hasNext()) {
            sb.append(i.next().toString());
            sb.append("->");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current;

        private DequeIterator() {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator has reached end of" +
                        " deque");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove through iterator " +
                    "is not supported");
        }
    }
}

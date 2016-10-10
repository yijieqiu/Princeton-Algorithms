import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic queue for which  the item removed is chosen uniformly at random
 *
 * @author Yijie Qiu
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int size;
    private int nexItemPointer;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        this.array = (Item[]) new Object[1];
        this.size = 0;
        this.nexItemPointer = 0;
    }

    /**
     * Check if the queue is empty
     * @return True if queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get number of items in the queue
     * @return Number of items
     */
    public int size() {
        return size;
    }

    /**
     * Add an item to the queue
     * @param item Item to be enqueued
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item to queue");
        }
        if (nexItemPointer == array.length) {
            expandArray();
        }

        array[nexItemPointer++] = item;
        size++;
    }


    /**
     * Remove and return a random item in the queue
     * @return Random item removed from the queue
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty queue");
        }

        Item item = null;
        int randomIndex = 0;
        while (item == null) {
            randomIndex = StdRandom.uniform(0, array.length);
            item = array[randomIndex];
        }
        size--;
        // Retrieved an item from array, set the array location to null
        array[randomIndex] = null;

        // Shrink array when needed
        if (size < array.length / 4) {
            shrinkArray();
        }

        return item;
    }

    /**
     * Return, but not remove a random item in the queue
     * @return Random item sampled from the queue
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot sample from empty queue");
        }
        Item item = null;
        while (item == null) {
            item = array[StdRandom.uniform(0, array.length)];
        }

        return item;
    }

    /**
     * Get an independent iterator over items in random order. If this method is
     * called multiple times, the iterators should operate independently from
     * each other
     * @return Iterator instance
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    /**
     * Expand the underlying array when needed
     */
    private void expandArray() {
        Item[] copy = (Item[]) new Object[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i];
        }
        array = copy;
    }

    /**
     * Shrink array when there are too many empty spaces due to item removal.
     * Also reset the index pointer for next item
     */
    private  void shrinkArray() {
        Item[] copy = (Item[]) new Object[array.length / 2];
        int updatedIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                copy[updatedIndex++] = array[i];
            }
        }
        array = copy;
        nexItemPointer = updatedIndex;
    }

    /**
     * Iterator that traverse the queue in random uniform order. Here we
     * assume that the iterator is sampling without replacement, each item
     * will only be returned once
     */
    private class RandomizedQueueIterator implements Iterator<Item> {

        private int itemCount;
        // In order for multiple iterators to operate independently from each
        // other, give each iterator its own copy of the queue
        private RandomizedQueue<Item> iteratorQueue;

        private RandomizedQueueIterator() {
            this.itemCount = 0;
            this.iteratorQueue = new RandomizedQueue<>();
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                   iteratorQueue.enqueue(array[i]);
                    itemCount++;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return itemCount > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator has reached end of" +
                        " queue");
            }
            itemCount--;
            return iteratorQueue.dequeue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove of item through " +
                    "iterator is not supported");
        }
    }

}

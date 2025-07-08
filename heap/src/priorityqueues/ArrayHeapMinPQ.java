package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    static final int START_INDEX = 1;
    private final Map<T, Integer> itemIndex;
    List<PriorityNode<T>> items;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        itemIndex = new HashMap<>();
        items.add(null);
    }

    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
        itemIndex.put(items.get(a).getItem(), a);
        itemIndex.put(items.get(b).getItem(), b);
    }

    private void swim(int idx) {
        while ((idx > 1) && ((items.get(idx).getPriority()) < items.get(idx / 2).getPriority())) {
            swap(idx, idx / 2);
            idx /= 2;
        }
    }

    private void sink(int idx) {
        while (2 * idx <= size()) {
            int j = 2 * idx;
            if ((j < size()) && (items.get(j).getPriority() > items.get(j + 1).getPriority())) {
                j++;
            }
            if (items.get(idx).getPriority() <= items.get(j).getPriority()) {
                break;
            }
            swap(idx, j);
            idx = j;
        }
    }

    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Item already exists in the priority queue.");
        }
        PriorityNode<T> newNode = new PriorityNode<>(item, priority);
        items.add(newNode);
        itemIndex.put(item, size());
        swim(size());
    }

    public boolean contains(T item) {
        return itemIndex.containsKey(item);
    }

    public T peekMin() {
        if (size() == 0) {
            throw new NoSuchElementException("Priority queue is empty.");
        }
        return items.get(START_INDEX).getItem();
    }

    public T removeMin() {
        if (size() == 0) {
            throw new NoSuchElementException("Priority queue is empty.");
        }
        T minItem = items.get(START_INDEX).getItem();
        swap(START_INDEX, size());
        items.remove(size());
        itemIndex.remove(minItem);

        if (size() > 0) {
            sink(START_INDEX);
        }
        return minItem;
    }

    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("Item does not exist in the priority queue.");
        }
        int idx = itemIndex.get(item);
        double oldPriority = items.get(idx).getPriority();
        items.get(idx).setPriority(priority);
        if (oldPriority < priority) {
            sink(idx);
        }
        else if (oldPriority > priority) {
            swim(idx);
        }
    }

    public int size() {
        return items.size()-1;
    }
}

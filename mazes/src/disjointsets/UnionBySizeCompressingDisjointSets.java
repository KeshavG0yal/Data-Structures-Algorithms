package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;

    private Map<T, Integer> itemToIndex;

    private int size;

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        itemToIndex = new HashMap<>();
        size = 0;
    }

    @Override
    public void makeSet(T item) {
        if (itemToIndex.containsKey(item)) {
            throw new IllegalArgumentException("Item already exists in a set");
        }
        itemToIndex.put(item, size);
        pointers.add(-1);
        size++;
    }

    @Override
    public int findSet(T item) {
        if (!itemToIndex.containsKey(item)) {
            throw new IllegalArgumentException("Item not found");
        }
        int index = itemToIndex.get(item);
        return find(index);
    }

    private int find(int index) {
        if (pointers.get(index) < 0) {
            return index;
        }
        int root = find(pointers.get(index));
        pointers.set(index, root);

        return root;
    }

    @Override
    public boolean union(T item1, T item2) {
        int root1 = findSet(item1);
        int root2 = findSet(item2);

        if (root1 == root2) {
            return false;
        }
        int size1 = pointers.get(root1);
        int size2 = pointers.get(root2);
        if (size1 <= size2) {
            pointers.set(root1, size1 + size2);
            pointers.set(root2, root1);
        } else {
            pointers.set(root2, size1 + size2);
            pointers.set(root1, root2);
        }
        return true;
    }
}

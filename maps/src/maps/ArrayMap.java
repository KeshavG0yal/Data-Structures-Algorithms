package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    SimpleEntry<K, V>[] entries;

    private int size;

    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be > 0");
        }
        this.entries = this.createArrayOfEntries(initialCapacity);
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    public V get(Object key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
                return entries[i].getValue();
            }
        }
        return null;
    }

    public V put(K key, V value) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
                V oldValue = entries[i].getValue();
                entries[i].setValue(value);
                return oldValue;
            }
        }
        if (size == entries.length) {
            resize();
        }
        entries[size] = new SimpleEntry<>(key, value);
        size++;
        return null;
    }

    public V remove(Object key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
                V removedValue = entries[i].getValue();
                entries[i] = entries[size - 1];
                entries[size - 1] = null;
                size--;
                return removedValue;
            }
        }
        return null;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            entries[i] = null;
        }
        size = 0;
    }

    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public Iterator<Map.Entry<K, V>> iterator() {
        return new ArrayMapIterator<>(this.entries, size);
    }

    private void resize() {
        SimpleEntry<K, V>[] newEntries = createArrayOfEntries(entries.length * 2);
        System.arraycopy(entries, 0, newEntries, 0, size);
        entries = newEntries;
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        private final int size;
        private int currentIndex;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            this.size = size;
            this.currentIndex = 0;
        }

        public boolean hasNext() {
            return currentIndex < size;
        }

        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements to iterate over.");
            }
            return entries[currentIndex++];
        }
    }
}

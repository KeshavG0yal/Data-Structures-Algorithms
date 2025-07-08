package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.75;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 16;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 4;

    AbstractIterableMap<K, V>[] chains;

    private int size;
    private final double resizingLoadFactorThreshold;
    private final int chainInitialCapacity;


    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        if (resizingLoadFactorThreshold <= 0 || initialChainCount <= 0 || chainInitialCapacity <= 0) {
            throw new IllegalArgumentException("Parameters must be greater than 0.");
        }
        this.resizingLoadFactorThreshold = resizingLoadFactorThreshold;
        this.chainInitialCapacity = chainInitialCapacity;
        this.chains = createArrayOfChains(initialChainCount);
        for (int i = 0; i < initialChainCount; i++) {
            this.chains[i] = createChain(chainInitialCapacity);
        }
        this.size = 0;
    }


    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }


    public V get(Object key) {
        int index = Math.abs(key.hashCode() % chains.length);
        return chains[index].get(key);
    }


    public V put(K key, V value) {
        int index = Math.abs(key.hashCode() % chains.length);
        V oldValue = chains[index].put(key, value);
        if (oldValue == null) {
            size++;
            if ((double) size / chains.length > resizingLoadFactorThreshold) {
                resize();
            }
        }
        return oldValue;
    }


    public V remove(Object key) {
        int index = Math.abs(key.hashCode() % chains.length);
        V removedValue = chains[index].remove(key);
        if (removedValue != null) {
            size--;
        }
        return removedValue;
    }

    public void clear() {
        for (AbstractIterableMap<K, V> chain : chains) {
            chain.clear();
        }
        size = 0;
    }

    public boolean containsKey(Object key) {
        int index = Math.abs(key.hashCode() % chains.length);
        return chains[index].containsKey(key);
    }

    public int size() {
        return size;
    }

    public Iterator<Map.Entry<K, V>> iterator() {
        return new ChainedHashMapIterator<>(this.chains);
    }

    private void resize() {
        int newChainCount = chains.length * 2;
        AbstractIterableMap<K, V>[] newChains = createArrayOfChains(newChainCount);
        for (int i = 0; i < newChainCount; i++) {
            newChains[i] = createChain(chainInitialCapacity);
        }

        for (AbstractIterableMap<K, V> chain : chains) {
            for (Map.Entry<K, V> entry : chain) {
                int newIndex = Math.abs(entry.getKey().hashCode() % newChainCount);
                newChains[newIndex].put(entry.getKey(), entry.getValue());
            }
        }

        this.chains = newChains;
    }


    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final AbstractIterableMap<K, V>[] chains;
        private int currentChainIndex;
        private Iterator<Map.Entry<K, V>> currentChainIterator;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            this.currentChainIndex = 0;
            this.currentChainIterator = chains[0].iterator();
        }

        public boolean hasNext() {
            while (currentChainIndex < chains.length) {
                if (currentChainIterator.hasNext()) {
                    return true;
                }
                currentChainIndex++;
                if (currentChainIndex < chains.length) {
                    currentChainIterator = chains[currentChainIndex].iterator();
                }
            }
            return false;
        }

        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements");
            }
            return currentChainIterator.next();
        }
    }
}

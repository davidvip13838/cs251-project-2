package CommonUtils;

import CommonUtils.Interfaces.BetterHashTableInterface;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class BetterHashTable<K, V> implements BetterHashTableInterface<K, V> {
    private final int INIT_CAPACITY = (1 << 4) + 3; // 16 + 3 = 19
    private final double LOAD_FACTOR = 0.75;
    private final int INCREASE_FACTOR = 2;
    private final int CAPACITY_INCREMENT = 1 << 5; // 32

    private Node<K, V>[] table;
    private int size;
    private int capacity;

    private static class Node<K, V> {
        final K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" + "key=" + key + ", value=" + value + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }
    }

    private final Node<K, V> DELETED = new Node<>(null, null);

    @SuppressWarnings("unchecked")
    public BetterHashTable() {
        this.capacity = INIT_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public BetterHashTable(int initialCapacity) throws IllegalArgumentException {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        this.capacity = Math.max(initialCapacity, INIT_CAPACITY);
        this.table = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
    }

    private int usefulHash(K thing) {
        return Math.abs(Objects.hash(thing) % capacity);
    }

    @Override
    public void insert(K key, V value) {
        if (key == null) return;

        if (size >= capacity * LOAD_FACTOR) {
            resize(capacity * INCREASE_FACTOR);
        }

        int hash = usefulHash(key);
        int i = 0;

        while (table[hash] != null && table[hash] != DELETED) {
            if (Objects.equals(table[hash].key, key)) {
                table[hash].value = value;
                return;
            }
            hash = (hash + (++i * i)) % capacity; // quadratic probing
        }

        table[hash] = new Node<>(key, value);
        size++;
    }

    @Override
    public void remove(K key) {
        if (key == null) return;

        int hash = usefulHash(key);
        int i = 0;

        while (table[hash] != null) {
            if (Objects.equals(table[hash].key, key)) {
                table[hash] = DELETED;
                size--;
                return;
            }
            hash = (hash + (++i * i)) % capacity;
        }
    }

    @Override
    public V get(K key) {
        if (key == null) return null;

        int hash = usefulHash(key);
        int i = 0;

        while (table[hash] != null) {
            if (Objects.equals(table[hash].key, key)) {
                return table[hash].value;
            }
            hash = (hash + (++i * i)) % capacity;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        this.table = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void visualize(Graphics g) {

    }

    private void resize(int newCapacity) {
        if (newCapacity > Integer.MAX_VALUE - CAPACITY_INCREMENT) {
            newCapacity = capacity + CAPACITY_INCREMENT;
        }

        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        capacity = newCapacity;
        size = 0;

        for (Node<K, V> node : oldTable) {
            if (node != null && node != DELETED) {
                insert(node.key, node.value);
            }
        }
    }
}

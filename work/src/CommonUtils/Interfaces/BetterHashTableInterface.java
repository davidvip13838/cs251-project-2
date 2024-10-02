package CommonUtils.Interfaces;


/**
 * Interface for our new BetterHashTable object.
 * <p>
 * Reminder from Head of Software (HS): It is recommended that you use quadratic probing to handle
 *   collisions as we might have both clustering and large data sets.
 * <p>
 * <b>251 students: You are explicitly forbidden from using java.util.Hashtable (including any subclass
 *   like HashMap) and any other java.util.* library EXCEPT java.util.Arrays and java.util.Objects.
 *   Write your own implementation of a hash table.</b>
 *
 * @param <K> Type of key the hash table is holding
 * @param <V> Type of value the hash table is holding
 */
public interface BetterHashTableInterface<K, V> {
    /**
     * Places the item in the hash table. Passing key=null will not change the state of the table.
     * Passing a key already in the table will modify the original entry in the table.
     *
     * @param key key to associate with the value
     * @param value item to store in the hash table
     */
    void insert(K key, V value);

    /**
     * Removes the key, value pair associated with the given key
     * @param key key/value to remove
     */
    void remove(K key);

    /**
     * Retrieves a value based on the given key
     * @param key key to search by
     * @return value associated with the key, or <code>null</code> if it does not exist
     */
    V get(K key);

    /**
     * Returns <code>true</code> if this hash table contains the given key.
     * @param key key to check for
     * @return true iff the hash table contains a mapping for the specified key,
     *          as ultimately determined by the equals method
     */
    boolean containsKey(K key);

    /**
     * Empties the hash table.
     */
    void clear();

    /**
     * Returns the number of items in the hash table
     * @return integer representing the number of elements in the hash table
     */
    int size();

    /**
     * DO NOT MODIFY NOR IMPLEMENT THIS FUNCTION
     * @param g graphics object to draw on
     */
    void draw(java.awt.Graphics g);

    /**
     * DO NOT MODIFY NOR IMPLEMENT THIS FUNCTION
     * @param g graphics object to draw on
     */
    void visualize(java.awt.Graphics g);
}

package CommonUtilsTest;

import CommonUtils.BetterHashTable;
import CommonUtilsTest.UsefulObjects.Blob;
import CommonUtilsTest.factories.IntTestFactory;
import CommonUtilsTest.factories.NullAndNotFoundTestFactory;
import CommonUtilsTest.factories.SizeEmptyBasicAddRemoveBackTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author Ethan Dickey
 * Tests the BetterHashTable class.  Extensively.  According to the interface specifications.
 */
public class BetterHashTableTest {
    /**
     * Tests very basic use.  Failing this should fail all other tests
     */
    @Test
    void sanityCheck(){
        BetterHashTable<Integer, Integer> table = new BetterHashTable<>(4);
        table.insert(1, 2); table.insert(2, 3); table.insert(3, 4); table.insert(4, 5);

        assertTrue(table.containsKey(1));
        assertTrue(table.containsKey(2));
        assertTrue(table.containsKey(3));
        assertTrue(table.containsKey(4));

        assertEquals(2, table.get(1));
        assertEquals(3, table.get(2));
        assertEquals(4, table.get(3));
        assertEquals(5, table.get(4));

        assertEquals(4, table.size());

        table.clear();

        assertEquals(0, table.size());
    }

    /**
     * Few tests to enforce behavior surrounding null (i.e. not throwing, returning null, etc)
     */
    @Nested
    class NullAndNotFoundTests extends NullAndNotFoundTestFactory {
        BetterHashTable<Integer, Integer> table = null;
        private final Integer NUM_EL_TO_FILL = 5000000;

        /**
         * Tests that containskey returns false for null keys.
         * Parent class does not check this
         */
        @Test
        void testContainsNullKey(){
            fillContainer();
            assertFalse(table.containsKey(null));
            verifyContainerDidNotChange();
        }

        /**
         * Tests that containskey returns false for keys that don't exist.
         * Parent class does not check this
         */
        @Test
        void testContainsBadKey(){
            fillContainer();
            for(var i : getKeysThatDoNotExistInFullContainer()){
                assertFalse(table.containsKey(i));
            }
            verifyContainerDidNotChange();
        }

        /*Functions for container setup and validation*/
        /**
         * Reinitializes the container to be empty.  Useful in NullInsert test.
         */
        @Override
        protected void makeNewEmptyContainer() { table = new BetterHashTable<>(); }

        /**
         * Children must provide functionality to fill their container before each test, as well as a way to
         * verify that nothing changed.  Does not assume container is initialized.
         *
         * @implNote this function and {@link #verifyContainerDidNotChange()} must agree on what
         * is in the container.
         */
        @Override
        protected void fillContainer() {
            table = new BetterHashTable<>();
            for(int i=0; i<NUM_EL_TO_FILL; i++){
                table.insert(i, i+1);
            }
        }

        /**
         * Returns a key that does not exist in the full container, after running {@link #fillContainer()}
         *
         * @return key that does not exist in full container
         */
        @Override
        protected List<Integer> getKeysThatDoNotExistInFullContainer() {
            List<Integer> orig = new ArrayList<>(Arrays.asList(-1, 10000000, NUM_EL_TO_FILL, NUM_EL_TO_FILL+265,
                                NUM_EL_TO_FILL+1024, NUM_EL_TO_FILL+2048,
                                NUM_EL_TO_FILL+1000000, NUM_EL_TO_FILL+65535));
            int sizeThing = orig.size()-1;
            for(int i=1; i<sizeThing; i++){
                orig.add(orig.get(i)*-1);
            }
            //just in case and because I'm too lazy to check when I change NUM_EL_TO_FILL
            return orig.stream().filter(i -> i < 0 || i >= NUM_EL_TO_FILL).collect(Collectors.toList());
        }

        /**
         * Children must provide functionality to fill their container before each test, as well as a way to
         * verify that nothing changed.
         *
         * @implNote {@link #fillContainer()} and this function must agree on what is in the container.
         */
        @Override
        protected void verifyContainerDidNotChange() {
            //size
            assertEquals(NUM_EL_TO_FILL, table.size());

            //contains key
            for(int i=0; i<NUM_EL_TO_FILL; i++){
                assertTrue(table.containsKey(i));
            }

            //get
            for(int i=0; i<NUM_EL_TO_FILL; i++){
                assertEquals(i+1, table.get(i));
            }

            //remove
            int numLeft = NUM_EL_TO_FILL;
            table.remove(0); numLeft--;//min key in the table
            assertEquals(numLeft, table.size());
            table.remove(NUM_EL_TO_FILL-1); numLeft--;//max key in the table
            assertEquals(numLeft, table.size());

            //awkward remove:: every 3rd/5th/7th element
            for(int i=1, j=0; i<NUM_EL_TO_FILL-1; i += 2*j+3, j = (j+1)%3){
                table.remove(i); numLeft--;
                assertEquals(numLeft, table.size());
            }

            //clear
            table.clear();
            assertEquals(0, table.size());
            for(int i=0; i<NUM_EL_TO_FILL; i++){
                assertNull(table.get(i));
            }
        }


        /*Functions for manipulating the container*/
        /**
         * Factory methods for calling the appropriate add function on the container you are testing.
         *
         * @param key   key to add with value
         * @param value value to add with key
         * @throws Exception if something goes wrong
         */
        @Override
        protected void add(Integer key, Integer value) throws Exception { table.insert(key, value); }

        /**
         * Factory method for calling the appropriate get(key) function on the container you are testing.
         *
         * @param key key to retrieve the value for
         * @return value associated with the key
         * @throws Exception if something goes wrong
         */
        @Override
        protected Integer get(Integer key) throws Exception { return table.get(key); }

        /**
         * Factory method for calling the appropriate parameterized remove function on the container you are testing.
         *
         * @param key key to remove from container
         * @throws Exception if something goes wrong
         */
        @Override
        protected void remove(Integer key) throws Exception { table.remove(key); }

        /**
         * Factory methods for calling the appropriate size() function on the container you are testing.
         */
        @Override
        protected long getSize() { return table.size(); }
    }

    /**
     * Verifies that it can hold integers properly
     */
    @Nested
    class GetContainsTests extends IntTestFactory {
        BetterHashTable<Integer, Integer> table = new BetterHashTable<>();
        /**
         * Factory method for calling the appropriate function you want to test for signed ints validity
         *
         * @param num signed int to test
         * @return the result of a getField on the respective object
         * @throws Exception if something goes wrong
         */
        @Override
        protected int setGetField(int num) throws Exception {
            table.insert(num, num);
            return table.get(num);
        }
    }

    /**
     * Does basic add remove with large amount of elements exhaustive testing
     */
    @Nested
    class ExtensiveUsageTests extends SizeEmptyBasicAddRemoveBackTestFactory {
        BetterHashTable<Long, Long> table = new BetterHashTable<>();

        /**
         * Factory methods for calling the appropriate add function on the container you are testing.  Requires persistence.
         *
         * @param o object to add
         * @throws Exception if something goes wrong
         */
        @Override
        protected void add(long o) throws Exception { table.insert(o, o); }

        /**
         * Factory methods for calling the appropriate parameterized remove function on the container you are
         * testing.  Requires persistence.
         * If you don't have one, leave it blank and override getTestParamterizedRemove to return false.
         *
         * @param o object to add
         * @throws Exception if something goes wrong
         */
        @Override
        protected void removeParameterized(long o) throws Exception { table.remove(o); }

        /**
         * Factory methods for calling the appropriate remove back function on the container you are
         *   testing.  Requires persistence.
         * If you don't have one, leave it blank and override getTestRemoveBack to return false.
         *
         * @throws Exception if something goes wrong
         */
        @Override
        protected void removeBack() throws Exception { /* nothing here on purpose */ }
        /**
         * Gives the subclass the option of whether to run remove back tests (if the type
         * being tested can't perform that operation, for example)
         *
         * @return whether to run remove back tests
         */
        @Override
        protected boolean getTestRemoveBack() { return false; }

        /**
         * Factory methods for calling the appropriate size() function on the container you are testing.  Requires persistence.
         */
        @Override
        protected long getSize() { return table.size(); }

        /**
         * Factory methods for calling the appropriate isEmpty back function on the container you are testing.  Requires persistence.
         */
        @Override
        protected boolean isEmpty() { return table.size() == 0; }
    }


    /**
     * Tests different kinds of probing mechanisms and attempts to force clustering in order to break the
     *   implementation.
     * Good cache performance (i.e. preventing double hashing) is tested in {@link  ExtensiveUsageTests},
     *   as those test cases insert many elements.
     */
    //THIS ANNOTATION IS IMPORTANT IN ORDER TO HAVE A NON-STATIC METHOD SOURCE -- because nested classes
    //  can't be static so neither can their methods.
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ProbingTests {
        
        private final long MAX_CLUSTERING_RUN_TIME_MS = 700;

        /**
         * Returns a list of parameters to run for the clustering test
         * @return a set of parameters for the clustering test
         */
        private Stream<Arguments> getPrePostfix(){
            return Stream.of(
                    arguments("", ""),
                    arguments("abcdefghijklmnopqrstuvwxyz0123456789", ""),
                    arguments("", "abcdefghijklmnopqrstuvwxyz0123456789"),
                    arguments("abcdefghijklmnopqrstuvwxyz0123456789", "abcdefghijklmnopqrstuvwxyz0123456789"),
                    arguments("0123456789", "0123456789"),
                    arguments("", "0123456789"),
                    arguments("0123456789", ""),
                    arguments("abcdefghijklmno", "pqrstuvwxyz0123456789")
            );
        }

        /**
         * Tries to force slow table behavior with clustering via string iteration.
         *   Only bases time on actual insertion time.  Strings are all pre-generated (before timer starts).
         * @param prefix clustering prefix
         * @param postfix clustering postfix
         */
        @DisplayName("Clustering test, MAX INSERT TIME=" + MAX_CLUSTERING_RUN_TIME_MS + "ms")
        @ParameterizedTest(name = "Prefix: \"{0}\", Postfix: \"{1}\"")
        @MethodSource("getPrePostfix")
        void testClustering(String prefix, String postfix){
            BetterHashTable<String, Integer> table = new BetterHashTable<>();
            //max number of times to add stuff to the end in an attempt to force clustering
            final int MAX_REP = 1000;

            //holds the pregenerated strings to reduce the amount of time spent in the actual insertion
            Set<String> stringsToInsert = new HashSet<>();
            stringsToInsert.add(prefix + postfix);

            //extra stuff added before and after
            String both = prefix + postfix;
            for(int i='0'; i<'z'; i++){//covers numbers, capitals, lowercase
                stringsToInsert.add((char)i + both);
                for(int j=0; j<MAX_REP; j++){
                    stringsToInsert.add((char)i + both + j);
                }
            }
            //extra stuff added in the middle and after
            for(int i='0'; i<'z'; i++){
                stringsToInsert.add(prefix + (char)i + postfix);
                for(int j=0; j<MAX_REP; j++){
                    stringsToInsert.add(prefix + (char)i + postfix + j);
                }
            }
            //extra stuff added after
            for(int i='0'; i<'z'; i++){
                stringsToInsert.add(both + (char)i);
                for(int j=0; j<MAX_REP; j++){
                    stringsToInsert.add(both + (char)i + j);
                }
            }

            //test the actual insert function, the student's code, and time it
            long start = System.currentTimeMillis();
            int i=0;
            for(var s : stringsToInsert){
                table.insert(s, i);
                i++;
            }
            long finish = System.currentTimeMillis();

            assertEquals(stringsToInsert.size(), table.size());
            assertTableContains(table, stringsToInsert);

            //fail if it took too long
            if(MAX_CLUSTERING_RUN_TIME_MS < finish-start){
                fail("Ran too long, likely failing from clustering or bad implementation. " +
                        "Max: "+ MAX_CLUSTERING_RUN_TIME_MS + ", actual: " + (finish-start));
            }
        }

        /**
         * fast complete table check
         * @param table table to check
         * @param stringsToInsert what table should contain
         */
        private void assertTableContains(BetterHashTable<String, Integer> table, Set<String> stringsToInsert) {
            int originalSize = table.size();
            int i=0;
            for(var s : stringsToInsert){
                assertTrue(table.containsKey(s));
                assertEquals(i, table.get(s));
                table.remove(s);
                assertEquals(--originalSize, table.size());
                i++;
            }
        }
    }


    //actual hash table tests::
    //  equivalence of objects based on equals (don't test with sword object because it's to be removed for testing)
    /**
     * Tests that the hash table uses equivalence based on equals function and not object equivalence.
     * Relies on the fact that they use hashcode for hashing into the table, not some arbitrary method.
     * This should not be a problem unless their implementation is incorrect.
     */
    @Nested
    class UsingEqualsForEquivalence {

        @DisplayName("If someone fails this test, make sure that they actually don't meet the specifications")
        @Test
        void basicEquivalenceTest(){
            Blob b = new Blob("hi0", "asdf", 0, 1),
                 c = new Blob("hi0", ";lkj", 0, 10000);
            assertEquals(b, c);

            BetterHashTable<Blob, Integer> table = new BetterHashTable<>();
            table.insert(b, 0);
            assertTrue(table.containsKey(c));
        }
    }

    /**
     * Tests that the table has an efficient contains() and decent caching via the following method:
     *   Run a bunch (>>MAX) of contains() on random numbers between 1 and MAX, after inserting all of those numbers.
     *   This will ensure an efficient contains() method (i.e. they actually hash and check the results of the hash)
     */
    @Test
    void testEfficientHashTable() {

        final int MAX_CONTAINS_TIME_MS = 3000;
        final int MAX_NUMS = 1000000, MAX_QUERIES = 10*MAX_NUMS;

        //insert all values into table
        BetterHashTable<Integer, Integer> table = new BetterHashTable<>(MAX_NUMS);
        for(int i=0; i<MAX_NUMS; i++){
            table.insert(i, i);
        }

        Random rand = new Random(0);//0 SEED TO ENSURE TESTING FAIRNESS!

        //timing contains -- what we really want out of hash tables
        long start = System.currentTimeMillis();
        for(int i=0; i<MAX_QUERIES; i++){
            assertTrue(table.containsKey(rand.nextInt(MAX_NUMS)));
        }
        long finish = System.currentTimeMillis();

        //fail if they took too long
        if(MAX_CONTAINS_TIME_MS < finish-start){
            fail("Ran too long, likely failing from bad caching or bad implementation. " +
                    "Max: "+ MAX_CONTAINS_TIME_MS + ", actual: " + (finish-start));
        }
    }

    /**
     * Tests that when inserting an element with the same key as one already in the table,
     *   it just updates the value at that key.
     */
    @Test
    void testSameKeyUpdated(){
        BetterHashTable<Integer, String> table = new BetterHashTable<>();
        table.insert(3, "3");
        assertEquals(table.get(3), "3");
        assertEquals(table.size(), 1);
        table.insert(3, "4");
        assertEquals(table.get(3), "4");
        assertEquals(table.size(), 1);
    }
}

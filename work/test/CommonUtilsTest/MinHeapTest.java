package CommonUtilsTest;

import CommonUtils.MinHeap;
import CommonUtilsTest.UsefulObjects.ComplexObject;
import CommonUtilsTest.UsefulObjects.NewInt;
import CommonUtilsTest.factories.IntTestFactory;
import CommonUtilsTest.factories.SizeEmptyBasicAddRemoveBackTestFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ethan Dickey
 * Tests MinHeap
 */
public class MinHeapTest {
    /**
     * Tests very basic use.  Failing this should fail all other tests
     */
    @Test
    void sanityCheck(){
        MinHeap<Integer> heap = new MinHeap<>();
        heap.add(0);
        heap.add(3);
        heap.add(200000000);
        assertEquals(0, heap.removeMin());
        assertEquals(3, heap.removeMin());
        assertEquals(200000000, heap.removeMin());
        assertEquals(0, heap.size());
    }

    /**
     * Test null pointer exception required on add
     */
    @Test
    void NullPointerAddTest(){
        MinHeap<Integer> heap = new MinHeap<>();
        assertThrows(NullPointerException.class, () -> heap.add(null));
    }


    /**
     * Tests clear and that removeMin and peekMin return null when empty
     * @param num number of elements to test with
     */
    @ParameterizedTest(name = "Num elements = {0}")
    @ValueSource(ints = {0, 1, 65535, 1000000})
    void testClearAndNullReturns(int num){
        MinHeap<Integer> heap = new MinHeap<>();
        assertNull(heap.removeMin());
        assertNull(heap.peekMin());
        for(int i=0; i<num; i++){
            heap.add(i);
            assertEquals(i+1, heap.size());
        }
        heap.clear();
        assertEquals(0, heap.size());
        assertNull(heap.removeMin());
        assertNull(heap.peekMin());
    }

    /**
     * Tests basic use cases with integers
     */
    @Nested
    class IntTests {
        /**
         * Tests basic set and get features with integers using a factory
         */
        @Nested
        class StandardIntTests extends IntTestFactory {
            MinHeap<Integer> heap = new MinHeap<>();
            /**
             * Factory method for calling the appropriate function you want to test for signed ints validity
             *
             * @param num signed int to test
             * @return the result of a getField on the respective object
             * @throws Exception if something goes wrong
             */
            @Override
            protected int setGetField(int num) throws Exception {
                heap.add(num);
                return heap.removeMin();
            }
        }

        /**
         * Does basic add remove with large amount of elements exhaustive testing
         */
        @Nested
        class StandardAddRemoveSizeIntTests extends SizeEmptyBasicAddRemoveBackTestFactory {
            MinHeap<Long> heap = new MinHeap<>();

            /**
             * Factory methods for calling the appropriate add function on the container you are testing.  Requires persistence.
             *
             * @param o object to add
             * @throws Exception if something goes wrong
             */
            @Override
            protected void add(long o) throws Exception { heap.add(o); }

            /**
             * Factory methods for calling the appropriate parameterized remove function on the container you are
             * testing.  Requires persistence.
             * If you don't have one, leave it blank and override getTestParamterizedRemove to return false.
             *
             * @param o object to add
             * @throws Exception if something goes wrong
             */
            @Override
            protected void removeParameterized(long o) throws Exception { /* nothing here on purpose */ }

            /**
             * Factory methods for calling the appropriate remove back function on the container you are testing.  Requires persistence.
             * @throws Exception if something goes wrong
             */
            @Override
            protected void removeBack() throws Exception { heap.removeMin(); }

            /**
             * Factory methods for calling the appropriate size() function on the container you are testing.  Requires persistence.
             */
            @Override
            protected long getSize() { return heap.size(); }

            /**
             * Factory methods for calling the appropriate isEmpty back function on the container you are testing.  Requires persistence.
             */
            @Override
            protected boolean isEmpty() { return heap.size() == 0; }

            /**
             * Gives the subclass the option of whether to run parameterized remove tests (if the type
             * being tested can't perform that operation, for example)
             *
             * @return whether to run parameterized remove tests
             */
            @Override
            protected boolean getTestParameterizedRemove() { return false; }
        }

        /**
         * Tests basic use with the normal comparator
         */
        @Nested
        class NormalOrderInts {
            //test the actual order
            @Test
            void correctOrder() {
                MinHeap<Integer> heap = new MinHeap<>();
                final int MAX = 10000;
                for(int i=0; i<MAX; i++){
                    heap.add(i);
                }
                for(int i=0; i<MAX; i++){
                    assertEquals(i, heap.peekMin());
                    assertEquals(i, heap.removeMin());
                }
            }

            /**
             * Inserts in reverse order with standard comparator
             */
            @Test
            void reverseInsert(){
                MinHeap<Integer> heap = new MinHeap<>();
                final int MAX = 10000;
                for(int i=MAX-1; i>=0; i--){
                    heap.add(i);
                }
                for(int i=0; i<MAX; i++){
                    assertEquals(i, heap.peekMin());
                    assertEquals(i, heap.removeMin());
                }
            }
        }

        /**
         * Tests standard use with reverse comparator
         */
        @Nested
        class ReverseOrderInts {
            /**
             * Tests basic use with a reverse comparator
             */
            @Test
            void reverseOrder() {
                MinHeap<NewInt> heap = new MinHeap<>();
                final int MAX = 10000;
                for(int i=0; i<MAX; i++){
                    heap.add(new NewInt(i));
                }
                for(int i=MAX-1; i>=0; i--){
                    assertEquals(i, heap.peekMin().get());
                    assertEquals(i, heap.removeMin().get());
                }
            }
        }
    }

    //complex sorting tests designed to trick them if they use the minus sign or any other form of comparison
    //  besides compareTo (check after every add what the peekMin is, then after each removeMin)
    /**
     * Tests comparators with multiple fields to compare on
     */
    @Nested
    class ComplexObjectTests{
        /**
         * tests that the heap produces a series of things in the order of things
         * @param heap heap to test
         * @param things order of objects to compare
         */
        void sequenceEqual(MinHeap<ComplexObject> heap, ArrayList<ComplexObject> things){
            for(var i : things){
                assertEquals(0, heap.peekMin().compareTo(i));
                assertEquals(0, heap.removeMin().compareTo(i));
            }
        }

        /**
         * Basic use with complex object
         */
        @Test
        void basicUseTest(){
            MinHeap<ComplexObject> heap = new MinHeap<>();
            ComplexObject a0 = new ComplexObject("abcd", 0),
                          a10 = new ComplexObject("abcd", 1),
                          a01 = new ComplexObject("abcd", 0),
                          b0 = new ComplexObject("dcba", 0),
                          b1 = new ComplexObject("dcba", 1),
                          c = new ComplexObject();
            heap.add(b1); heap.add(b0);
            heap.add(c);
            heap.add(a10); heap.add(a01); heap.add(a0);
            assertEquals(6, heap.size());
            sequenceEqual(heap, new ArrayList<>(Arrays.asList(a0, a01, a10, b0, b1, c)));
        }
    }
}

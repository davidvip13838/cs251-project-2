package Drones.Interfaces;

import Items.Types.SwordType;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Interface for the BETTER sword cleaning manager.  The implementing class should follow the specifications
 *   listed in the project description ("Story 3").  You may NOT use any Stack/Queue/priority queue/hash table
 *   objects or any similar objects or implementations from the Java standard libraries.
 *
 * <bold>251 students: you may use any of the data structures you have previously created, but may not use
 *   any Java library for stacks, queues, min heaps/priority queues, or hash tables (or any similar classes).</bold>
 */
public interface BetterCleanSwordManagerInterface {
    /**
     * Class used to store and retrieve sword cleaning times
     * @param <E> type of sword we are dealing with
     */
    class DetailedCleanSwordTime<E extends SwordType> {
        long timeFilled, timeToFulfill;
        E swordFulfilledWith;

        //constructor with aptly-named fields
        public DetailedCleanSwordTime(long timeFilled0, long timeToFulfill0, E swordFulfilledWith0){
            timeFilled = timeFilled0;
            timeToFulfill = timeToFulfill0;
            swordFulfilledWith = swordFulfilledWith0;
        }

        //default Intellij-generated equals function
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetailedCleanSwordTime<?> that = (DetailedCleanSwordTime<?>) o;
            return timeFilled == that.timeFilled && timeToFulfill == that.timeToFulfill && Objects.equals(swordFulfilledWith, that.swordFulfilledWith);
        }

        /**
         * To string method for debugging
         * @return string version of object
         */
        @Override
        public String toString() {
            return "DetailedCleanSwordTimes{" +
                    "timeFilled=" + timeFilled +
                    ", timeToFulfill=" + timeToFulfill +
                    ", swordFulfilledWith=" + swordFulfilledWith.toString() +
                    '}';
        }
    }

    /**
     * Gets the cleaning times per the specifications.
     *
     * @param filename file to read input from
     * @return the list of times requests were filled, times it took to fill them,
     *          and sword returned, as per the specifications
     */
    ArrayList<DetailedCleanSwordTime<SwordType>> getCleaningTimes(String filename);
}

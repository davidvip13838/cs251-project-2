package Drones;

import Drones.Interfaces.BetterCleanSwordManagerInterface;
import Items.Types.SwordType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages everything regarding the cleaning of swords in our game.  Will be integrated with
 *   the other drone classes.  UPDATED VERSION -- handles sword management with the new rules.
 *   Old version-CleanSwordManager-is deprecated.
 *
 * <bold>251 students: you may use any of the data structures you have previously created, but may not use
 *   any Java library for stacks, queues, min heaps/priority queues, or hash tables (or any similar classes).</bold>
 */
public class BetterCleanSwordManager implements BetterCleanSwordManagerInterface {
    /**
     * Gets the cleaning times per the specifications.
     *
     * @param filename file to read input from
     * @return the list of times requests were filled, times it took to fill them,
     * and sword returned, as per the specifications
     */
    @Override
    public ArrayList<DetailedCleanSwordTime<SwordType>> getCleaningTimes(String filename) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filename));
            //todo
        } catch (IOException e) {
            //This should never happen... uh oh o.o
            System.err.println("ATTENTION TAs: Couldn't find test file: \"" + filename + "\":: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }
}

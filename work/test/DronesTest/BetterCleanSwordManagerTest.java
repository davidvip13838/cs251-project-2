package DronesTest;

import Drones.BetterCleanSwordManager;
import Drones.Interfaces.BetterCleanSwordManagerInterface.DetailedCleanSwordTime;
import Items.Types.SwordType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author Ethan Dickey
 * Tests BetterCleanSwordManager
 */
public class BetterCleanSwordManagerTest {
    final static String prefix = System.getenv("ASNLIB") + "/test/DronesTest/betterCleanSwordFiles/";
    final static File[] folderPaths = {new File(prefix + "sample"),
            new File(prefix + "manual"),
//            new File(prefix + "generated")
    };
    final static String inputSuffix = "in", ansSuffix = "out";

    /**
     * All done in one instance of the manager
     */
    BetterCleanSwordManager manager = new BetterCleanSwordManager();

    /**
     * copy of the answer Sword class, because I use it in testing
     */
    private static class TempAnsKeySword extends SwordType {
        /**
         * Constructs the Sword object by referencing the parent class.  Fields are as named.
         */
        public TempAnsKeySword(int totalHealth, int healthLeft, int timeToClean, int length, int DPS, int attackSpeed,
                     String name, String description, String comments, String style) {
            super(totalHealth, healthLeft, timeToClean, length, DPS, attackSpeed, name, description, comments, style);
        }

        //uniquely identified by a combination of: totalHealth, DPS, attackSpeed, style

        /**
         * Returns the hash code of an object based on the fields that uniquely identify a sword
         * @param totalHealth total health
         * @param DPS damage per second
         * @param attackSpeed attack speed
         * @param style style of sword
         * @return hashed value
         */
        public static int getHashValue(int totalHealth, int DPS, int attackSpeed, String style){
            return Objects.hash(totalHealth, DPS, attackSpeed, style);
        }//pass in the "few" fields required to make the hash

        /**
         * Returns the hash code of this object, for internal use only
         */
        @Override
        public int hashCode() {
            return Objects.hash(totalHealth, DPS, attackSpeed, style);
        }
    }

    /**
     * Provides a list of test files and their names for the parameterized test below.
     * @return List of valid test input files and their names
     */
    static Stream<Arguments> testFileProvider(){
        ArrayList<Arguments> args = new ArrayList<>();
        //for all folders provided
        for(final File path : folderPaths){
            //for each file in each folder
            for(final File entry : Objects.requireNonNull(path.listFiles())){
                String inputFile = entry.getPath();
                //if not an input file, skip
                if(! (inputFile.substring(inputFile.length() - inputSuffix.length()).equalsIgnoreCase(inputSuffix))){
                    continue;
                }
                args.add(arguments(Named.of(entry.getName(), entry)));
            }
        }

        return args.stream();
    }


    /**
     * Runs all input files
     */
    @DisplayName("File-based tests for Story 3")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testFileProvider")
    void runFiles(File file){
        String inputFile = file.getPath();

        //guaranteed to have a valid input file
        String ansFile = inputFile.substring(0, inputFile.length() - inputSuffix.length()) + ansSuffix;

        //run test
        ArrayList<DetailedCleanSwordTime<SwordType>> ans = null;
        try {
            ans = manager.getCleaningTimes(inputFile);
        } catch(Exception e){
            e.printStackTrace();
            fail("Error calling manager.getCleaningTimes(\"" + file.getName() + "\": " + e.getMessage());
        }

        //compare to answer
        //get full hash table
        HashMap<Integer, SwordType> table = getFullHashTable(inputFile);

        //read in answer file
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(ansFile));
        } catch (FileNotFoundException e) {
            fail("GRADER ERROR:: ANSWER FILE NOT FOUND:: \"" + file.getName() + "\"");
        }

        ArrayList<DetailedCleanSwordTime<SwordType>> trueAns = new ArrayList<>();
        bf.lines().forEach((line) -> {
            //written out explicitly for clarity for future readers of this code
            String[] parsed = line.split(", ");
            int timeFilled = Integer.parseInt(parsed[0]), delayToFilled = Integer.parseInt(parsed[1]);
            trueAns.add(new DetailedCleanSwordTime<>(
                    timeFilled,
                    delayToFilled,
                    table.get(TempAnsKeySword.getHashValue(Integer.parseInt(parsed[2]),
                            Integer.parseInt(parsed[3]),
                            Integer.parseInt(parsed[4]),
                            parsed[5].substring(1, parsed[5].length()-1)
                    ))));
        });

        //compare
        compareAnswer(trueAns, ans, "Test case: " + inputFile);
    }

    /**
     * Laziness.
     * @param inputFile input file
     * @return hash table of swords
     */
    private HashMap<Integer, SwordType> getFullHashTable(String inputFile) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(inputFile));

            /* REMOVE FOR STUDENT COPY */
            String[] line = bf.readLine().split(" ");
            int numSwords = Integer.parseInt(line[0]);

            HashMap<Integer, SwordType> table = new HashMap<>(numSwords);

            int t, th, hl, l, dps, as;
            String n, dsc, com, sty;
            for (int i = 0; i < numSwords; i++) {
                line = bf.readLine().split(", ");
                //technically unnecessary, but I like seeing the variable names before they get containerized
                t = Integer.parseInt(line[1]);
                th = Integer.parseInt(line[2]);
                hl = Integer.parseInt(line[3]);
                l = Integer.parseInt(line[4]);
                dps = Integer.parseInt(line[5]);
                as = Integer.parseInt(line[6]);
                n = line[7].substring(1, line[7].length() - 1);//remove the "" surrounding the stuff
                dsc = line[8].substring(1, line[8].length() - 1);
                com = line[9].substring(1, line[9].length() - 1);
                sty = line[10].substring(1, line[10].length() - 1);
                SwordType s = new TempAnsKeySword(th, hl, t, l, dps, as, n, dsc, com, sty);
                //insert into hash table
                table.put(s.hashCode(), s);
            }
            return table;
        } catch (IOException e) {
            System.err.println("SOMETHING WENT REALLY WRONG HERE!  Attempting to construct the sword table.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Compares answer and prints detailed information if wrong
     * @param trueAns correct answer
     * @param ans actual answer
     * @param testName name of test, to print if failed
     */
    private static void compareAnswer(ArrayList<DetailedCleanSwordTime<SwordType>> trueAns, ArrayList<DetailedCleanSwordTime<SwordType>> ans, String testName) {
        TestUtils.compareArraysWithEqual(trueAns, ans, testName);
    }
}

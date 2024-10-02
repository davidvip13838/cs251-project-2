package Drones;

import Drones.Interfaces.BetterCleanSwordManagerInterface.DetailedCleanSwordTime;
import Items.Types.SwordType;

import java.util.ArrayList;

/**
 * <bold>251 Students: This is how you should test your program. Generate test cases based on
 * the constraints of the input and see if they are correct. Types of test cases to consider
 * include really small or really big input (you can generate that with a program if you want),
 * cases where the drone could get requests in a weird order, etc. </bold>
 */
public class LocalTesting {
    /**
     * main
     * @param args args
     */
    public static void main(String[] args) {
        BetterCleanSwordManager manager = new BetterCleanSwordManager();
        //current working directory is "Project1/", so the following path needs to be from
        //  "Project1/" to the test file.  For example, if sample.in is at "Project1/src/Drones/sample.in",
        //  you would put the following: "src/Drones/sample.in"
        String filename = "sample.in";

        //If you wanted to list all files in a folder (ex. multiple test files), you could do something like this:
        //File folder = new File("src/Drones/testingFolder");
        //for(final File entry : Objects.requireNonNull(path.listFiles()))
        //  String inputFile = entry.getName();
        //  ...

        ArrayList<DetailedCleanSwordTime<SwordType>> times = manager.getCleaningTimes(filename);
        for (DetailedCleanSwordTime<SwordType> time : times) {
            System.out.println(time.toString());
        }
    }
}


package Drones;

import CommonUtils.BetterHashTable;
import CommonUtils.BetterQueue;
import CommonUtils.MinHeap;
import Drones.Interfaces.BetterCleanSwordManagerInterface;
import Items.Sword;
import Items.Types.SwordType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BetterCleanSwordManager implements BetterCleanSwordManagerInterface {

    // Class to store the cleaning status of swords
    private class Cleantimes implements Comparable<Cleantimes> {
        public Cleantimes(int swordHash, int timetocomplete, int request_t) {
            this.swordHash = swordHash;
            this.timetocomplete =  timetocomplete;
            this.request_t = request_t;
        }
        public int swordHash;
        public int timetocomplete;
        public int request_t;

        public int compareTo(Cleantimes other) {
            return Integer.compare(this.timetocomplete, other.timetocomplete);
        }
    }

    private MinHeap<Cleantimes> cleaning = new MinHeap<>();

    private BetterQueue<Cleantimes> using = new BetterQueue<>();

    private BetterQueue<Cleantimes> unused = new BetterQueue<Cleantimes>();

    // HashMap to quickly find swords based on their unique key
    private BetterHashTable<Integer, Sword> swordMap = new BetterHashTable<>();

    /**
     * Gets the cleaning times per the specifications.
     *
     * @param filename file to read input from
     * @return the list of times requests were filled, times it took to fill them,
     * and sword returned, as per the specifications
     */
    @Override
    public ArrayList<DetailedCleanSwordTime<SwordType>> getCleaningTimes(String filename) {
        ArrayList<DetailedCleanSwordTime<SwordType>> results = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // First line: number of swords and requests
            String[] firstLine = reader.readLine().split(" ");
            int N = Integer.parseInt(firstLine[0]);  // Number of swords
            int M = Integer.parseInt(firstLine[1]);  // Number of requests


            // Reading sword descriptions
            for (int i = 0; i < N; i++) {
                String line = reader.readLine();
                String[] fields = line.split(", ");

                int T = Integer.parseInt(fields[1]);
                int TH = Integer.parseInt(fields[2]);
                int HL = Integer.parseInt(fields[3]);
                int L = Integer.parseInt(fields[4]);
                int DPS = Integer.parseInt(fields[5]);
                int AS = Integer.parseInt(fields[6]);
                String name = fields[7].replaceAll("\"", "");   // Remove quotes
                String description = fields[8].replaceAll("\"", "");
                String commend = fields[9].replaceAll("\"", "");
                String style = fields[10].replaceAll("\"", "").trim();

                // Create sword object
                Sword sword = new Sword(TH, HL,T,L, DPS, AS, name, description, commend, style);
                // print sword object
                //System.out.println(sword.toString());
                //System.out.println(sword.hashCode());
                // add sword to hashmap
                swordMap.insert(sword.hashCode(),sword);
                // if used add to min heap
                if (Integer.parseInt(fields[0]) != -1 ) {
                    cleaning.add(new Cleantimes(sword.hashCode(), sword.getTimeToClean(),0));
                }
            }
            //6 5
            //0, 3, 300, 299, 5, 200, 20, "Katana", "Cool Japanese sword", "Works well when speed is necessary", "CURVED_DECORATIVE_JAPANESE"
            //4, 5, 1000, 900, 20, 1000, 1, "Battering Ram", "Hunk of tree", "Use when hinges are rusted shut", "TREE_WITH_FRESH_SHAVE"
            //-1, 8, 200, 200, 7, 50, 100, "Rapier", "Death by paper cuts", "Most useful when trying to get that perfect golden brown marshmallow but don't want to be singed by the fire", "SHISH_KABOB"
            //-1, 6, 100, 80, 14, 150, 10, "Claymore", "Great sword!", "My best fireplace decoration", "SCOTTISH_VARIANT"
            //-1, 8, 700, 700, 2, 600, 72, "Netherite Pickaxe", "Surprisingly effective tool", "No enchantments yet -- not sure where the wizards are in this world.", "MINECRAFT_PROPRIETARY"
            //-1, 2, 20, 3, 2, 20, 72, "Wooden Pickaxe", "Surprisingly ineffective... thing", "Good for 1 thing: getting enough stone to make a stone pickaxe", "MINECRAFT_PROPRIETARY_BAD"
            //0, 200, 50, 100, "SHISH_KABOB"
            //1, 100, 150, 10, "SCOTTISH_VARIANT"
            //1, 700, 600, 72, "MINECRAFT_PROPRIETARY"
            //3, 20, 20, 72, "MINECRAFT_PROPRIETARY_BAD"
            //6, 1000, 1000, 1, "TREE_WITH_FRESH_SHAVE"

            // Reading request
            for (int i = 0; i < M; i++) {
                String line = reader.readLine();
                String[] fields = line.split(", ");

                int TH = Integer.parseInt(fields[1]);
                int DPS = Integer.parseInt(fields[2]);
                int AS = Integer.parseInt(fields[3]);
                String style = fields[4].replaceAll("\"", "");   // Remove quotes

                int hashcode = Sword.generateUniqueId(TH,DPS,AS,style);
                //System.out.println(hashcode);
                int time = Integer.parseInt(fields[0]);
                unused.add(new Cleantimes(hashcode, swordMap.get(hashcode).getTimeToClean(),time));
                //System.out.println(swordMap.get(hashcode).getTimeToClean());
            }


            int t = 0;
            while (!unused.isEmpty() || !using.isEmpty()) {
                while (unused.peek() != null && unused.peek().request_t == t) {
                    Cleantimes curr_sword = unused.remove();
                    //System.out.println("time is" + t + "curr request time is" + curr_sword.request_t);
                    curr_sword.timetocomplete = curr_sword.timetocomplete + t;
                    using.add(curr_sword);
                    cleaning.add(curr_sword);
                }
                while (!using.isEmpty() && cleaning.peekMin().timetocomplete <= t) {
                    //System.out.println(cleaning.peekMin().timetocomplete);
                    if (cleaning.peekMin().timetocomplete <= t ) {
                        //System.out.println("time is" + t + "sword request time is" + using.peek().request_t);
                        Cleantimes sword_given = cleaning.removeMin();
                        results.add(new DetailedCleanSwordTime<>(t, t - using.remove().request_t, swordMap.get(sword_given.swordHash)));
                    }
                }
                t++;
            }


        } catch (IOException e) {
            System.err.println("ATTENTION TAs: Couldn't find test file: \"" + filename + "\":: " + e.getMessage());
            System.exit(1);
        }
        return results;
    }
}

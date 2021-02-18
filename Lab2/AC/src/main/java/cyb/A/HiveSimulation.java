package cyb.A;

import java.util.ArrayList;
import java.util.List;

public class HiveSimulation {
    private static final int FOREST_SIZE = 100;
    private static final int NUM_OF_BEES = 10;

    public static void main(String[] args) {
        Forest forest = new Forest(FOREST_SIZE);
        forest.invitePooh(52, 63);
        BeeManager beeManager = new BeeManager(forest.getSize());
        List<Thread> bees = new ArrayList<>(NUM_OF_BEES);

        for (int i = 0; i < NUM_OF_BEES; i++) {
            bees.add(new Thread(new BeeRunnable(forest, beeManager)));
            bees.get(i).start();
        }
    }
}

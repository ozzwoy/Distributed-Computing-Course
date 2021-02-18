package cyb.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Championship {
    private static final int NUM_OF_MONKS = 100;
    private static final int MAX_QI_ENERGY = 1000;

    public static List<Integer> generateMonks() {
        List<Integer> qiEnergies = new ArrayList<>(NUM_OF_MONKS);
        Random rand = new Random();
        for (int i = 0; i < NUM_OF_MONKS; i++) {
            qiEnergies.add(Math.abs(rand.nextInt(MAX_QI_ENERGY + 1)));
        }
        return qiEnergies;
    }

    public static void main(String[] args) {
        List<Integer> qiEnergies = generateMonks();
        Integer winner = new ForkJoinPool().invoke(new Fight(qiEnergies, 0, NUM_OF_MONKS - 1));
        System.out.println("BODHISATTVA STATUE GOES TO " + winner + "! CONGRATULATIONS!");
    }
}

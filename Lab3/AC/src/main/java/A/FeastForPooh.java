package A;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class FeastForPooh {
    private static final int TIMES = 5;
    private static final int POT_CAPACITY = 10;
    private static final int NUM_OF_BEES = 5;

    public static void main(String[] args) {
        AtomicInteger count = new AtomicInteger(0);
        HoneyPot pot = new HoneyPot(POT_CAPACITY);
        Semaphore isPotBeingServed = new Semaphore(1);
        Semaphore isPoohEating = new Semaphore(1);
        try {
            isPoohEating.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Pooh pooh = new Pooh(TIMES, count, pot, isPotBeingServed, isPoohEating);
        Thread[] bees = new Bee[NUM_OF_BEES];
        for (int i = 0; i < NUM_OF_BEES; i++) {
            bees[i] = new Bee(pot, isPotBeingServed, isPoohEating);
            bees[i].start();
        }
        pooh.start();

        while (count.get() != TIMES);
        for (Thread bee : bees) {
            bee.interrupt();
        }
    }
}

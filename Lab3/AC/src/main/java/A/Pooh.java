package A;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Pooh extends Thread {
    private final int times;
    private final AtomicInteger count;
    private final HoneyPot pot;
    private final Semaphore isPotBeingServed;
    private final Semaphore isPoohEating;

    public Pooh(int times, AtomicInteger count, HoneyPot pot, Semaphore isPotBeingServed, Semaphore isPoohEating) {
        this.times = times;
        this.count = count;
        this.pot = pot;
        this.isPotBeingServed = isPotBeingServed;
        this.isPoohEating = isPoohEating;
    }

    @Override
    public void run() {
        while (true) {
            try {
                isPoohEating.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pot.eatHoney();
            System.out.println("Pooh ate all honey from the pot.");
            if (count.incrementAndGet() == times) {
                System.out.println("Pooh got bored and fell asleep until tomorrow.");
                return;
            } else {
                isPotBeingServed.release();
            }
        }
    }
}

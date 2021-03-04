package A;

import java.util.concurrent.Semaphore;

public class Pooh extends Thread {
    private final int times;
    private final HoneyPot pot;
    private final Semaphore isPotBeingServed;
    private final Semaphore isPoohEating;

    public Pooh(int times, HoneyPot pot, Semaphore isPotBeingServed, Semaphore isPoohEating) {
        this.times = times;
        this.pot = pot;
        this.isPotBeingServed = isPotBeingServed;
        this.isPoohEating = isPoohEating;
    }

    @Override
    public void run() {
        int i = 0;

        while (true) {
            try {
                isPoohEating.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pot.eatHoney();
            System.out.println("Pooh ate all honey from the pot.");
            if (i == times) {
                System.out.println("Pooh got bored and fell asleep until tomorrow.");
                return;
            } else {
                isPotBeingServed.release();
                i++;
            }
        }
    }
}

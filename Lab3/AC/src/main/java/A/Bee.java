package A;

import java.util.concurrent.Semaphore;

public class Bee extends Thread {
    private final HoneyPot pot;
    private final Semaphore isPotBeingServed;
    private final Semaphore isPoohEating;

    public Bee(HoneyPot pot, Semaphore isPotBeingServed, Semaphore isPoohEating) {
        this.pot = pot;
        this.isPotBeingServed = isPotBeingServed;
        this.isPoohEating = isPoohEating;
    }

    @Override
    public void run() {
        while (true) {
            try {
                isPotBeingServed.acquire();
            } catch (InterruptedException e) {
                System.out.println("Bee #" + Thread.currentThread().getId() + " finished work.");
                return;
            }

            pot.addHoney();
            System.out.println("Bee #" + Thread.currentThread().getId() + " added honey. Current level: " +
                               pot.getCurrentLevel() + ".");
            if (pot.isFull()) {
                System.out.println("Bee #" + Thread.currentThread().getId() + " woke Pooh up.");
                isPoohEating.release();
            } else {
                isPotBeingServed.release();
            }
        }
    }
}

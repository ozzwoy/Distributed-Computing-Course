package C;

import java.util.concurrent.BrokenBarrierException;

public class CustomCyclicBarrier {
    private final int parties;
    private int numberWaiting;
    private final Runnable barrierAction;
    private boolean broken;
    private boolean released;

    public CustomCyclicBarrier(int parties, Runnable barrierAction) throws IllegalArgumentException {
        if (parties < 1) {
            throw new IllegalArgumentException("Number of parties should be greater than 1.");
        }
        this.parties = parties;
        this.barrierAction = barrierAction;
        this.numberWaiting = 0;
        this.broken = false;
        this.released = false;
    }

    public int getParties() {
        return parties;
    }

    public int getNumberWaiting() {
        return numberWaiting;
    }

    public boolean isBroken() {
        return broken;
    }

    public boolean isReleased() {
        return released;
    }

    public synchronized void reset() {
        numberWaiting = 0;
        broken = true;
        released = true;
        notifyAll();
    }

    public synchronized int await() throws BrokenBarrierException, InterruptedException {
        broken = false;
        numberWaiting++;
        int arrivalIndex = parties - numberWaiting;

        while (numberWaiting < parties) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                reset();
                throw e;
            }

            if (released) {
                if (broken) {
                    throw new BrokenBarrierException("The barrier was broken.");
                }
                break;
            }
        }

        if (arrivalIndex == 0) {
            numberWaiting = 0;
            notifyAll();
            if (barrierAction != null) {
                barrierAction.run();
            }
            released = true;
        }

        return arrivalIndex;
    }
}


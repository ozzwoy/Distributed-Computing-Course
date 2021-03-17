package A;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoldiersOrganizerThread extends Thread {
    private final Line line;
    private final int left;
    private final int right;
    private final CyclicBarrier barrier;
    private final AtomicBoolean shouldStop;

    public SoldiersOrganizerThread(Line line, int left, int right, CyclicBarrier barrier, AtomicBoolean shouldStop) {
        this.line = line;
        this.left = left;
        this.right = right;
        this.barrier = barrier;
        this.shouldStop = shouldStop;
    }

    @Override
    public void run() {
        while (!shouldStop.get()) {
            for (int i = left; i <= right; i++) {
                if (line.isSoldierTurnedRight(i)) {
                    if ((i + 1) != line.size() && line.isSoldierTurnedLeft(i + 1)) {
                        line.turnSoldierAround(i);
                        if (i != right) {
                            line.turnSoldierAround(i + 1);
                        }
                    }
                } else if (i == left && i != 0 && line.isSoldierTurnedRight(i - 1)) {
                    line.turnSoldierAround(i);
                }
            }

            try {
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

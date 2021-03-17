package A;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompletionCheckerRunnable implements Runnable {
    private final Line line;
    private final AtomicBoolean shouldStop;

    public CompletionCheckerRunnable(Line line, AtomicBoolean shouldStop) {
        this.line = line;
        this.shouldStop = shouldStop;
    }

    @Override
    public void run() {
        line.update();
        line.print();
        if (line.isOrdered()) {
            shouldStop.set(true);
        }
    }
}

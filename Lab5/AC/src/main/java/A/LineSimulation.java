package A;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class LineSimulation {
    private static final int SIZE = 200;
    private static final int NUM_OF_THREADS = 4;

    public static void main(String[] args) {
        Line line = new Line(SIZE);

        line.print();

        AtomicBoolean isFinished = new AtomicBoolean(false);
        CyclicBarrier barrier = new CyclicBarrier(NUM_OF_THREADS, new CompletionCheckerRunnable(line, isFinished));
        Thread[] threads = new SoldiersOrganizerThread[NUM_OF_THREADS];
        int chunk = SIZE / NUM_OF_THREADS;

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new SoldiersOrganizerThread(line, i * chunk, (i + 1) * chunk - 1, barrier,
                                                     isFinished);
            threads[i].start();
        }
    }
}

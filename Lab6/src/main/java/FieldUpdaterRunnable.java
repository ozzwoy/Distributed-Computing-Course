import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class FieldUpdaterRunnable implements Runnable {
    private final Field field;
    private final Semaphore letUIDraw;
    private final Semaphore letThreadsScan;
    private final AtomicBoolean shouldStop;
    private final int delay;

    public FieldUpdaterRunnable(Field field, Semaphore letUIDraw, Semaphore letThreadsScan, AtomicBoolean shouldStop,
                                int delay) {
        this.field = field;
        this.letUIDraw = letUIDraw;
        this.letThreadsScan = letThreadsScan;
        this.shouldStop = shouldStop;
        this.delay = delay;
    }

    @Override
    public void run() {
        if (!shouldStop.get()) {
            try {
                field.update();
                Thread.sleep(delay);
                letUIDraw.release();
                letThreadsScan.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            letUIDraw.release();
            Thread.currentThread().interrupt();
        }
    }
}

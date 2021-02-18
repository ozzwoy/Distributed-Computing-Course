package cyb.A;

public class BeeRunnable implements Runnable {
    private final Forest forest;
    private final BeeManager beeManager;

    public BeeRunnable(Forest forest, BeeManager beeManager) {
        this.forest = forest;
        this.beeManager = beeManager;
    }

    @Override
    public void run() {
        int region;

        while (!beeManager.shouldStop()) {
            region = beeManager.getRegion();
            if (region == BeeManager.NO_REGION) {
                break;
            }

            for (int i = 0; i < forest.getSize(); i++) {
                if (forest.checkForPooh(i, region)) {
                    forest.clearGlade(i, region);
                    beeManager.notifyPoohFound();
                    System.out.println("Bee #" + Thread.currentThread().getId() +
                                       ": Pooh found and punished at (" + i + ", " + region + ")!");
                    return;
                }
            }

            System.out.println("Bee #" + Thread.currentThread().getId() + " checked region #" + region +
                               ": Pooh not found.");
        }
    }
}


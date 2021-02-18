package cyb.A;

public class BeeManager {
    public static final int NO_REGION = -1;
    private boolean isPoohFound = false;
    private final int regions;
    private int nextRegion = 0;

    public BeeManager(int regions) {
        this.regions = regions;
    }

    public void notifyPoohFound() {
        isPoohFound = true;
    }

    public boolean shouldStop() {
        return isPoohFound || nextRegion == regions;
    }

    public synchronized int getRegion() {
        return shouldStop() ? NO_REGION : nextRegion++;
    }
}

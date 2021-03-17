package C;

public class StatusPrinterRunnable implements Runnable {
    private final int[] sums;

    public StatusPrinterRunnable(int[] sums) {
        this.sums = sums;
    }

    @Override
    public void run() {
        String status = "Sums: " + sums[0] + " " + sums[1] + " " + sums[2];
        System.out.println(status);
    }
}

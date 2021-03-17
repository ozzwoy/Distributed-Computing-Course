package C;

public class ArraysEqualizer {

    public static void main(String[] args) {
        int[] sums = new int[3];
        CustomCyclicBarrier firstStage = new CustomCyclicBarrier(3, null);
        CustomCyclicBarrier secondStage = new CustomCyclicBarrier(3, new StatusPrinterRunnable(sums));
        Thread[] threads = new ArrayKeeperThread[3];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ArrayKeeperThread(sums, i, firstStage, secondStage);
        }

        System.out.println("Sums: " + sums[0] + " " + sums[1] + " " + sums[2]);

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }
}

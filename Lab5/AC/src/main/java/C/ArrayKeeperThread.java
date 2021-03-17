package C;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class ArrayKeeperThread extends Thread {
    private final int[] array;
    private final int[] sums;
    private final int index;
    private final CustomCyclicBarrier firstStage;
    private final CustomCyclicBarrier secondStage;

    public ArrayKeeperThread(int[] sums, int index, CustomCyclicBarrier firstStage, CustomCyclicBarrier secondStage) {
        this.array = generateRandomArray();
        this.sums = sums;
        this.index = index;
        this.firstStage = firstStage;
        this.secondStage = secondStage;

        sums[index] = calcSum();
    }

    private static int[] generateRandomArray() {
        int[] result;
        Random random = new Random();
        result = new int[random.nextInt(10) + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = random.nextInt(1000);
        }
        return result;
    }

    private int calcSum() {
        int sum = 0;
        for (int n : array) {
            sum += n;
        }
        return sum;
    }

    private boolean areEqual() {
        return sums[index] == sums[(index + 1) % sums.length] &&
               sums[index] == sums[(index + 2) % sums.length];
    }

    private int getDigit() {
        int i = (index + 1) % sums.length;
        int j = (index + 2) % sums.length;

        if (sums[index] < sums[i]) {
            if (sums[index] != sums[j]) {
                return 1;
            }
        } else if (sums[index] > sums[i]) {
            if (sums[index] != sums[j]) {
                return -1;
            }
        }

        return 0;
    }

    @Override
    public void run() {
        Random random = new Random();

        while (!areEqual()) {
            try {
                firstStage.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }

            int digit = getDigit();
            array[random.nextInt(array.length)] += digit;
            sums[index] += digit;

            try {
                secondStage.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

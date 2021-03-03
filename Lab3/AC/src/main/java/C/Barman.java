package C;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Barman extends Thread {
    private final int times;
    private final Table table;
    private final Semaphore isSmokerSmoking;
    private final Semaphore isBarmanWorking;

    public Barman(int times, Table table, Semaphore isSmokerSmoking, Semaphore isBarmanWorking) {
        this.times = times;
        this.table = table;
        this.isSmokerSmoking = isSmokerSmoking;
        this.isBarmanWorking = isBarmanWorking;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < times; i++) {
            try {
                isBarmanWorking.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SmokerType[] values = SmokerType.values();
            SmokerType smoker = Arrays.asList(values).get(random.nextInt(values.length));
            synchronized (table) {
                switch (smoker) {
                    case TOBACCO_KEEPER -> {
                        table.putPaper();
                        table.putMatches();
                        System.out.println("Barman put paper and matches.");
                    }
                    case PAPER_KEEPER -> {
                        table.putTobacco();
                        table.putMatches();
                        System.out.println("Barman put tobacco and matches.");
                    }
                    case MATCHES_KEEPER -> {
                        table.putTobacco();
                        table.putPaper();
                        System.out.println("Barman put tobacco and paper.");
                    }
                }
            }

            isSmokerSmoking.release();
        }
    }
}

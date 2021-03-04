package C;

import java.util.Arrays;
import java.util.Random;

public class Barman extends Thread {
    private final int times;
    private final Table table;
    private final Semaphores semaphores;

    public Barman(int times, Table table, Semaphores semaphores) {
        this.times = times;
        this.table = table;
        this.semaphores = semaphores;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < times; i++) {
            try {
                semaphores.isBarmanWorking.acquire();
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
                        semaphores.isSmokerWithTobaccoSmoking.release();
                    }
                    case PAPER_KEEPER -> {
                        table.putTobacco();
                        table.putMatches();
                        System.out.println("Barman put tobacco and matches.");
                        semaphores.isSmokerWithPaperSmoking.release();
                    }
                    case MATCHES_KEEPER -> {
                        table.putTobacco();
                        table.putPaper();
                        System.out.println("Barman put tobacco and paper.");
                        semaphores.isSmokerWithMatchesSmoking.release();
                    }
                }
            }
        }
    }
}

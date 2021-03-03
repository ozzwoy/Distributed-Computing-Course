package C;

import java.util.concurrent.Semaphore;

public class Smoker extends Thread {
    private final SmokerType smokerType;
    private final Table table;
    private final Semaphore isSmokerSmoking;
    private final Semaphore isBarmanWorking;

    public Smoker(SmokerType smokerType, Table table, Semaphore isSmokerSmoking, Semaphore isBarmanWorking) {
        this.smokerType = smokerType;
        this.table = table;
        this.isSmokerSmoking = isSmokerSmoking;
        this.isBarmanWorking = isBarmanWorking;
    }

    @Override
    public void run() {
        while (true) {
            try {
                isSmokerSmoking.acquire();
            } catch (InterruptedException e) {
                System.out.println(smokerType.toString() + " leaves.");
                return;
            }
            Cigarette cigarette = null;

            switch (smokerType) {
                case TOBACCO_KEEPER -> {
                    if (table.checkForTobacco()) {
                        isSmokerSmoking.release();
                        continue;
                    } else {
                        cigarette = new Cigarette(true, table.takePaper(), table.takeMatches());
                    }
                }
                case PAPER_KEEPER -> {
                    if (table.checkForPaper()) {
                        isSmokerSmoking.release();
                        continue;
                    } else {
                        cigarette = new Cigarette(table.takeTobacco(), true, table.takeMatches());
                    }
                }
                case MATCHES_KEEPER -> {
                    if (table.checkForMatches()) {
                        isSmokerSmoking.release();
                        continue;
                    } else {
                        cigarette = new Cigarette(table.takeTobacco(), table.takePaper(), true);
                    }
                }
            }

            System.out.println(smokerType.toString() + " started smoking");
            cigarette.smoke();
            System.out.println(smokerType.toString() + " finished smoking");
            isBarmanWorking.release();
        }
    }
}

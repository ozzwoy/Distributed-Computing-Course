package C.smokers;

import C.Cigarette;
import C.Semaphores;
import C.SmokerType;
import C.Table;

public class SmokerWithTobacco extends Thread {
    private final Table table;
    private final Semaphores semaphores;

    public SmokerWithTobacco(Table table, Semaphores semaphores) {
        this.table = table;
        this.semaphores = semaphores;
    }

    @Override
    public void run() {
        while (true) {
            try {
                semaphores.isSmokerWithTobaccoSmoking.acquire();
            } catch (InterruptedException e) {
                System.out.println(SmokerType.TOBACCO_KEEPER.toString() + " leaves.");
                return;
            }

            Cigarette cigarette;
            if (table.checkForTobacco()) {
                semaphores.isSmokerWithTobaccoSmoking.release();
                continue;
            } else {
                cigarette = new Cigarette(true, table.takePaper(), table.takeMatches());
            }

            System.out.println(SmokerType.TOBACCO_KEEPER.toString() + " started smoking");
            cigarette.smoke();
            System.out.println(SmokerType.TOBACCO_KEEPER.toString() + " finished smoking");

            semaphores.isBarmanWorking.release();
        }
    }
}

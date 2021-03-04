package C.smokers;

import C.Cigarette;
import C.Semaphores;
import C.SmokerType;
import C.Table;

public class SmokerWithPaper extends Thread {
    private final Table table;
    private final Semaphores semaphores;

    public SmokerWithPaper(Table table, Semaphores semaphores) {
        this.table = table;
        this.semaphores = semaphores;
    }

    @Override
    public void run() {
        while (true) {
            try {
                semaphores.isSmokerWithPaperSmoking.acquire();
            } catch (InterruptedException e) {
                System.out.println(SmokerType.PAPER_KEEPER.toString() + " leaves.");
                return;
            }

            Cigarette cigarette;
            if (table.checkForPaper()) {
                semaphores.isSmokerWithPaperSmoking.release();
                continue;
            } else {
                cigarette = new Cigarette(table.takeTobacco(), true, table.takeMatches());
            }

            System.out.println(SmokerType.PAPER_KEEPER.toString() + " started smoking");
            cigarette.smoke();
            System.out.println(SmokerType.PAPER_KEEPER.toString() + " finished smoking");

            semaphores.isBarmanWorking.release();
        }
    }
}

package C.smokers;

import C.Cigarette;
import C.Semaphores;
import C.SmokerType;
import C.Table;

public class SmokerWithMatches extends Thread {
    private final Table table;
    private final Semaphores semaphores;

    public SmokerWithMatches(Table table, Semaphores semaphores) {
        this.table = table;
        this.semaphores = semaphores;
    }

    @Override
    public void run() {
        while (true) {
            try {
                semaphores.isSmokerWithMatchesSmoking.acquire();
            } catch (InterruptedException e) {
                System.out.println(SmokerType.MATCHES_KEEPER.toString() + " leaves.");
                return;
            }

            Cigarette cigarette;
            if (table.checkForMatches()) {
                semaphores.isSmokerWithMatchesSmoking.release();
                continue;
            } else {
                cigarette = new Cigarette(table.takeTobacco(), table.takePaper(), true);
            }

            System.out.println(SmokerType.MATCHES_KEEPER.toString() + " started smoking");
            cigarette.smoke();
            System.out.println(SmokerType.MATCHES_KEEPER.toString() + " finished smoking");

            semaphores.isBarmanWorking.release();
        }
    }
}

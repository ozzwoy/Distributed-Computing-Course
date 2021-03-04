package C;

import C.smokers.SmokerWithMatches;
import C.smokers.SmokerWithPaper;
import C.smokers.SmokerWithTobacco;

import java.util.concurrent.Semaphore;

public class SmokersBar {

    public static void main(String[] args) {
        Table table = new Table();

        Semaphore isSmokerWithTobaccoSmoking = new Semaphore(1);
        Semaphore isSmokerWithPaperSmoking = new Semaphore(1);
        Semaphore isSmokerWithMatchesSmoking = new Semaphore(1);
        Semaphore isBarmanWorking = new Semaphore(1);
        try {
            isSmokerWithTobaccoSmoking.acquire();
            isSmokerWithPaperSmoking.acquire();
            isSmokerWithMatchesSmoking.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Semaphores semaphores = new Semaphores(isSmokerWithTobaccoSmoking, isSmokerWithPaperSmoking,
                                               isSmokerWithMatchesSmoking, isBarmanWorking);

        Thread barman = new Barman(5, table, semaphores);
        Thread tobacco_keeper = new SmokerWithTobacco(table, semaphores);
        Thread paper_keeper = new SmokerWithPaper(table, semaphores);
        Thread matches_keeper = new SmokerWithMatches(table, semaphores);

        tobacco_keeper.start();
        paper_keeper.start();
        matches_keeper.start();
        barman.start();

        try {
            barman.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tobacco_keeper.interrupt();
        paper_keeper.interrupt();
        matches_keeper.interrupt();
    }
}

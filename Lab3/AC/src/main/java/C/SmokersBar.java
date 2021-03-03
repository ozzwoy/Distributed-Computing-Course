package C;

import java.util.concurrent.Semaphore;

public class SmokersBar {

    public static void main(String[] args) {
        Table table = new Table();
        Semaphore isSmokerSmoking = new Semaphore(1);
        Semaphore isBarmanWorking = new Semaphore(1);
        try {
            isSmokerSmoking.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread barman = new Barman(5, table, isSmokerSmoking, isBarmanWorking);
        Thread tobacco_keeper = new Smoker(SmokerType.TOBACCO_KEEPER, table, isSmokerSmoking, isBarmanWorking);
        Thread paper_keeper = new Smoker(SmokerType.PAPER_KEEPER, table, isSmokerSmoking, isBarmanWorking);
        Thread matches_keeper = new Smoker(SmokerType.MATCHES_KEEPER, table, isSmokerSmoking, isBarmanWorking);

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

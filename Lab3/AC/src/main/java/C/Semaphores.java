package C;

import java.util.concurrent.Semaphore;

public class Semaphores {
    public final Semaphore isSmokerWithTobaccoSmoking;
    public final Semaphore isSmokerWithPaperSmoking;
    public final Semaphore isSmokerWithMatchesSmoking;
    public final Semaphore isBarmanWorking;

    public Semaphores(Semaphore isSmokerWithTobaccoSmoking, Semaphore isSmokerWithPaperSmoking,
                      Semaphore isSmokerWithMatchesSmoking, Semaphore isBarmanWorking) {
        this.isSmokerWithTobaccoSmoking = isSmokerWithTobaccoSmoking;
        this.isSmokerWithPaperSmoking = isSmokerWithPaperSmoking;
        this.isSmokerWithMatchesSmoking = isSmokerWithMatchesSmoking;
        this.isBarmanWorking = isBarmanWorking;
    }
}

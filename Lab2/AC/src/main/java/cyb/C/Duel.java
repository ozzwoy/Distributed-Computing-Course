package cyb.C;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Duel extends RecursiveTask<Integer> {
    private final List<Integer> qiEnergies;
    private final int first;
    private final int last;

    public Duel(List<Integer> qiEnergies, int first, int last) {
        this.qiEnergies = qiEnergies;
        this.first = first;
        this.last = last;
    }

    @Override
    protected Integer compute() {
        if (last - first == 1) {
            return qiEnergies.get(last) > qiEnergies.get(first) ? last : first;
        } else if (last == first) {
            return last;
        }

        Duel firstDuel = new Duel(qiEnergies, first, (first + last) / 2);
        Duel secondDuel = new Duel(qiEnergies, (first + last) / 2 + 1, last);
        firstDuel.fork();
        secondDuel.fork();
        int firstWinner = firstDuel.join();
        int secondWinner = secondDuel.join();

        if (qiEnergies.get(secondWinner) > qiEnergies.get(firstWinner)) {
            System.out.println(secondWinner + " beats " + firstWinner + " with " + qiEnergies.get(secondWinner) + "/" +
                               qiEnergies.get(firstWinner));
            return secondWinner;
        }

        System.out.println(firstWinner + " beats " + secondWinner + " with " + qiEnergies.get(firstWinner) + "/" +
                qiEnergies.get(secondWinner));
        return firstWinner;
    }
}

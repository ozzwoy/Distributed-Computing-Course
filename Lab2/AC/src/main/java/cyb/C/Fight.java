package cyb.C;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Fight extends RecursiveTask<Integer> {
    private final List<Integer> qiEnergies;
    private final int first;
    private final int last;

    public Fight(List<Integer> qiEnergies, int first, int last) {
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

        Fight firstFight = new Fight(qiEnergies, first, (first + last) / 2);
        Fight secondFight = new Fight(qiEnergies, (first + last) / 2 + 1, last);
        firstFight.fork();
        secondFight.fork();
        int firstWinner = firstFight.join();
        int secondWinner = secondFight.join();

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

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

enum RangeZone {
    FIRST_ZONE("First Zone"),
    SECOND_ZONE("Second Zone");

    private final String name;

    RangeZone(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Terminal {
    private final int number;
    private final RangeZone zone;
    private final Semaphore laddersSemaphore;

    public Terminal(int number, RangeZone zone, int numOfLadders) {
        this.number = number;
        this.zone = zone;
        this.laddersSemaphore = new Semaphore(numOfLadders);
    }

    public int getNumber() {
        return number;
    }

    public RangeZone getZone() {
        return zone;
    }

    public boolean areAllLaddersBusy() {
        return laddersSemaphore.availablePermits() == 0;
    }

    public void land() {
        try {
            laddersSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void depart() {
        laddersSemaphore.release();
    }
}

class Plane extends Thread {
    private final RangeZone zone;
    private final int capacity;
    private final Terminal[] terminals;

    public Plane(RangeZone zone, int capacity, Terminal[] terminals) {
        this.zone = zone;
        this.capacity = capacity;
        this.terminals = terminals;
    }

    private void doLand(Terminal terminal) {
        terminal.land();
        System.out.println("Plane #" + Thread.currentThread().getId() + " has landed in terminal #" +
                terminal.getNumber() + ".");
        try {
            Thread.sleep(capacity * 10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Plane #" + Thread.currentThread().getId() +
                " has finished loading/unloading in terminal #" + terminal.getNumber() + ".");
        terminal.depart();
    }

    @Override
    public void run() {
        int last_index = 0;
        boolean hasLanded = false;

        System.out.println("Plane #" + Thread.currentThread().getId() + " arrived at airport.");
        for (int i = 0; i < terminals.length; i++) {
            if (terminals[i].getZone() == zone) {
                if (!terminals[i].areAllLaddersBusy()) {
                    doLand(terminals[i]);
                    hasLanded = true;
                    break;
                } else {
                    last_index = i;
                }
            }
        }

        if (!hasLanded) {
            doLand(terminals[last_index]);
        }
    }
}

public class Task11Java {
    private static final int NUM_OF_TERMINALS = 4;
    private static final int NUM_OF_PLANES = 100;

    public static void main(String[] args) {
        Random random = new Random();

        Terminal[] terminals = new Terminal[NUM_OF_TERMINALS];
        for (int i = 0; i < NUM_OF_TERMINALS; i++) {
            terminals[i] = new Terminal(i, Arrays.asList(RangeZone.values())
                                                 .get(random.nextInt(RangeZone.values().length)), 3);
            System.out.println("Terminal #" + i + ": " + terminals[i].getZone().getName() + ", number of ladders: 3.");
        }

        Plane[] planes = new Plane[NUM_OF_PLANES];
        for (int i = 0; i < NUM_OF_PLANES; i++) {
            planes[i] = new Plane(Arrays.asList(RangeZone.values())
                                        .get(random.nextInt(RangeZone.values().length)), 100, terminals);
            planes[i].start();

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package A;

public class HoneyPot {
    private final int capacity;
    private int currentLevel;

    public HoneyPot(int capacity) {
        this.capacity = capacity;
        currentLevel = 0;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean isFull() {
        return currentLevel == capacity;
    }

    public void addHoney() {
        if (currentLevel < capacity) {
            currentLevel++;
        } else {
            System.out.println("Error! Pot overflow!");
        }
    }

    public void eatHoney() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentLevel = 0;
    }
}

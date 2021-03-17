package A;

import java.util.Random;

public class Soldier {
    private boolean turnedRight;

    public Soldier() {
        turnedRight = new Random().nextBoolean();
    }

    public boolean isTurnedRight() {
        return turnedRight;
    }

    public boolean isTurnedLeft() {
        return !turnedRight;
    }

    public void turnAround() {
        turnedRight = !turnedRight;
    }
}

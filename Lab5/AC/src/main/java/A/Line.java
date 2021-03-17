package A;

public class Line {
    private final Soldier[] soldiers;
    private final boolean[] needTurn;

    public Line(int size) {
        this.soldiers = new Soldier[size];
        for (int i = 0; i < size; i++) {
            soldiers[i] = new Soldier();
        }
        this.needTurn = new boolean[size];
    }

    public int size() {
        return soldiers.length;
    }

    public boolean isSoldierTurnedRight(int i) {
        return soldiers[i].isTurnedRight();
    }

    public boolean isSoldierTurnedLeft(int i) {
        return soldiers[i].isTurnedLeft();
    }

    public void turnSoldierAround(int i) {
        needTurn[i] = true;
    }

    public boolean isOrdered() {
        for (int i = 0; i < soldiers.length - 1; i++) {
            if (soldiers[i].isTurnedRight() && soldiers[i + 1].isTurnedLeft()) {
                return false;
            }
        }
        return true;
    }

    public void update() {
        for (int i = 0; i < needTurn.length; i++) {
            if (needTurn[i]) {
                soldiers[i].turnAround();
            }
            needTurn[i] = false;
        }
    }

    public void print() {
        StringBuilder status = new StringBuilder();

        for (Soldier soldier : soldiers) {
            if (soldier.isTurnedRight()) {
                status.append("-> ");
            } else {
                status.append("<- ");
            }
        }

        System.out.println(status + "\n");
    }
}

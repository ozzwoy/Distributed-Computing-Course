public class Cell {
    private Colors color = Colors.NEUTRAL;
    private Colors updatedColor = Colors.NEUTRAL;

    public boolean isAlive() {
        return color != Colors.NEUTRAL;
    }

    public void mortify() {
        updatedColor = Colors.NEUTRAL;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        updatedColor = color;
    }

    public void update() {
        color = updatedColor;
    }
}

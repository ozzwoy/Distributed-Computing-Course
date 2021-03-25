public enum Colors {
    NEUTRAL("white"),
    BLACK("black"),
    RED("red"),
    GREEN("green"),
    BLUE("blue");

    private final String name;

    Colors(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

import java.util.stream.Stream;

public class Field {
    private final Cell[][] cells;

    public Field(int width, int height) {
        cells = new Cell[height][];
        for (int i = 0; i < height; i++) {
            cells[i] = Stream.generate(Cell::new).limit(width).toArray(Cell[]::new);
        }
    }

    public int getWidth() {
        return cells[0].length;
    }

    public int getHeight() {
        return cells.length;
    }

    public Cell get(int i, int j) {
        return cells[i][j];
    }

    public void update() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.update();
            }
        }
    }
}

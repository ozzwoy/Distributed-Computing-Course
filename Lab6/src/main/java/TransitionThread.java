import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TransitionThread extends Thread {
    private final Field field;
    private final int x0;
    private final int y0;
    private final int width;
    private final int height;
    private final CyclicBarrier barrier;

    public TransitionThread(Field field, int x0, int y0, int width, int height, CyclicBarrier barrier) {
        this.field = field;
        this.x0 = x0;
        this.y0 = y0;
        this.width = width;
        this.height = height;
        this.barrier = barrier;
    }

    private Colors checkCell(int i, int j) {
        List<Cell> neighbours = new ArrayList<>();
        Cell neighbour;

        for (int k = i - 1; k <= i + 1; k++) {
            if (k < 0 || k >= field.getHeight()) {
                continue;
            }
            for (int l = j - 1; l <= j + 1; l++) {
                if (l < 0 || l >= field.getWidth() || (k == i && l == j)) {
                    continue;
                }
                neighbour = field.get(k, l);
                if (neighbour.isAlive()) {
                    neighbours.add(neighbour);
                }
                if (neighbours.size() > 3) {
                    return Colors.NEUTRAL;
                }
            }
        }

        if (neighbours.size() < 2) {
            return Colors.NEUTRAL;
        } else if (neighbours.size() == 2) {
            return field.get(i, j).getColor();
        } else if (field.get(i, j).isAlive()) {
            return field.get(i, j).getColor();
        } else {
            Colors color1 = neighbours.get(0).getColor();
            Colors color2 = neighbours.get(1).getColor();
            Colors color3 = neighbours.get(2).getColor();

            if (color1 == color2 || color1 == color3) {
                return color1;
            } else {
                return color2;
            }
        }
    }

    private void scanArea() {
        for (int i = 0; i < height; i++) {
            int posY = y0 + i;
            if (posY == field.getHeight()) {
                break;
            }

            for (int j = 0; j < width; j++) {
                int posX = x0 + j;
                if (posX == field.getWidth()) {
                    break;
                }

                Colors color = checkCell(posY, posX);
                if (color == Colors.NEUTRAL) {
                    field.get(posY, posX).mortify();
                } else {
                    field.get(posY, posX).setColor(color);
                }
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            scanArea();
            try {
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                return;
            }
        }
    }
}

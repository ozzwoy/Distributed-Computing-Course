package duck_hunt;

import java.awt.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Duck extends Thread {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static final int MAX_VELOCITY = 6;

    private static final Image IMAGE_ALIVE_LEFT = Toolkit.getDefaultToolkit()
            .createImage("src/main/resources/duck.gif")
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
    private static final Image IMAGE_ALIVE_RIGHT = Toolkit.getDefaultToolkit()
            .createImage("src/main/resources/duck_mirrored.gif")
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
    private static final Image IMAGE_DEAD = Toolkit.getDefaultToolkit()
            .createImage("src/main/resources/bam.gif")
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
    private static final int FINAL_ANIMATION_DURATION = 20;

    private final int windowWidth;
    private Image currentImage;
    private int x;
    private final int y;
    private final int velocity;
    private Direction direction;
    private boolean dead = false;
    private int finalAnimationCounter = 0;

    private final GraphicsHolder graphicsHolder;
    private final CyclicBarrier barrier;

    public Duck(int windowWidth, int x, int y, int velocity, Direction direction, GraphicsHolder graphicsHolder,
                CyclicBarrier barrier) {
        this.windowWidth = windowWidth;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.direction = direction;
        this.graphicsHolder = graphicsHolder;
        this.barrier = barrier;

        switch (direction) {
            case LEFT -> currentImage = IMAGE_ALIVE_LEFT;
            case RIGHT -> currentImage = IMAGE_ALIVE_RIGHT;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDead() {
        return dead;
    }

    public void kill() {
        dead = true;
    }

    private void draw() {
        if (!dead) {
            x += (direction == Direction.RIGHT) ? velocity : -velocity;
            if (x > windowWidth - WIDTH) {
                x = windowWidth - WIDTH;
                direction = Direction.LEFT;
                currentImage = IMAGE_ALIVE_LEFT;
            } else if (x < 0) {
                x = 0;
                direction = Direction.RIGHT;
                currentImage = IMAGE_ALIVE_RIGHT;
            }
            graphicsHolder.currentGraphics.drawImage(currentImage, x, y, null);
        } else {
            graphicsHolder.currentGraphics.drawImage(IMAGE_DEAD, x, y, null);
            finalAnimationCounter++;
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                return;
            }

            if (!dead || finalAnimationCounter != FINAL_ANIMATION_DURATION) {
                draw();
            }

            try {
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                return;
            }
        }
    }
}

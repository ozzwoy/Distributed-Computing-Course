package duck_hunt;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Hunter extends Thread {
    public static final int WIDTH = 150;
    public static final int HEIGHT = 250;

    private static final Image IMAGE_LEFT = Toolkit.getDefaultToolkit()
            .createImage("src/main/resources/hunter.png")
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
    private static final Image IMAGE_RIGHT = Toolkit.getDefaultToolkit()
            .createImage("src/main/resources/hunter_mirrored.png")
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);

    private final int windowWidth;
    private final int windowHeight;
    private Image currentImage;
    private int x;
    private final int y;
    private final Duck[] ducks;
    private final int velocity = 5;
    private Direction direction = Direction.LEFT;
    private boolean moving = false;
    private final List<Bullet> bullets = new LinkedList<>();
    private boolean shouldShoot = false;

    private final GraphicsHolder graphicsHolder;
    private final CyclicBarrier barrier;

    public Hunter(Duck[] ducks, int windowWidth, int windowHeight, GraphicsHolder graphicsHolder,
                  CyclicBarrier barrier) {
        this.ducks = ducks;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.currentImage = IMAGE_LEFT;
        this.x = windowWidth / 2 - WIDTH / 2;
        this.y = windowHeight - HEIGHT - 10;
        this.graphicsHolder = graphicsHolder;
        this.barrier = barrier;
    }

    public void moveLeft() {
        moving = true;
        direction = Direction.LEFT;
        currentImage = IMAGE_LEFT;
    }

    public void moveRight() {
        moving = true;
        direction = Direction.RIGHT;
        currentImage = IMAGE_RIGHT;
    }

    public void stopMoving() {
        moving = false;
    }

    public void shoot() {
        shouldShoot = true;
    }

    private void draw() {
        if (moving) {
            x += (direction == Direction.RIGHT) ? velocity : -velocity;
            if (x > windowWidth - WIDTH) {
                x = windowWidth - WIDTH;
            } else if (x < 0) {
                x = 0;
            }
        }
        graphicsHolder.currentGraphics.drawImage(currentImage, x, y, null);

        if (!bullets.isEmpty() && bullets.get(0).getY() > windowHeight - Bullet.HEIGHT) {
            bullets.remove(0);
        }

        boolean hit = false;

        for (int i = 0; i < bullets.size(); i++) {
            for (Duck duck : ducks) {
                if (!duck.isDead() && bullets.get(i).intersects(duck)) {
                    duck.kill();
                    bullets.remove(i);
                    hit = true;
                    i--;
                    break;
                }
            }

            if (!hit) {
                bullets.get(i).draw();
                bullets.get(i).move();
                hit = false;
            }
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

            if (shouldShoot) {
                switch (direction) {
                    case LEFT -> bullets.add(new Bullet(x + 115, windowHeight - HEIGHT - Bullet.HEIGHT,
                            graphicsHolder));
                    case RIGHT -> bullets.add(new Bullet(x + 20, windowHeight - HEIGHT - Bullet.HEIGHT,
                            graphicsHolder));
                }
                shouldShoot = false;
            }
            draw();

            try {
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                return;
            }
        }
    }
}

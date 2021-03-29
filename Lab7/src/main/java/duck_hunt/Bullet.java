package duck_hunt;

import java.awt.*;

public class Bullet {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 28;

    private static final Image IMAGE = Toolkit.getDefaultToolkit()
            .createImage("src/main/resources/bullet.png")
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);

    private final GraphicsHolder graphicsHolder;
    private final int x;
    private int y;
    private final int velocity = 8;

    public Bullet(int x, int y, GraphicsHolder graphicsHolder) {
        this.x = x;
        this.y = y;
        this.graphicsHolder = graphicsHolder;
    }

    public int getY() {
        return y;
    }

    public boolean intersects(Duck duck) {
        if (x + WIDTH > duck.getX() && x < duck.getX() + Duck.WIDTH) {
            if (y < duck.getY() + Duck.HEIGHT && y + HEIGHT > duck.getY()) {
                return true;
            }
        }
        return false;
    }

    public void move() {
        y -= velocity;
    }

    public void draw() {
        graphicsHolder.currentGraphics.drawImage(IMAGE, x, y, null);
    }
}

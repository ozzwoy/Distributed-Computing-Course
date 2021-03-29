package duck_hunt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 710;

    private static Image BACKGROUND_IMAGE;
    private static final int PADDING_TOP = 10;
    private static final int DELAY = 10;
    private static final int DUCKS_NUM = 6;

    private final GraphicsHolder graphicsHolder = new GraphicsHolder();
    private final Duck[] ducks = new Duck[DUCKS_NUM];
    private final CyclicBarrier duckBarrier = new CyclicBarrier(DUCKS_NUM + 1);
    private final CyclicBarrier hunterBarrier = new CyclicBarrier(2);
    private final Hunter hunter = new Hunter(ducks, WIDTH, HEIGHT, graphicsHolder, hunterBarrier);

    public GamePanel() throws IOException {
        addKeyListener(this);
        setFocusable(true);

        BACKGROUND_IMAGE = ImageIO.read(new File("src/main/resources/background.jpg"))
                                  .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);

        Random random = new Random();
        for (int i = 0; i < DUCKS_NUM; i++) {
            ducks[i] = new Duck(WIDTH, random.nextInt(WIDTH - Duck.WIDTH),
                    i * Duck.HEIGHT + PADDING_TOP,
                    random.nextInt(Duck.MAX_VELOCITY) + 1,
                    Arrays.asList(Direction.values()).get(random.nextInt(2)),
                    graphicsHolder,
                    duckBarrier);
            ducks[i].start();
        }
        hunter.start();

        new Timer(DELAY, this).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        graphicsHolder.currentGraphics = g;
        g.drawImage(BACKGROUND_IMAGE, 0, 0, null);

        try {
            //letting hunter draw
            hunterBarrier.await();
            //waiting for hunter to finish drawing
            hunterBarrier.await();

            //letting ducks draw
            duckBarrier.await();
            //waiting for all ducks to finish drawing
            duckBarrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> hunter.moveRight();
            case KeyEvent.VK_LEFT -> hunter.moveLeft();
            case KeyEvent.VK_SPACE -> hunter.shoot();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT) {
            hunter.stopMoving();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class CellularAutomaton extends JFrame {
    private static final int NUM_OF_ITERATIONS = 50;
    private static final int DELAY = 1000;
    private static final int FIELD_WIDTH = 50;
    private static final int FIELD_HEIGHT = 20;
    private static final Field field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
    private static final int NUM_OF_THREADS = 4;
    private static final Thread[] workers = new TransitionThread[NUM_OF_THREADS];
    private static final Semaphore letUIDraw = new Semaphore(1);
    private static final Semaphore letThreadsScan = new Semaphore(1);
    private static final AtomicBoolean shouldStop = new AtomicBoolean(false);

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;
    private static final int CELL_SIZE = 40;
    private static final JButton[][] buttons = new JButton[FIELD_HEIGHT][FIELD_WIDTH];
    private static final JButton startBtn = new JButton("Start");

    public CellularAutomaton() {
        try {
            letUIDraw.acquire();
            letThreadsScan.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CyclicBarrier barrier = new CyclicBarrier(NUM_OF_THREADS, new FieldUpdaterRunnable(field, letUIDraw,
                letThreadsScan, shouldStop, DELAY));

        int areaWidth = NUM_OF_THREADS > 1 ? (FIELD_WIDTH / (NUM_OF_THREADS / 2)) : FIELD_WIDTH;
        int areaHeight = FIELD_HEIGHT / 2;
        for (int i = 0; i < workers.length - 1; i++) {
            if (i % 2 == 0) {
                workers[i] = new TransitionThread(field, areaWidth * (i / 2), 0, areaWidth, areaHeight, barrier);
            } else {
                workers[i] = new TransitionThread(field, areaWidth * (i / 2), areaHeight, areaWidth,
                        FIELD_HEIGHT - areaHeight, barrier);
            }
            workers[i].setDaemon(true);
        }

        int x0 = areaWidth * ((workers.length - 1) / 2);
        if (workers.length % 2 == 0) {
            workers[workers.length - 1] = new TransitionThread(field, x0, areaHeight, FIELD_WIDTH - x0,
                    FIELD_HEIGHT - areaHeight, barrier);
        } else {
            workers[workers.length - 1] = new TransitionThread(field, x0, 0, FIELD_WIDTH - x0,
                    FIELD_HEIGHT, barrier);
        }
        workers[workers.length - 1].setDaemon(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.BLACK);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        startBtn.setFont(new Font("Arial", Font.PLAIN, 40));
        startBtn.addActionListener((ActionEvent e) -> onStartClick());
        mainPanel.add(startBtn);

        JPanel cellsPanel = new JPanel();
        cellsPanel.setLayout(new GridLayout(FIELD_HEIGHT, FIELD_WIDTH, 1, 1));

        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                JButton cell = new JButton();
                cell.setSize(CELL_SIZE, CELL_SIZE);
                cell.setBorderPainted(false);
                cell.setFocusPainted(false);
                cell.setBackground(Color.WHITE);
                cell.addActionListener((ActionEvent e) -> onCellClick((JButton) e.getSource()));
                cellsPanel.add(cell);
                buttons[i][j] = cell;
            }
        }

        mainPanel.add(cellsPanel);
        add(mainPanel);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);

        int iteration = 0;
        while (iteration < NUM_OF_ITERATIONS) {
            try {
                letUIDraw.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(CellularAutomaton::paintCells);
            iteration++;
        }

        shouldStop.set(true);
        letThreadsScan.release();
    }

    private static void onCellClick(JButton source) {
        int rgb = source.getBackground().getRGB();

        if (rgb == Color.WHITE.getRGB()) {
            source.setBackground(Color.BLACK);
        } else if (rgb == Color.BLACK.getRGB()) {
            source.setBackground(Color.RED);
        } else if (rgb == Color.RED.getRGB()) {
            source.setBackground(Color.GREEN);
        } else if (rgb == Color.GREEN.getRGB()) {
            source.setBackground(Color.BLUE);
        } else {
            source.setBackground(Color.WHITE);
        }
    }

    private static void onStartClick() {
        startBtn.setEnabled(false);

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setEnabled(false);
                int rgb = buttons[i][j].getBackground().getRGB();

                if (rgb == Color.BLACK.getRGB()) {
                    field.get(i, j).setColor(Colors.BLACK);
                } else if (rgb == Color.RED.getRGB()) {
                    field.get(i, j).setColor(Colors.RED);
                } else if (rgb == Color.GREEN.getRGB()) {
                    field.get(i, j).setColor(Colors.GREEN);
                } else if (rgb == Color.BLUE.getRGB()) {
                    field.get(i, j).setColor(Colors.BLUE);
                }
            }
        }
        field.update();

        for (Thread thread : workers) {
            thread.start();
        }

        letUIDraw.release();
    }

    private static void paintCells() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                Colors color = field.get(i, j).getColor();

                switch (color) {
                    case NEUTRAL -> buttons[i][j].setBackground(Color.WHITE);
                    case BLACK -> buttons[i][j].setBackground(Color.BLACK);
                    case RED -> buttons[i][j].setBackground(Color.RED);
                    case GREEN -> buttons[i][j].setBackground(Color.GREEN);
                    case BLUE -> buttons[i][j].setBackground(Color.BLUE);
                }

                buttons[i][j].updateUI();
            }
        }
        letThreadsScan.release();
    }

    public static void main(String[] args) {
        new CellularAutomaton();
    }
}

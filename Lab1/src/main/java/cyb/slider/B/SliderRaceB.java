package cyb.slider.B;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SliderRaceB {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JButton startButton1;
    private JSlider slider;
    private JPanel leftPanel;
    private JSpinner leftSpinner;
    private JLabel leftLabel;
    private JLabel messageLabel;
    private JPanel rightPanel;
    private JSpinner rightSpinner;
    private JLabel rightLabel;
    private JButton stopButton1;
    private JButton startButton2;
    private JButton stopButton2;
    private final Thread thread1;
    private final Thread thread2;
    private final AtomicInteger semaphore;

    public SliderRaceB() {
        semaphore = new AtomicInteger(0);
        thread1 = new Thread(new SliderRunnableB(slider, semaphore,
                (Integer value) -> 10));
        thread2 = new Thread(new SliderRunnableB(slider, semaphore,
                (Integer value) -> 90));
        thread1.setDaemon(true);
        thread2.setDaemon(true);

        startButton1.addActionListener(e -> new Thread(() -> {
            if (semaphore.get() == 1) {
                messageLabel.setText("Message: impossible, semaphore is acquired by the other thread.");
                return;
            }
            thread1.setPriority(Thread.MIN_PRIORITY);
            leftSpinner.setValue(Thread.MIN_PRIORITY);
            thread1.start();
            startButton1.setEnabled(false);
            stopButton1.setEnabled(true);
            messageLabel.setText("Message: thread #1 started and acquired semaphore.");
        }).start());

        stopButton1.addActionListener(e -> {
            thread1.interrupt();
            stopButton1.setEnabled(false);
            messageLabel.setText("Message: thread #1 was terminated and released semaphore.");
        });

        startButton2.addActionListener(e -> new Thread(() -> {
            if (semaphore.get() == 1) {
                messageLabel.setText("Message: impossible, semaphore is acquired by the other thread.");
                return;
            }
            thread2.setPriority(Thread.MAX_PRIORITY);
            rightSpinner.setValue(Thread.MAX_PRIORITY);
            thread2.start();
            startButton2.setEnabled(false);
            stopButton2.setEnabled(true);
            messageLabel.setText("Message: thread #2 started and acquired semaphore.");
        }).start());

        stopButton2.addActionListener(e -> {
            thread2.interrupt();
            stopButton2.setEnabled(false);
            messageLabel.setText("Message: thread #2 was terminated and released semaphore.");
        });
    }

    private void createUIComponents() {
        leftSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        rightSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setContentPane(new SliderRaceB().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        frame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }
}

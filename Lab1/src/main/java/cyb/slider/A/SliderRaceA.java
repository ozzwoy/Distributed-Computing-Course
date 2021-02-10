package cyb.slider.A;

import javax.swing.*;
import java.awt.*;

public class SliderRaceA {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JButton startButton;
    private JSlider slider;
    private JPanel leftPanel;
    private JSpinner leftSpinner;
    private JLabel leftLabel;
    private JLabel messageLabel;
    private JPanel rightPanel;
    private JSpinner rightSpinner;
    private JLabel rightLabel;
    Thread thread1;
    Thread thread2;

    public SliderRaceA() {
        thread1 = new Thread(new SliderRunnableA(slider, (Integer value) -> (value < 90) ? value + 1 : value));
        thread2 = new Thread(new SliderRunnableA(slider, (Integer value) -> (value > 10) ? value - 1 : value));
        thread1.setDaemon(true);
        thread2.setDaemon(true);

        startButton.addActionListener(e -> {
            thread1.start();
            thread2.start();
            startButton.setEnabled(false);
            messageLabel.setText("Message: threads started.");
        });
        leftSpinner.addChangeListener(e -> {
            Integer value = (Integer) leftSpinner.getValue();
            thread1.setPriority(value);
        });
        rightSpinner.addChangeListener(e -> {
            Integer value = (Integer) leftSpinner.getValue();
            thread2.setPriority(value);
        });
    }

    private void createUIComponents() {
        leftSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        rightSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setContentPane(new SliderRaceA().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        frame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }
}

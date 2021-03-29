package duck_hunt;

import javax.swing.*;
import java.io.IOException;

public class DuckHunt {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Duck Hunt");
        frame.setSize(GamePanel.WIDTH, GamePanel.HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            frame.add(new GamePanel());
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setVisible(true);
    }
}

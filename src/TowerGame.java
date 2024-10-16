import javax.swing.*;

public class TowerGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tower Game");
        StartScreen startScreen = new StartScreen(frame);
        frame.add(startScreen);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LeaderboardScreen extends JPanel {
    private JButton backButton;
    private JFrame frame;
    private Leaderboard leaderboard;

    public LeaderboardScreen(JFrame frame) {
        this.frame = frame;
        this.leaderboard = new Leaderboard();
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        Font buttonFont = new Font("SansSerif", Font.BOLD, 24);
        backButton = createButton("Back", buttonFont, 300, 500);

        add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(LeaderboardScreen.this);
                StartScreen startScreen = new StartScreen(frame);
                frame.add(startScreen);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    private JButton createButton(String text, Font font, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBounds(x, y, 200, 50);
        button.setBackground(new Color(255, 255, 255));
        button.setForeground(new Color(0, 0, 0));
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("LEADERBOARD", getWidth() / 2 - 100, 50);

        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        int y = 100;
        for (Map.Entry<Integer, String> entry : leaderboard.getScores().entrySet()) {
            g.drawString(entry.getValue() + ": " + entry.getKey(), getWidth() / 2 - 100, y);
            y += 30;
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StartScreen extends JPanel {
    private JButton playButton;
    private JButton leaderboardButton;
    private JButton quitButton;
    private JFrame frame;
    private Image backgroundImage;

    public StartScreen(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/background1.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font buttonFont = new Font("SansSerif", Font.BOLD, 24);

        playButton = createButton("Play", buttonFont, 300, 200);
        leaderboardButton = createButton("Leaderboard", buttonFont, 300, 270);
        quitButton = createButton("Quit", buttonFont, 300, 340);

        add(playButton);
        add(leaderboardButton);
        add(quitButton);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(StartScreen.this);
                TowerGamePanel gamePanel = new TowerGamePanel(frame);
                frame.add(gamePanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(StartScreen.this);
                LeaderboardScreen leaderboardScreen = new LeaderboardScreen(frame);
                frame.add(leaderboardScreen);
                frame.revalidate();
                frame.repaint();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(230, 230, 230));
                button.setForeground(new Color(0, 0, 0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 255, 255));
                button.setForeground(new Color(0, 0, 0));
            }
        });
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.setFont(new Font("SansSerif", Font.BOLD, 60));
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth("Tower Game")) / 2;
        int y = 100;

        g.setColor(Color.BLACK);
        g.drawString("Tower Game", x - 2, y - 2);
        g.drawString("Tower Game", x + 2, y - 2);
        g.drawString("Tower Game", x - 2, y + 2);
        g.drawString("Tower Game", x + 2, y + 2);

        g.setColor(Color.WHITE);
        g.drawString("Tower Game", x, y);
    }
}

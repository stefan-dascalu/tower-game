import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class TowerGamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Block fallingBlock;
    private ArrayList<Block> blocks = new ArrayList<>();
    private int blockSpeed = 5;
    private int fallSpeed = 5;
    private boolean isFalling = false;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private boolean firstBlockDropped = false;
    private Block referenceBlock;
    private BufferedImage backgroundImage;
    private int score = 0;
    private String playerName = "";
    private Leaderboard leaderboard = new Leaderboard();
    private boolean showLeaderboard = false;
    private JButton backButton;

    public TowerGamePanel(JFrame frame) {
        setFocusable(true);

        try {
            backgroundImage = ImageIO.read(new File("src/background.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fallingBlock = new Block(200, 80, 50, 20, Color.WHITE);

        timer = new Timer(16, this);
        timer.start();
        addKeyListener(this);

        backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backButton.setBounds(680, 10, 80, 30);
        backButton.setBackground(new Color(255, 255, 255));
        backButton.setForeground(new Color(0, 0, 0));
        backButton.setFocusPainted(false);
        backButton.setBorder(new RoundedBorder(10));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(true);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(TowerGamePanel.this);
                StartScreen startScreen = new StartScreen(frame);
                frame.add(startScreen);
                frame.revalidate();
                frame.repaint();
            }
        });

        add(backButton);

        playerName = JOptionPane.showInputDialog(this, "Enter your name:");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (showLeaderboard) {
            drawLeaderboard(g);
            return;
        }

        for (Block block : blocks) {
            block.draw(g);
        }

        if (fallingBlock != null) {
            fallingBlock.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("Press SPACE to drop the block", 10, 30);
        g.drawString("Press R to reset the game", 10, 50);
        g.drawString("Score: " + score, 10, getHeight() - 10);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("SansSerif", Font.BOLD, 32));
            g.drawString("YOU LOSE!", getWidth() / 2 - 100, getHeight() / 2);
        } else if (score >= 3) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("SansSerif", Font.BOLD, 32));
            g.drawString("YOU WIN!", getWidth() / 2 - 100, getHeight() / 2);
        }
    }

    private void drawLeaderboard(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("LEADERBOARD", getWidth() / 2 - 100, 50);

        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        int y = 100;
        for (Map.Entry<Integer, String> entry : leaderboard.getScores().entrySet()) {
            g.drawString(entry.getValue() + ": " + entry.getKey(), getWidth() / 2 - 100, y);
            y += 30;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (gameStarted) {
                if (isFalling) {
                    fallingBlock.y += fallSpeed;
                    if (fallingBlock.y + fallingBlock.height > getHeight() || isCollision()) {
                        if (firstBlockDropped && !isAligned()) {
                            gameOver = true;
                            leaderboard.addScore(playerName, score);
                            score = 0;
                        } else {
                            if (isAligned()) {
                                fallingBlock.setColor(Color.GREEN);
                                score++;
                            } else {
                                fallingBlock.setColor(Color.RED);
                            }
                            blocks.add(fallingBlock);
                            fallingBlock = new Block((int) (Math.random() * (getWidth() - 50)), 80, 50, 20, Color.WHITE);
                            if (!firstBlockDropped) {
                                referenceBlock = blocks.get(0);
                                firstBlockDropped = true;
                            }
                            isFalling = false;
                        }
                    }
                } else {
                    fallingBlock.x += blockSpeed;
                    if (fallingBlock.x < 0 || fallingBlock.x + fallingBlock.width > getWidth()) {
                        blockSpeed = -blockSpeed;
                    }
                }
            }
        }
        repaint();
    }

    private boolean isCollision() {
        Rectangle currentBlock = new Rectangle(fallingBlock.x, fallingBlock.y, fallingBlock.width, fallingBlock.height);
        for (Block block : blocks) {
            if (currentBlock.intersects(block)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAligned() {
        if (referenceBlock == null) return false;
        Rectangle currentBlock = new Rectangle(fallingBlock.x, fallingBlock.y, fallingBlock.width, fallingBlock.height);
        Rectangle refBlockRect = new Rectangle(referenceBlock.x, referenceBlock.y, referenceBlock.width, referenceBlock.height);
        return currentBlock.getMaxY() <= refBlockRect.getMaxY() &&
                currentBlock.x < refBlockRect.x + refBlockRect.width &&
                currentBlock.x + currentBlock.width > refBlockRect.x;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameStarted) {
                gameStarted = true;
                fallingBlock = new Block((int) (Math.random() * (getWidth() - fallingBlock.width)), 80, 50, 20, Color.WHITE);
                firstBlockDropped = false;
            } else if (!isFalling && !gameOver) {
                isFalling = true;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            resetGame();
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            showLeaderboard = !showLeaderboard;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void resetGame() {
        blocks.clear();
        fallingBlock = new Block(200, 80, 50, 20, Color.WHITE);
        blockSpeed = 5;
        fallSpeed = 5;
        isFalling = false;
        gameStarted = false;
        gameOver = false;
        firstBlockDropped = false;
        referenceBlock = null;
        score = 0;
        repaint();
    }
}

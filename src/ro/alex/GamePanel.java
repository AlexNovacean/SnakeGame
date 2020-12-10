package ro.alex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final String FONT = "Ink Free";
    static final String SCORE = "Score: ";
    static final String GAME_OVER = "Game Over";

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100;
    final int[] xAxis = new int[GAME_UNITS];
    final int[] yAxis = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(xAxis[i], yAxis[i], UNIT_SIZE, UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.setFont(new Font(FONT, Font.PLAIN, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(SCORE + applesEaten, (SCREEN_WIDTH - metrics.stringWidth(SCORE + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            xAxis[i] = xAxis[i - 1];
            yAxis[i] = yAxis[i - 1];
        }

        switch (direction) {
            case 'U':
                yAxis[0] = yAxis[0] - UNIT_SIZE;
                break;
            case 'D':
                yAxis[0] = yAxis[0] + UNIT_SIZE;
                break;
            case 'L':
                xAxis[0] = xAxis[0] - UNIT_SIZE;
                break;
            case 'R':
                xAxis[0] = xAxis[0] + UNIT_SIZE;
                break;
            default:
                break;
        }
    }

    public void checkApple() {
        if ((xAxis[0] == appleX) && (yAxis[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((xAxis[0] == xAxis[i]) && (yAxis[0] == yAxis[i])) {
                running = false;
                break;
            }
        }
        //check if head touches left border
        if (xAxis[0] < 0) {
            xAxis[0] = SCREEN_WIDTH;
        }

        //check if head touches right border
        if (xAxis[0] > SCREEN_WIDTH) {
            xAxis[0] = 0;
        }
        //check if head touches top border
        if (yAxis[0] < 0) {
            yAxis[0] = SCREEN_HEIGHT;
        }
        //check if head touches bottom border
        if (yAxis[0] > SCREEN_HEIGHT) {
            yAxis[0] = 0;
        }
        if (!running) {
            timer.stop();
        }

    }

    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.RED);
        g.setFont(new Font(FONT, Font.PLAIN, 25));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString(SCORE + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth(SCORE + applesEaten)) / 2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font(FONT, Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString(GAME_OVER, (SCREEN_WIDTH - metrics2.stringWidth(GAME_OVER)) / 2, SCREEN_HEIGHT / 2);
    }

    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

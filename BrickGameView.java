import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BrickGameView extends JFrame {
    private final BrickGameModel model;
    private final BrickGameController controller;

    public BrickGameView(BrickGameModel model) {
        this.model = model;
        this.controller = new BrickGameController(model, this);
        initUI();
    }

    private void initUI() {
        setTitle("Brick Breaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        panel.setPreferredSize(new Dimension(model.getWindowWidth(), model.getWindowHeight()));
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void startGame() {
        controller.start();
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            render(g);
        }

        private void render(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) model.getBall().position.x - 8, (int) model.getBall().position.y - 8, 16, 16);
            g2d.fillRect((int) model.getPaddle().x, getHeight() - 30, (int) model.getPaddle().width, 12);

            int row = 0;
            for (BrickGameModel.Brick brick : model.getBricks()) {
                g2d.setColor(getBrickColor(brick.colorIndex));
                g2d.fillRect((int) brick.x, (int) brick.y, (int) brick.width, (int) brick.height);
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            g2d.drawString("Lives: " + model.getLives(), 10, getHeight() - 10);
            g2d.drawString("Level: " + model.getCurrentLevel(), getWidth() - 100, getHeight() - 10);

            if (model.getGameState() == BrickGameModel.GameState.PAUSED) {
                drawOverlay(g2d, "Paused");
            } else if (model.getGameState() == BrickGameModel.GameState.GAME_OVER) {
                drawOverlay(g2d, "Game Over");
            } else if (model.getGameState() == BrickGameModel.GameState.START) {
                drawOverlay(g2d, "Press SPACE to start");
            }
        }

        private Color getBrickColor(int index) {
            switch (index % 6) {
                case 0: return Color.RED;
                case 1: return Color.ORANGE;
                case 2: return Color.YELLOW;
                case 3: return Color.GREEN;
                case 4: return Color.CYAN;
                default: return Color.MAGENTA;
            }
        }

        private void drawOverlay(Graphics2D g2d, String text) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2);
        }
    }

    public static void main(String[] args) {
        BrickGameModel model = new BrickGameModel();
        BrickGameView view = new BrickGameView(model);
        view.setVisible(true);
        view.startGame();
    }
}

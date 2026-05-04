import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class BrickGameController {
    private final BrickGameModel model;
    private final BrickGameView view;
    private boolean leftPressed;
    private boolean rightPressed;
    private final Timer gameTimer;

    public BrickGameController(BrickGameModel model, BrickGameView view) {
        this.model = model;
        this.view = view;
        this.leftPressed = false;
        this.rightPressed = false;

        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyReleased(e);
            }
        });

        gameTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGameLoop();
            }
        });
    }

    public void start() {
        view.setVisible(true);
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
    }

    private void updateGameLoop() {
        processInput();
        model.updateGame();
        view.repaint();
    }

    private void processInput() {
        if (model.getGameState() == BrickGameModel.GameState.PLAYING) {
            if (leftPressed) {
                model.movePaddle(-5);
            }
            if (rightPressed) {
                model.movePaddle(5);
            }
        }
    }

    private void handleKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_SPACE:
                model.launchBall();
                break;
            case KeyEvent.VK_P:
                model.togglePause();
                break;
            default:
                break;
        }
    }

    private void handleKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            default:
                break;
        }
    }
}

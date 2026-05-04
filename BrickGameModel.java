import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickGameModel {
    public enum GameState {
        START,
        PLAYING,
        PAUSED,
        GAME_OVER,
        LEVEL_COMPLETE
    }

    public static class Ball {
        public Point2D.Double position;
        public Point2D.Double velocity;

        public Ball(double x, double y, double vx, double vy) {
            this.position = new Point2D.Double(x, y);
            this.velocity = new Point2D.Double(vx, vy);
        }
    }

    public static class Paddle {
        public double x;
        public double width;

        public Paddle(double x, double width) {
            this.x = x;
            this.width = width;
        }
    }

    public static class Brick {
        public double x;
        public double y;
        public double width;
        public double height;
        public int colorIndex;

        public Brick(double x, double y, double width, double height, int colorIndex) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.colorIndex = colorIndex;
        }
    }

    private final int windowWidth = 800;
    private final int windowHeight = 600;
    private final int paddleHeight = 12;

    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private int lives;
    private int currentLevel;
    private GameState gameState;
    private final Random random;

    public BrickGameModel() {
        random = new Random();
        lives = 3;
        currentLevel = 1;
        gameState = GameState.START;
        paddle = new Paddle(windowWidth / 2.0 - 60, 120);
        ball = new Ball(windowWidth / 2.0, windowHeight - 40, 0, 0);
        bricks = new ArrayList<>();
        generateLevel(currentLevel);
    }

    public void updateGame() {
        if (gameState != GameState.PLAYING) {
            return;
        }

        ball.position.x += ball.velocity.x;
        ball.position.y += ball.velocity.y;
    }

    public void generateLevel(int level) {
        bricks.clear();
        int rows = 5;
        int columns = 10;
        double brickWidth = (windowWidth - 100.0) / columns;
        double brickHeight = 20;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                double x = 50 + col * brickWidth;
                double y = 50 + row * (brickHeight + 5);
                bricks.add(new Brick(x, y, brickWidth - 5, brickHeight, row % 6));
            }
        }
    }

    public void resetBallToPaddle() {
        ball.position.x = paddle.x + paddle.width / 2.0;
        ball.position.y = windowHeight - 45;
        ball.velocity.x = 0;
        ball.velocity.y = 0;
        gameState = GameState.START;
    }

    public void launchBall() {
        if (gameState == GameState.START) {
            ball.velocity.x = 3;
            ball.velocity.y = -4;
            gameState = GameState.PLAYING;
        }
    }

    public void movePaddle(double dx) {
        paddle.x += dx;
        if (paddle.x < 0) {
            paddle.x = 0;
        }
        if (paddle.x + paddle.width > windowWidth) {
            paddle.x = windowWidth - paddle.width;
        }
    }

    public void togglePause() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
        } else if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING;
        }
    }

    public void loseLife() {
        lives--;
        if (lives <= 0) {
            gameState = GameState.GAME_OVER;
        } else {
            resetBallToPaddle();
        }
    }

    public void checkLevelComplete() {
        if (bricks.isEmpty()) {
            gameState = GameState.LEVEL_COMPLETE;
        }
    }
    
    public void clearBricks() {
        bricks.clear();
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public Ball getBall() {
        return ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Brick> getBricks() {
        return new ArrayList<>(bricks);
    }

    public int getLives() {
        return lives;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public GameState getGameState() {
        return gameState;
    }
}

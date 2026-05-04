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
    private final int ballRadius = 8;
    private final double paddleY = windowHeight - 30;

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
        ball = new Ball(windowWidth / 2.0, paddleY - ballRadius, 0, 0);
        bricks = new ArrayList<>();
        generateLevel(currentLevel);
    }

    public void updateGame() {
        if (gameState != GameState.PLAYING) {
            return;
        }

        double previousX = ball.position.x;
        double previousY = ball.position.y;

        ball.position.x += ball.velocity.x;
        ball.position.y += ball.velocity.y;

        handleWallCollisions();
        handlePaddleCollision(previousY);
        handleBrickCollisions(previousX, previousY);

        if (ball.position.y - ballRadius > windowHeight) {
            loseLife();
        }
    }

    private void handleWallCollisions() {
        if (ball.position.x - ballRadius <= 0) {
            ball.position.x = ballRadius;
            ball.velocity.x = Math.abs(ball.velocity.x);
        } else if (ball.position.x + ballRadius >= windowWidth) {
            ball.position.x = windowWidth - ballRadius;
            ball.velocity.x = -Math.abs(ball.velocity.x);
        }

        if (ball.position.y - ballRadius <= 0) {
            ball.position.y = ballRadius;
            ball.velocity.y = Math.abs(ball.velocity.y);
        }
    }

    private void handlePaddleCollision(double previousY) {
        boolean ballCrossedPaddleTop = previousY + ballRadius <= paddleY
                && ball.position.y + ballRadius >= paddleY;
        boolean ballOverPaddle = ball.position.x >= paddle.x
                && ball.position.x <= paddle.x + paddle.width;

        if (ball.velocity.y > 0 && ballCrossedPaddleTop && ballOverPaddle) {
            ball.position.y = paddleY - ballRadius;
            ball.velocity.y = -Math.abs(ball.velocity.y);

            double paddleCenter = paddle.x + paddle.width / 2.0;
            double hitOffset = (ball.position.x - paddleCenter) / (paddle.width / 2.0);
            ball.velocity.x = hitOffset * 5;
        }
    }

    private void handleBrickCollisions(double previousX, double previousY) {
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);

            if (ballIntersectsBrick(brick)) {
                bricks.remove(i);
                bounceOffBrick(brick, previousX, previousY);
                checkLevelComplete();
                return;
            }
        }
    }

    private boolean ballIntersectsBrick(Brick brick) {
        return ball.position.x + ballRadius >= brick.x
                && ball.position.x - ballRadius <= brick.x + brick.width
                && ball.position.y + ballRadius >= brick.y
                && ball.position.y - ballRadius <= brick.y + brick.height;
    }

    private void bounceOffBrick(Brick brick, double previousX, double previousY) {
        double ballLeft = ball.position.x - ballRadius;
        double ballRight = ball.position.x + ballRadius;
        double ballTop = ball.position.y - ballRadius;
        double ballBottom = ball.position.y + ballRadius;
        double brickRight = brick.x + brick.width;
        double brickBottom = brick.y + brick.height;

        double overlapLeft = ballRight - brick.x;
        double overlapRight = brickRight - ballLeft;
        double overlapTop = ballBottom - brick.y;
        double overlapBottom = brickBottom - ballTop;
        double minHorizontalOverlap = Math.min(overlapLeft, overlapRight);
        double minVerticalOverlap = Math.min(overlapTop, overlapBottom);

        if (minHorizontalOverlap < minVerticalOverlap) {
            bounceHorizontallyOffBrick(brick, previousX);
        } else {
            bounceVerticallyOffBrick(brick, previousY);
        }
    }

    private void bounceHorizontallyOffBrick(Brick brick, double previousX) {
        if (previousX < brick.x) {
            ball.position.x = brick.x - ballRadius;
            ball.velocity.x = -Math.abs(ball.velocity.x);
        } else {
            ball.position.x = brick.x + brick.width + ballRadius;
            ball.velocity.x = Math.abs(ball.velocity.x);
        }
    }

    private void bounceVerticallyOffBrick(Brick brick, double previousY) {
        if (previousY < brick.y) {
            ball.position.y = brick.y - ballRadius;
            ball.velocity.y = -Math.abs(ball.velocity.y);
        } else {
            ball.position.y = brick.y + brick.height + ballRadius;
            ball.velocity.y = Math.abs(ball.velocity.y);
        }
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
        ball.position.y = paddleY - ballRadius;
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

    public int getBallRadius() {
        return ballRadius;
    }

    public double getPaddleY() {
        return paddleY;
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

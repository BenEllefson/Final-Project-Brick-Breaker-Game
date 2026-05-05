public class BrickModelTester {
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Brick Breaker Game Model Tester ===\n");

        testInitialState();
        testBallLaunchBehavior();
        testBallMovement();
        testBrickInteraction();
        testCollisionBehavior();
        testLevelCompletion();
        testLifeLoss();
        testGameOver();

        printSummary();
    }

    private static void testInitialState() {
        System.out.println("Test Suite 1: Initial State");
        BrickGameModel model = new BrickGameModel();

        assertEquals("Initial lives = 3", model.getLives(), 3);
        assertEquals("Initial game state is START", model.getGameState(), BrickGameModel.GameState.START);
        assertEquals("Initial level is 1", model.getCurrentLevel(), 1);

        BrickGameModel.Ball ball = model.getBall();
        BrickGameModel.Paddle paddle = model.getPaddle();
        
        // Ball should be resting on the paddle
        assertEquals("Ball positioned on paddle", ball.position.y, model.getPaddleY() - model.getBallRadius());
        
        // Ball should not be moving
        assertEquals("Ball velocity X is 0 before launch", ball.velocity.x, 0);
        assertEquals("Ball velocity Y is 0 before launch", ball.velocity.y, 0);

        System.out.println();
    }

    private static void testBallLaunchBehavior() {
        System.out.println("Test Suite 2: Ball Launch Behavior");
        BrickGameModel model = new BrickGameModel();

        BrickGameModel.Ball ball = model.getBall();
        double initialVelX = ball.velocity.x;
        double initialVelY = ball.velocity.y;

        assertEquals("Ball velocity is 0 before launch", initialVelX, 0);
        assertEquals("Ball velocity is 0 before launch", initialVelY, 0);

        model.launchBall();

        assertTrue("Ball velocity X != 0 after launch", ball.velocity.x != 0);
        assertTrue("Ball velocity Y != 0 after launch", ball.velocity.y != 0);
        assertEquals("Game state is PLAYING after launch", model.getGameState(), BrickGameModel.GameState.PLAYING);

        System.out.println();
    }

    private static void testBallMovement() {
        System.out.println("Test Suite 3: Ball Movement");
        BrickGameModel model = new BrickGameModel();

        model.launchBall();
        BrickGameModel.Ball ball = model.getBall();

        double initialX = ball.position.x;
        double initialY = ball.position.y;

        model.updateGame();

        double newX = ball.position.x;
        double newY = ball.position.y;

        assertTrue("Ball X position changed after update", Math.abs(newX - initialX) > 0);
        assertTrue("Ball Y position changed after update", Math.abs(newY - initialY) > 0);

        System.out.println("  PASS: Ball moved from (" + initialX + ", " + initialY + ") to (" + newX + ", " + newY + ")");

        System.out.println();
    }

    private static void testBrickInteraction() {
        System.out.println("Test Suite 4: Brick Interaction");
        BrickGameModel model = new BrickGameModel();

        int initialBrickCount = model.getBricks().size();
        assertTrue("Level generated with bricks", initialBrickCount > 0);
        System.out.println("  PASS: Bricks generated on level start (count: " + initialBrickCount + ")");

        // Note: Actual brick removal on collision would require collision detection
        // For now, test that bricks are stored and accessible
        for (BrickGameModel.Brick brick : model.getBricks()) {
            assertTrue("Brick has valid position", brick.x >= 0 && brick.y >= 0);
            assertTrue("Brick has valid size", brick.width > 0 && brick.height > 0);
        }
        System.out.println("  PASS: All bricks have valid properties");

        System.out.println();
    }

    private static void testCollisionBehavior() {
        System.out.println("Test Suite 5: Collision Behavior");
        BrickGameModel model = new BrickGameModel();
        BrickGameModel.Ball ball = model.getBall();

        model.launchBall();
        ball.position.y = model.getBallRadius() + 1;
        ball.velocity.x = 0;
        ball.velocity.y = -4;

        model.updateGame();

        assertEquals("Ball bounces down from top wall", ball.velocity.y, 4);
        assertEquals("Ball stays inside top boundary", ball.position.y, model.getBallRadius());

        model = new BrickGameModel();
        ball = model.getBall();
        BrickGameModel.Brick firstBrick = model.getBricks().get(0);
        int initialBrickCount = model.getBricks().size();

        model.launchBall();
        ball.position.x = firstBrick.x + firstBrick.width / 2.0;
        ball.position.y = firstBrick.y - model.getBallRadius() - 1;
        ball.velocity.x = 0;
        ball.velocity.y = 4;

        model.updateGame();

        assertEquals("Brick removed after ball collision", model.getBricks().size(), initialBrickCount - 1);
        assertEquals("Ball bounces upward after brick collision", ball.velocity.y, -4);
        assertEquals("Ball moves outside brick after collision", ball.position.y, firstBrick.y - model.getBallRadius());

        model = new BrickGameModel();
        ball = model.getBall();
        initialBrickCount = model.getBricks().size();

        model.launchBall();
        for (int i = 0; i < 200 && model.getBricks().size() == initialBrickCount; i++) {
            model.updateGame();
        }

        assertEquals("Launched ball removes a brick during normal play", model.getBricks().size(), initialBrickCount - 1);
        assertTrue("Launched ball bounces back after hitting a brick", ball.velocity.y > 0);

        System.out.println();
    }

    private static void testLevelCompletion() {
        System.out.println("Test Suite 6: Level Completion");
        BrickGameModel model = new BrickGameModel();

        assertEquals("Initial state is START", model.getGameState(), BrickGameModel.GameState.START);

        // Simulate clearing all bricks
        model.clearBricks();

        model.checkLevelComplete();
        assertEquals("Level complete when all bricks removed", model.getGameState(), BrickGameModel.GameState.LEVEL_COMPLETE);

        System.out.println();
    }

    private static void testLifeLoss() {
        System.out.println("Test Suite 7: Life Loss");
        BrickGameModel model = new BrickGameModel();

        int initialLives = model.getLives();
        assertEquals("Initial lives = 3", initialLives, 3);

        model.loseLife();
        assertEquals("Lives decreased to 2", model.getLives(), 2);
        assertEquals("State is START after life loss", model.getGameState(), BrickGameModel.GameState.START);

        BrickGameModel.Ball ball = model.getBall();
        assertTrue("Ball velocity reset to 0", ball.velocity.x == 0 && ball.velocity.y == 0);

        model.loseLife();
        assertEquals("Lives decreased to 1", model.getLives(), 1);

        model.loseLife();
        assertEquals("Lives reached 0", model.getLives(), 0);
        assertEquals("Game state is GAME_OVER", model.getGameState(), BrickGameModel.GameState.GAME_OVER);

        System.out.println();
    }

    private static void testGameOver() {
        System.out.println("Test Suite 8: Game Over");
        BrickGameModel model = new BrickGameModel();

        // Lose all lives
        model.loseLife();
        model.loseLife();
        model.loseLife();

        assertEquals("Game state is GAME_OVER when lives = 0", model.getGameState(), BrickGameModel.GameState.GAME_OVER);
        assertEquals("Lives = 0", model.getLives(), 0);

        System.out.println();
    }

    // Helper Methods
    private static void assertEquals(String testName, Object actual, Object expected) {
        boolean valuesMatch;

        if (actual instanceof Number && expected instanceof Number) {
            valuesMatch = Double.compare(((Number) actual).doubleValue(), ((Number) expected).doubleValue()) == 0;
        } else {
            valuesMatch = actual == null ? expected == null : actual.equals(expected);
        }

        if (valuesMatch) {
            System.out.println("  PASS: " + testName);
            testsPassed++;
        } else {
            System.out.println("  FAIL: " + testName + " (expected: " + expected + ", got: " + actual + ")");
            testsFailed++;
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            testsPassed++;
        } else {
            System.out.println("  FAIL: " + testName);
            testsFailed++;
        }
    }

    private static void printSummary() {
        System.out.println("=== Test Summary ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("Total:  " + (testsPassed + testsFailed));

        if (testsFailed == 0) {
            System.out.println("\n✓ All tests passed!");
        } else {
            System.out.println("\n✗ " + testsFailed + " test(s) failed.");
        }
    }
}

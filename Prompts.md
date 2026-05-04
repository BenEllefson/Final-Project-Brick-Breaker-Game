Interview For Spec Design:

  I want to build a Brick Breaker style in Java with Swing using MVC. Before we write any code, interview me.
  Ask me one question at a time about gameplay, controls, win and loss conditions, and what should be on the screen.
  After about 8 questions, summarize what I told you as a one-page spec organized by Model, View, and Controller

  What kind of gameplay feel do you want?
  I would like a more classic style with a simple paddle and no real extras

  How should the player control the paddle?
  Via the keyboard and it should be smooth/continuous

  How should the ball behave when the game starts?
  It should sit on the paddle until the player presses space bar

  How should the ball bounce off the paddle?
  The angle will depend on where it hits the paddle, most likely split into thirds

  What should happen with the bricks?
  I would like the layout to be randomly generated each level 

  What are the win and loss conditions?
  The win is when all bricks are destroyed the game is over or next level.
  A loss will be when the ball goes past the paddle and you will get three lives

  What information and elements should be visible on the screen during gameplay?
  The things tracked on the screen will be remaining lives, current level and a pause indicator

  What should the visual style and layout look like?
  I would like the game to be about little less than window size, very classic and simple colors with
  white for the paddle, a dark background and multicolored bricks. 

The AI then produced the one page spec that is located in the REEADME.md document

Spec Dump Prompt:

User: I am building a Brick Breaker game in Java with Swing using MVC. Here is my spec:
Model (Game State & Logic) Core State: Ball (position, velocity, direction) Paddle (position, width) Bricks (2D grid or list, randomized each level) Lives (starts at 3) Current Level Game State: START (ball waiting on paddle) PLAYING PAUSED GAME_OVER LEVEL_COMPLETE Game Rules: Ball starts resting on paddle until spacebar is pressed Ball bounces: Off walls (standard reflection) Off paddle (angle depends on hit position: split into thirds) Bricks: All bricks break in one hit Layout is randomly generated each level Win Condition: All bricks destroyed → advance to next level or end game Loss Condition: Ball falls below paddle → lose a life Game ends when lives reach 0

View (Rendering / UI) Window & Layout: Game area slightly smaller than full window Fixed size preferred (simple layout) Visual Style: Background: dark (black or near-black) Paddle: solid white rectangle Ball: simple white circle Bricks: multicolored rectangles (varied per row or random) Displayed UI Elements: Remaining Lives Current Level Pause Indicator (e.g., “Paused” overlay text) Screens / States: Start state: ball sitting on paddle Pause overlay (text centered) Game Over screen (simple message) Optional level transition message

Controller (Input & Game Flow) Input Handling: Keyboard-based controls: Left Arrow → move paddle left (continuous) Right Arrow → move paddle right (continuous) S pacebar → launch ball (from START state) Pause key (e.g., P) → toggle pause Movement Behavior: Paddle movement is smooth and continuous while key is held Ball moves continuously once launched Game Loop Responsibilities: Update ball position Handle collisions: Wall collisions Paddle collision (angle based on thirds) Brick collisions (remove brick) Check win/loss conditions Trigger state transitions: Life lost → reset ball to paddle Level complete → generate new bricks Game over → stop gameplay

Generate three class shells — BrickGameModel.java, BrickGameView.java, BrickGameController.java — with method stubs based on this design. BrickGameModel must not import any Swing classes. The program should compile and open a blank window.

The AI gave me three seperate class shells named what were prompted and put the respective objects and functions in each class.

Prompt to create the Model Tester:
Create a Java file called BrickModelTester.java that tests the BrickGameModel class for my Brick Breaker MVC project.

You already have access to the project spec and the existing BrickGameModel implementation. Do not redefine the model just use it as it is.
Do NOT use Swing or any UI components
Do NOT use JUnit; use a main method with simple assertions or printed PASS/FAIL messages
This file should be runnable from the terminal

Test the GameModel against the spec by simulating gameplay scenarios.
Include tests for:
1. Initial State
   -Lives start at 3
   -Game state is START
   -Ball is positioned on the paddle
   -Ball is not moving

2. Ball Launch Behavior
   - Ball does not move before launch
   - After calling launch, the ball begins moving

3. Ball Movement and Collisions
   - Ball bounces off walls correctly
   - Ball bounces off paddle
   - Bounce angle changes depending on hit location (left, middle, right thirds of paddle)

4. Brick Interaction
   - Bricks are removed after one hit
   - Brick count decreases appropriately

5. Level Completion
   - When all bricks are removed, the model enters LEVEL_COMPLETE or advances level

6. Life Loss
   - When the ball falls below the paddle, lives decrease by 1
   - Ball resets to paddle after life loss

7. Game Over
   - When lives reach 0, the model enters GAME_OVER state

Implementation Notes:
- Use helper methods inside ModelTester to keep tests organized
- Print clear output like:
  PASS: Initial lives = 3
  FAIL: Ball moved before launch
- If necessary, call update() repeatedly to simulate game ticks
- If method names differ slightly, adapt to the existing GameModel API

Keep the code simple, readable, and focused on validating the model logic only.

# Final-Project-Brick-Breaker-Game

Model (Game State & Logic)
Core State:
Ball (position, velocity, direction)
Paddle (position, width)
Bricks (2D grid or list, randomized each level)
Lives (starts at 3)
Current Level
Game State:
  START (ball waiting on paddle)
  PLAYING
  PAUSED
  GAME_OVER
  LEVEL_COMPLETE
Game Rules:
Ball starts resting on paddle until spacebar is pressed
Ball bounces:
  Off walls (standard reflection)
  Off paddle (angle depends on hit position: split into thirds)
Bricks:
  All bricks break in one hit
  Layout is randomly generated each level
Win Condition:
  All bricks destroyed → advance to next level or end game
Loss Condition:
  Ball falls below paddle → lose a life
  Game ends when lives reach 0

View (Rendering / UI)
Window & Layout:
Game area slightly smaller than full window
Fixed size preferred (simple layout)
Visual Style:
  Background: dark (black or near-black)
  Paddle: solid white rectangle
  Ball: simple white circle
Bricks: multicolored rectangles (varied per row or random)
Displayed UI Elements:
  Remaining Lives
  Current Level
  Pause Indicator (e.g., “Paused” overlay text)
  Screens / States:
Start state: ball sitting on paddle
Pause overlay (text centered)
Game Over screen (simple message)
Optional level transition message

Controller (Input & Game Flow)
Input Handling:
Keyboard-based controls:
  Left Arrow → move paddle left (continuous)
  Right Arrow → move paddle right (continuous)
S  pacebar → launch ball (from START state)
  Pause key (e.g., P) → toggle pause
Movement Behavior:
  Paddle movement is smooth and continuous while key is held
  Ball moves continuously once launched
Game Loop Responsibilities:
  Update ball position
Handle collisions:
  Wall collisions
  Paddle collision (angle based on thirds)
  Brick collisions (remove brick)
Check win/loss conditions
Trigger state transitions:
  Life lost → reset ball to paddle
  Level complete → generate new bricks
  Game over → stop gameplay

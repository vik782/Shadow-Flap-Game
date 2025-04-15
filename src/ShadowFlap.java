import bagel.AbstractGame;
import bagel.Font;
import bagel.Window;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * ShadowFlap class for SWEN20003 Project 2, Semester 2, 2021
 *
 * @author: Vincent Kurniawan (1110090)
 */
public class ShadowFlap extends AbstractGame {

    // exit constant
    private final int EXIT = -1;
    // constants related to font type, size and gaps
    private final int FONT_SIZE = 48;
    private final int TEXT_GAP_1 = 75;
    private final int TEXT_GAP_2 = 68;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    // constants related to timescale logic
    private final double MIN_TIMESCALE = 0.5;
    private final double MAX_TIMESCALE = 5;
    // duration of leveling-up screen, adjusted for playability in 60FPS devices
    private final int MAX_LEVEL_UP_FRAME = 150;
    // constants related score logic
    private final int LOSE_SCORE_L0 = 3;
    private final int LOSE_SCORE_L1 = 6;
    private final int LEVEL_UP_SCORE = 10;
    private final int WIN_SCORE = 100;
    private final Point SCORE_POINT = new Point(100.0, 100.0);

    // tells state of game
    private boolean startStage;
    private boolean firstStage;
    private boolean secondStage;
    private boolean stillPlaying;
    // keeps track of frame count
    private int currFrame;
    private int levelUpFrame;
    // keeps track of current timescale and changes accordingly
    private double timescale;
    // keeps track of current score and changes accordingly
    private int score;
    // the 2 levels which the game has
    private Level level0, level1;

    /**
     * A constructor which creates a new instance of ShadowFlap Game
     */
    public ShadowFlap() {
        // initially set state of game to be on "welcome screen"
        startStage = true;
        firstStage = true;
        secondStage = false;
        stillPlaying = true;
        // set variables to 0
        currFrame = 0;
        levelUpFrame = 0;
        score = 0;
        // initial timescale is 1
        timescale = MIN_TIMESCALE;
        // initiate new instances of level0 and level1
        level0 = new Level0();
        level0.resetPipes();
        level1 = new Level1();
        level1.resetPipes();

        renderStartScreen();
    }

    /**
     * The entry point for the program.
     *
     * @param args The command-line arguments inputted (Optional)
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * A method to render a message on the screen
     *
     * @param message The message to be rendered on the screen
     * @param extraGap The Y-position gap at which message is rendered
     */
    public void printMessage(String message, double extraGap) {
        extraGap += TEXT_GAP_1;
        double messageWidth = (Window.getWidth() / 2.0) - (FONT.getWidth(message) / 2.0);
        double messageHeight = (Window.getHeight() / 2.0) + extraGap + (FONT_SIZE / 2.0);
        FONT.drawString(message, messageWidth, messageHeight);
    }

    /**
     * A method to render Welcome Screen when game is first run.
     * Calls printMessage() method to render welcome message of level0.
     */
    public void renderStartScreen(){
        level0.drawBackground();
        printMessage("PRESS SPACE TO START", -TEXT_GAP_1);
    }

    /**
     * A method to render Win Screen when game has been won.
     * Calls printMessage() method to render win message.
     */
    public void renderWinScreen(){
        level1.drawBackground();
        printMessage("CONGRATULATIONS!", -TEXT_GAP_1);
    }

    /**
     * A method to render Level-Up Screen when thresh-hold for leveling up was achieved.
     * Calls printMessage() method to render level-up message and final score.
     */
    public void renderLevelUpScreen(){
        level0.drawBackground();
        printMessage("LEVEL-UP!", -TEXT_GAP_1);
    }

    /**
     * A method to render Welcome Screen on the start of level1.
     * Calls printMessage() method to render welcome message of level1.
     */
    public void renderStartScreen2(){
        level1.drawBackground();
        printMessage("PRESS SPACE TO START", -TEXT_GAP_1);
        printMessage("PRESS 'S' TO SHOOT", TEXT_GAP_2 - TEXT_GAP_1);
    }

    /**
     * A method to render lose message when game has been lost in either level0 or level1.
     * Calls printMessage() method to render lose message and final score.
     */
    public void renderLoseScreen(){
        printMessage("GAME OVER", -TEXT_GAP_1);
        printMessage("FINAL SCORE: " + score, 0);
        printMessage("Press R to Restart", TEXT_GAP_1);
    }

    /**
     * A method to render score status when game is being played.
     */
    public void renderScoreStatus(){
        FONT.drawString("SCORE: " + score, SCORE_POINT.x, SCORE_POINT.y);
    }

    /**
     * A method to resets game's states upon leveling up.
     */
    public void levelUp() {
        firstStage = false;
        timescale = 0.7;
        currFrame = 0;
    }

    /**
     * A method to adjust timescale when game is being played.
     *
     * @param input The key-input of which the user pressed.
     */
    public void adjustTimescale(Input input){
        // increases timescale by 1 if 'L' was pressed
        if (input.wasPressed(Keys.L)) {
            if (timescale + 1 <= MAX_TIMESCALE) {
                timescale += 1;
            }
        }
        // decreases timescale by 1 if 'K' was pressed
        if (input.wasPressed(Keys.K)) {
            if (timescale - 1 >= MIN_TIMESCALE) {
                timescale -= 1;
            }
        }
    }

    /**
     * Resets the game state to start from level0.
     */
    public void resetGame() {
        // Reset score
        score = 0;

        // Reset frames and timers
        currFrame = 0;
        levelUpFrame = 0;

        // Reset game states
        startStage = true;
        firstStage = true;
        secondStage = false;
        stillPlaying = true;

        // Reset timescale
        timescale = MIN_TIMESCALE;

        level0 = new Level0();
        level0.resetPipes();
        level1 = new Level1();
        level1.resetPipes();

        renderStartScreen();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     *
     * @param input The key-input of which the user pressed.
     */
    @Override
    public void update(Input input) {

        // Example: If the game is over and the user presses "R", reset the game
        if (!stillPlaying && input.wasPressed(Keys.R)) {
            resetGame();
        }

        // exits game when 'ESCAPE' is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            System.exit(EXIT);
        }

        // Example: If the game is over and the user presses "R", reset the game
        else if (!stillPlaying && input.wasPressed(Keys.R)) {
            resetGame();
        }

        // start of game, welcome screen is rendered
        else if (input.isUp(Keys.SPACE) && startStage) {
            renderStartScreen();
        }

        // game is being played
        else {
            startStage = false;
            // lose score thresh-hold in level0 is achieved
            if ( level0.getLoseCount() == LOSE_SCORE_L0) {
                firstStage = false;
                stillPlaying = false;
                level0.drawBackground();
                renderLoseScreen();
            }

            // lose score thresh-hold in level1 is achieved
            if ( level1.getLoseCount() == LOSE_SCORE_L1) {
                secondStage = false;
                stillPlaying = false;
                level1.drawBackground();
                renderLoseScreen();
            }

            // win score thresh-hold in level1 is achieved
            if (score == WIN_SCORE) {
                secondStage = false;
                stillPlaying = false;
                renderWinScreen();
            }

            // playing in level0
            if ( (firstStage) && (stillPlaying) ) {
                // adjusts timescale according to input
                adjustTimescale(input);

                currFrame += 1;
                // level0 is operating
                level0.implementLevel(input, currFrame, timescale);
                renderScoreStatus();
                if (level0.hasScored()) {
                    // earns a score when bird passes through a pipe set
                    score += 1;
                }
            }

            // level-up score thresh-hold is achieved
            if ( (score == LEVEL_UP_SCORE) && (!secondStage) && (stillPlaying) ) {
                levelUp();
                if (levelUpFrame <= MAX_LEVEL_UP_FRAME) {
                    levelUpFrame++;
                    renderLevelUpScreen();
                }
                else {
                    if (input.isUp(Keys.SPACE) && !secondStage) {
                        // welcome screen of level1 is rendered
                        renderStartScreen2();
                    }
                    else {
                        // start of level1
                        secondStage = true;
                    }
                }
            }

            // playing in level1
            if ( (secondStage) && (stillPlaying) ) {
                // adjusts timescale according to input
                adjustTimescale(input);

                currFrame += 1;
                // level1 is operating
                level1.implementLevel(input, currFrame, timescale);
                renderScoreStatus();
                if (level1.hasScored() || ((Level1)level1).getPipeSetShotThrough()) {
                    // earns a score when bird passes through a pipe set
                    // or appropriate weapon destroys an appropriate pipe set
                    score += 1;
                }
            }
        }
    }
}
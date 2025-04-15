import bagel.Image;
import bagel.Input;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstract class of Level which implements interface Operational.
 */
public abstract class Level {
    // images of hearts representing full life and no life
    private final Image FULL_LIFE = new Image ("res/level/fullLife.png");
    private final Image NO_LIFE = new Image ("res/level/noLife.png");

    // constants representing heart's spawn position
    private final int LIFE_BAR_X = 100;
    private final int LIFE_BAR_Y = 15;
    private final int LIFE_BAR_GAP = 50;

    // constants representing pipe set's spawn position
    private final int HIGH_GAP = 100;
    private final int MID_GAP = 300;
    private final int LOW_GAP = 500;
    private final double SPEED_CHANGE = 1 + 0.50;

    // constant used to indicate new pipe set insertion
    private final int PIPE_RENDER_FRAME = 100;

    // each level has its own bird, array of pipe sets, and a lost count
    private Bird bird;
    private ArrayList<PipeSet> pipeArray;
    private int loseCount;

    /**
     * A constructor which initialises an instance of a subclass of a Level.
     * Level abstract class itself cannot be instantiated.
     *
     * @param bird The Bird of the level.
     * @param pipeArray The ArrayList of pipe-sets of the level.
     */
    public Level(Bird bird, ArrayList<PipeSet> pipeArray){
        this.bird = bird;
        this.pipeArray = pipeArray;
        loseCount = 0;
    }

    /**
     * A method which returns an Image of a full-life heart.
     *
     * @return The Image representing a full-life heart.
     */
    public Image getFullLife(){return FULL_LIFE;}

    /**
     * A method which returns an Image of a no-life heart.
     *
     * @return The Image representing a no-life heart.
     */
    public Image getNoLife(){return NO_LIFE;}

    /**
     * A method which returns an int representing life bar's X-position.
     *
     * @return The int representing life bar's X-position.
     */
    public int getLifeBarX(){
        return LIFE_BAR_X;
    }

    /**
     * A method which returns an int representing life bar's Y-position.
     *
     * @return The int representing life bar's Y-position.
     */
    public int getLifeBarY(){
        return LIFE_BAR_Y;
    }

    /**
     * A method which returns an int representing gap of hearts within the life bar.
     *
     * @return The int representing gap of hearts within the life bar.
     */
    public int getLifeBarGap(){
        return LIFE_BAR_GAP;
    }

    /**
     * A method which returns a double representing level's lost count.
     *
     * @return The double representing level's lost count.
     */
    public int getLoseCount(){return loseCount;}

    /**
     * A method which returns an int representing pipe set's render frame.
     *
     * @return The int representing pipe set's render frame.
     */
    public int getPipeRenderFrame(){return PIPE_RENDER_FRAME;}

    /**
     * A method which returns an int representing pipe set's high-gap constant.
     *
     * @return The int representing high-gap constant.
     */
    public int getHighGap(){return HIGH_GAP;}

    /**
     * A method which returns an int representing pipe set's mid-gap constant.
     *
     * @return The int representing mid-gap constant.
     */
    public int getMidGap(){return MID_GAP;}

    /**
     * A method which returns an int representing pipe set's low-gap constant.
     *
     * @return The int representing low-gap constant.
     */
    public int getLowGap(){return LOW_GAP;}

    /**
     * A method which increments the current lose count of the current level.
     *
     * @param loseCount The int representing update of lose count.
     */
    public void setLoseCount(int loseCount){
        this.loseCount += loseCount;
    }

    /**
     * A method which re-instantiate a bird within a level.
     *
     * @param bird The Bird representing new bird of the level.
     */
    public void ResetBird(Bird bird) {this.bird = bird;}

    /**
     * A method which returns a boolean representing state of bird-pipe collision.
     *
     * @param pipeSet The PipeSet being iterated.
     * @return The boolean representing state whether a pipe set has collided with a bird or not.
     */
    public boolean birdPipeCollision(PipeSet pipeSet) {
        // updating Rectangle box of the pipe sets and bird
        pipeSet.updateBottomPipeBox();
        pipeSet.updateTopPipeBox();
        bird.updateBirdBox();

        // bird-pipe collision logic
        return (pipeSet.getTopBox().intersects(bird.getBirdBox())) ||
                        (pipeSet.getBottomBox().intersects(bird.getBirdBox()));
    }

    /**
     * A method which returns a boolean representing state of score.
     *
     * @return The boolean representing state whether a score is achieved or not.
     */
    public boolean hasScored() {
        // iterating through pipeArray
        for (PipeSet pipeSet : pipeArray) {
            pipeSet.updateBottomPipeBox();
            pipeSet.updateTopPipeBox();

            // scoring logic
            if ( (bird.getBirdX() > pipeSet.getBottomBox().right()) && (!pipeSet.getPassedThrough()) ) {
                // bird only scores 1 point for each pipe set it passes through
                pipeSet.setPassedThrough(true);
                return true;
            }
        }
        return false;
    }

    /**
     * A method which removes a pipe set once it leaves the left border.
     */
    public void removePipe(){
        Iterator<PipeSet> iterator = pipeArray.iterator();
        while (iterator.hasNext()){
            PipeSet pipeSet = iterator.next();
            pipeSet.updateTopPipeBox();
            pipeSet.updateBottomPipeBox();

            if ( pipeSet.getBottomBox().right() < 0) {
                iterator.remove();
            }
        }
    }

    /**
     * A method which returns a double representing speed of movement's multiplier after adjusted by timescale.
     *
     * @param timescale The int representing current timescale.
     * @return The double representing speed of movement's multiplier after adjusted by timescale.
     */
    public double moveSpeedChange(double timescale){
        return Math.pow(SPEED_CHANGE, timescale - 1);
    }

    /**
     * An abstract method at which subclasses render their background image.
     */
    public abstract void drawBackground();

    /**
     * An abstract method to clear the pipes
     *
     */
    public void resetPipes(){}

    /**
     * An abstract method at which subclasses create a new instance of a subclass of a PipeSet.
     *
     * @return The PipeSet which was randomly made.
     */
    public abstract PipeSet makeRandomPipe();

    /**
     * An abstract method at which subclasses insert a new instance of a subclass of a PipeSet into the pipe set array.
     *
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public abstract void insertPipe(int currFrame, double timescale);

    /**
     * An abstract method at which subclasses spawn a pipe set within the array.
     *
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public abstract void spawnPipes(int currFrame, double timescale);

    /**
     * An abstract method at which subclasses implement respective logic to remove a heart from the life-bar.
     */
    public abstract void loseLife();

    /**
     * An abstract method at which classes implement to operate a level.
     *
     * @param input The key-input of which the user pressed.
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
     public abstract void implementLevel(Input input, int currFrame, double timescale);

    /**
     * An abstract method at which classes implement respective logic to initialize life-bar.
     */
    public abstract void initializeLifeBar();

    /**
     * An abstract method at which classes implement respective logic to spawn life-bar.
     */
    public abstract void spawnLifeBar();

}

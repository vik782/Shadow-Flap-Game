import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

/**
 * Child class Level0 which extends from Level.
 */
public class Level0 extends Level {
    // level0 has a birdL0 and pipe set array
    private static Bird birdL0 = new BirdL0();
    private static ArrayList<PipeSet>pipeArrayL0 = new ArrayList<>();

    // background image of level0
    private final Image BACKGROUND_IMAGE_L0 = new Image("res/level-0/background.png");

    // max lives for level0
    private final int MAX_LIFE_L0 = 3;

    // life-bar for level0
    private Image[] lifeBarL0;

    // the 3 pipe types available for level0
    private final int[] PIPE_TYPE = {getHighGap() - getMidGap(), 0, getLowGap() - getMidGap()};

    // random index gap for level0
    private int idx;
    private final Random RANDOM = new Random();

    /**
     * A constructor which creates a new instance of a Level0.
     * Calls the constructor of its parent class using super().
     */
    public Level0(){
        super(birdL0, pipeArrayL0);
        initializeLifeBar();
    }

    /**
     * A method to initialize life-bar of level0 with images of full-life.
     */
    public void initializeLifeBar(){
        lifeBarL0 = new Image[MAX_LIFE_L0];
        for (int i=0; i<MAX_LIFE_L0; i++) {
            lifeBarL0[i] = getFullLife();
        }
    }

    /**
     * A method to render background image of level0.
     */
    public void drawBackground(){
        BACKGROUND_IMAGE_L0.drawFromTopLeft(0,0);
    }

    /**
     * A method to render life bar with its images of hearts.
     */
    public void spawnLifeBar() {
        int pos = getLifeBarX();
        for (int i=0; i<MAX_LIFE_L0; i++) {
            lifeBarL0[i].drawFromTopLeft(pos, getLifeBarY());
            pos+=getLifeBarGap();
        }
    }

    /**
     * A method to replace a full-life image with a no-life image in the life bar.
     */
    public void loseLife() {
        lifeBarL0[MAX_LIFE_L0 - getLoseCount()] = getNoLife();
    }

    /**
     * A method to generate new instance of a PlasticPipe set in level0.
     *
     * @return The PipeSet which was randomly made.
     */
    public PipeSet makeRandomPipe(){
        PipeSet pipes = new PlasticPipe();

        // choose 1 out of the 3 pipe types available
        // types meaning Y-position of pipes, not subclass of PipeSet
        idx = RANDOM.nextInt(PIPE_TYPE.length);
        pipes.setTopPipeY(pipes.getTopPipeY() + PIPE_TYPE[idx]);
        pipes.setBottomPipeY(pipes.getBottomPipeY() + PIPE_TYPE[idx]);

        return pipes;
    }

    /**
     * A method to insert pipe set into level0's pipe array.
     *
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public void insertPipe(int currFrame, double timescale) {
        // the rate at which pipes are inserted into the pipe array is influenced by timescale
        double frame = (double)getPipeRenderFrame() / moveSpeedChange(timescale);

        if ( currFrame % Math.ceil(frame) == 0) {
            // insert pipe set into pipe array
            pipeArrayL0.add(makeRandomPipe());
        }
    }

    /**
     * A method to clear the pipes
     *
     */
    public void resetPipes(){
        pipeArrayL0.clear();
    }

    /**
     * A method to insert pipe set into level0's pipe array.
     *
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public void spawnPipes(int currFrame, double timescale){
        // iterating through pipe set array
        Iterator<PipeSet> iterator = pipeArrayL0.iterator();
        while(iterator.hasNext()){
            PipeSet pipeSet = iterator.next();

            // checking for bird-pipe collision
            if (birdPipeCollision(pipeSet)){
                // pipe set removed, no longer being spawned
                iterator.remove();
                // loses a life
                setLoseCount(1);
                // replaces a full-life with a no-life
                loseLife();
            }
            else {
                // pipe set is rendered on screen and continues to move
                pipeSet.spawn(currFrame);
                pipeSet.moveScaled(timescale);
            }
        }
    }

    /**
     * A method which operates the level during gameplay.
     * Logic of level0's gameplay is implemented here.
     *
     * @param input The key-input of which the user pressed.
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public void implementLevel(Input input, int currFrame, double timescale) {
        drawBackground();

        // triggers birdL0 to fly once SPACE key is pressed
        if (input.wasPressed(Keys.SPACE)) {
            birdL0.birdFly();
        }
        // triggers birdL0 to start falling
        birdL0.birdFall();
        // spawns image of birdL0
        birdL0.spawn(currFrame);

        // make, insert, move and spawn pipe sets according to frame count and timescale
        insertPipe(currFrame, timescale);
        spawnPipes(currFrame, timescale);

        // spawn life bar of level0
        spawnLifeBar();


        if ( birdL0.outOfBound() ) {
            // bird has left top/bottom border, loses a life
            setLoseCount(1);
            loseLife();

            // re-instantiate birdL0
            birdL0 = new BirdL0();
            ResetBird(birdL0);
        }

        // once pipe set leaves left border, pipe set gets removed
        removePipe();

    }

}

import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Abstract class of PipeSet which implements interface TimeScalable.
 * A PipeSet contains a set of top and bottom pipes.
 */
public abstract class PipeSet implements TimeScalable {
    // constants related to pipe set's spawn, gap, step size and image
    private final Image PIPE_IMAGE;
    private final double INVERTED_ANGLE = 180;
    private final double PIPE_GAP = 168;
    // Step size of pipe set is changed to improve playability in 60FPS devices
    private final double PIPE_STEP_SIZE = 5;

    // variable relating to pipe set's attributes
    private double pipeSpeed;
    private double pipeX;
    private double topPipeY;
    private double bottomPipeY;
    private Rectangle topPipeBox;
    private Rectangle bottomPipeBox;

    // boolean indicating if bird has passed through a pipe set
    private boolean passedThrough;

    // boolean indicating pipe set's removal
    private boolean remove;

    /**
     * A constructor which initialises an instance of a subclass of a PipeSet.
     * PipeSet abstract class itself cannot be instantiated.
     *
     * @param pipeImage The image of the pipe.
     */
    public PipeSet(Image pipeImage) {
        this.PIPE_IMAGE = pipeImage;
        // initial position of pipes is initially set to be rendered in the middle of the screen
        // but position will be altered when pipes are spawned within methods of the respective Level sub-classes
        pipeX = (Window.getWidth());
        topPipeY = -PIPE_GAP/2.0;
        bottomPipeY = Window.getHeight() + PIPE_GAP / 2.0;
        topPipeBox = pipeImage.getBoundingBoxAt(new Point(pipeX, topPipeY));
        bottomPipeBox = pipeImage.getBoundingBoxAt(new Point(pipeX, bottomPipeY));
        pipeSpeed = PIPE_STEP_SIZE;
        passedThrough = false;
        remove = false;

    }

    /**
     * A method which returns a double representing pipe set's X-position.
     *
     * @return The double representing pipe set's X-position.
     */
    public double getPipeX(){return pipeX;}

    /**
     * A method which returns a double representing top pipe's Y-position.
     *
     * @return The double representing top pipe's Y-position.
     */
    public double getTopPipeY(){return topPipeY;}

    /**
     * A method which returns a double representing bottom pipe's Y-position.
     *
     * @return The double representing bottom pipe's Y-position.
     */
    public double getBottomPipeY(){return bottomPipeY;}

    /**
     * A method which returns a double representing pipe's height.
     *
     * @return The double representing pipe's height.
     */
    public double getPipeHeight(){return PIPE_IMAGE.getHeight();}

    /**
     * A method which returns a double representing angle measured in degrees.
     *
     * @return The double representing angle measured in degrees.
     */
    public double getInvertedAngle(){return INVERTED_ANGLE;}

    /**
     * A method which returns a boolean representing state of pipe set of being passed-through.
     *
     * @return The boolean representing state whether pipe set has been passed-through or not by the bird.
     */
    public boolean getPassedThrough() {return passedThrough;}

    /**
     * A method which returns a boolean representing state of pipe set of being removed.
     *
     * @return The boolean representing state whether pipe set should be removed or not.
     */
    public boolean getRemove(){return remove;}

    /**
     * A method which returns a Rectangle representing top pipe's Rectangle box.
     *
     * @return The Rectangle representing top pipe's Rectangle box.
     */
    public Rectangle getTopBox() {
        return topPipeBox;
    }

    /**
     * A method which returns a Rectangle representing bottom pipe's Rectangle box.
     *
     * @return The Rectangle representing bottom pipe's Rectangle box.
     */
    public Rectangle getBottomBox() {
        return bottomPipeBox;
    }

    /**
     * A method which sets the top pipe's Y-position.
     *
     * @param topPipeY The double representing top pipe's new Y-Position.
     */
    public void setTopPipeY(double topPipeY) {this.topPipeY = topPipeY;}

    /**
     * A method which sets the bottom pipe's Y-position.
     *
     * @param bottomPipeY The double representing bottom pipe's new Y-Position.
     */
    public void setBottomPipeY(double bottomPipeY) {this.bottomPipeY = bottomPipeY;}

    /**
     * A method which sets the pipe set's state of being passed-through.
     *
     * @param passedThrough The boolean representing pipe set's new state of being passed-through.
     */
    public void setPassedThrough(boolean passedThrough) {this.passedThrough = passedThrough;}


    /**
     * A method which sets the pipe set's state of being removed.
     *
     * @param remove The boolean representing pipe set's new state of being removed.
     */
    public void setRemove(boolean remove){this.remove = remove;}


    /**
     * A method which updates top pipe's Rectangle box.
     */
    public void updateTopPipeBox(){topPipeBox = PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, topPipeY));}

    /**
     * A method which updates bottom pipe's Rectangle box.
     */
    public void updateBottomPipeBox(){bottomPipeBox = PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, bottomPipeY));}

    /**
     * A method which updates the pipe set's X-position during gameplay.
     * PipeSet moves towards left border.
     *
     * @param timescale The int representing current timescale.
     */
    public void moveScaled(double timescale) {
        // Movement is influenced by timescale
        pipeSpeed = PIPE_STEP_SIZE * (Math.pow(speedMultiplier, timescale - 1));
        pipeX -= pipeSpeed;
    }

    /**
     * An abstract method at which subclasses implement respective logic of rendering
     * top and bottom pipe image to screen.
     *
     * @param currFrame The int representing the current frame of the game.
     */
    public abstract void spawn(int currFrame);
}

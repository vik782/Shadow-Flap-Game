import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Child class SteelPipe which extends from PipeSet.
 */
public class SteelPipe extends PipeSet{
    // images of steel pipe and flame
    private static final Image STEEL_PIPE_PNG = new Image("res/level-1/steelPipe.png");
    private static final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final DrawOptions DRAW_OPTIONS = new DrawOptions();

    // constants related to flame's logic of being rendered on screen and its duration
    // values are altered to improve playability in 60FPS devices
    private final int RENDER_FLAME = 20;
    private final int FLAME_DURATION = 30;
    private final int FLAME_GAP = 10;

    // variable relating to flame's attributes
    private int flameDuration;

    // indicates whether steel pipe has flames on it
    private boolean flaming;

    /**
     * A constructor which creates a new instance of a SteelPipe.
     * Calls the constructor of its parent class using super().
     */
    public SteelPipe() {
        super(STEEL_PIPE_PNG);
        flameDuration = 0;
        flaming = false;
    }

    /**
     * A method to render steel pipe set's and flame's images on screen
     *
     * @param currFrame The int representing the current frame of the game.
     */
    public void spawn(int currFrame) {

        // steel pipe is set to be flaming
        if (currFrame % RENDER_FLAME == 0) {
            flaming = true;
        }

        // flames are still on steel pipe
        if (flaming) {

            // render flame image
            if (flameDuration <= FLAME_DURATION) {
                flameDuration++;
                // flame of top pipe
                FLAME_IMAGE.draw(this.getPipeX(), this.getTopPipeY() + (this.getPipeHeight() / 2) +
                        (FLAME_IMAGE.getHeight() / 2.0) - FLAME_GAP);
                // flame of bottom pipe
                FLAME_IMAGE.draw(this.getPipeX(), this.getBottomPipeY() - (this.getPipeHeight() / 2) -
                                    (FLAME_IMAGE.getHeight() / 2.0) + FLAME_GAP,
                                            DRAW_OPTIONS.setRotation(Math.toRadians(getInvertedAngle())));
            }

            // steel pipe is no longer flaming, flames are removed
            else {
                flameDuration = 0;
                flaming = false;
            }
        }

        STEEL_PIPE_PNG.draw(this.getPipeX(), this.getBottomPipeY(), DRAW_OPTIONS.setRotation(Math.toRadians(getInvertedAngle())));
        STEEL_PIPE_PNG.draw(this.getPipeX(), this.getTopPipeY());
    }

    /**
     * A method which returns a boolean representing state of steel pipe's flames.
     *
     * @return The boolean representing state whether steel pipes have flames or not.
     */
    public boolean getFlaming(){
        return flaming;
    }

    /**
     * A method which returns a double representing flame's height.
     *
     * @return The double representing flame's height.
     */
    public double getFlameHeight(){return FLAME_IMAGE.getHeight();}

    /**
     * A method which returns a double representing extra gap caused by flames being rendered by top steel pipe.
     *
     * @return The double representing extra gap caused by flames being rendered by top steel pipe.
     */
    public double extraTopGap(){return getTopPipeY() + getFlameHeight();}

    /**
     * A method which returns a double representing extra gap caused by flames being rendered by bottom steel pipe.
     *
     * @return The double representing extra gap caused by flames being rendered by bottom steel pipe.
     */
    public double extraBottomGap(){return getBottomPipeY() - getFlameHeight();}

    /**
     * A method which returns a Rectangle representing top steel pipe's Rectangle box when flames are rendered on it.
     *
     * @return The Rectangle representing top steel pipe's Rectangle box when flames are rendered on it.
     */
    public Rectangle extraTopBox(){return STEEL_PIPE_PNG.getBoundingBoxAt(new Point(getPipeX(), extraTopGap()));}

    /**
     * A method which returns a Rectangle representing bottom steel pipe's Rectangle box when flames are rendered on it.
     *
     * @return The Rectangle representing bottom steel pipe's Rectangle box when flames are rendered on it.
     */
    public Rectangle extraBottomBox(){return STEEL_PIPE_PNG.getBoundingBoxAt(new Point(getPipeX(),  extraBottomGap()));}

}

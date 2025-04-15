import bagel.DrawOptions;
import bagel.Image;

/**
 * Child class PlasticPipe which extends from PipeSet.
 */
public class PlasticPipe extends PipeSet{
    // image of plastic pipe
    private static final Image PLASTIC_PIPE_PNG = new Image("res/level/plasticPipe.png");
    private final DrawOptions DRAW_OPTIONS = new DrawOptions();

    /**
     * A constructor which creates a new instance of a PlasticPipe.
     * Calls the constructor of its parent class using super().
     */
    public PlasticPipe() {
        super(PLASTIC_PIPE_PNG);
    }

    /**
     * A method to render image of top and bottom plastic pipe during gameplay.
     *
     * @param currFrame The int representing the current frame of the game.
     */
    public void spawn(int currFrame) {
        PLASTIC_PIPE_PNG.draw(this.getPipeX(), this.getTopPipeY());
        PLASTIC_PIPE_PNG.draw(this.getPipeX(), this.getBottomPipeY(), DRAW_OPTIONS.setRotation(Math.toRadians(getInvertedAngle())));
    }

}

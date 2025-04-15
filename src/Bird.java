import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Abstract class of Bird.
 */
public abstract class Bird {

    // constants related to bird's flying and falling behaviour
    private final double FLY_VELOCITY = -4;
    private final double ACCELERATION = 0.1;
    private final double MAX_FALL_SPEED = 12;
    // constants related to bird's image and spawn position
    private final Image BIRD_IMAGE;
    private final Point BIRD_SPAWN = new Point (200, 350);
    private final double BIRD_X = BIRD_SPAWN.x;
    // constant at which bird images are rendered
    private final int RENDER_FRAME = 10;

    // variable relating to bird's attributes
    private double birdY;
    private double birdVelocity;
    private Rectangle birdBox;

    /**
     * A constructor which initialises an instance of a subclass of a Bird.
     * Bird abstract class itself cannot be instantiated.
     *
     * @param birdImage The image of the bird.
     */
    public Bird(Image birdImage) {
        this.BIRD_IMAGE = birdImage;
        birdY = BIRD_SPAWN.y;
        birdBox = birdImage.getBoundingBoxAt(new Point(BIRD_X,birdY));
        birdVelocity = 0;
    }

    /**
     * A method which returns a double representing bird's X-position.
     *
     * @return The double representing bird's X-position.
     */
    public double getBirdX() {
        return BIRD_X;
    }

    /**
     * A method which returns a double representing bird's Y-position.
     *
     * @return The double representing bird's Y-position.
     */
    public double getBirdY() {
        return birdY;
    }

    /**
     * A method which returns a double representing bird's width.
     *
     * @return The double representing bird's width.
     */
    public double getBirdWidth(){return BIRD_IMAGE.getWidth();}

    /**
     * A method which returns a Rectangle representing bird's box.
     *
     * @return The Rectangle representing bird's box.
     */
    public Rectangle getBirdBox(){
        return birdBox;
    }

    /**
     * A method which returns an int representing bird's render frame.
     *
     * @return The int representing bird's render frame.
     */
    public int getRenderFrame(){return RENDER_FRAME;}

    /**
     * A method which updates bird's Rectangle box.
     */
    public void updateBirdBox() {
        birdBox = BIRD_IMAGE.getBoundingBoxAt(new Point(BIRD_X, birdY));
    }

    /**
     * A method which initiates bird to start flying.
     */
    public void birdFly() {
        birdVelocity = FLY_VELOCITY - ACCELERATION;
    }

    /**
     * A method which initiates bird to start falling.
     */
    public void birdFall() {
        if (birdVelocity + ACCELERATION <= MAX_FALL_SPEED) {
            birdVelocity += ACCELERATION;
        }
        birdY += birdVelocity;
    }

    /**
     * A method which returns a boolean checking if bird has reached any out-of-bounds boundary.
     *
     * @return The boolean representing whether bird has reached out-of-bounds border or not.
     */
    public boolean outOfBound() {
        return birdY < 0 || birdY > Window.getHeight();
    }

    /**
     * An abstract method at which subclasses implement respective logic of rendering bird's image to screen.
     *
     * @param currFrame The int representing the current frame of the game.
     */
    public abstract void spawn(int currFrame);

}

import bagel.Image;

/**
 * Child class BirdL0 which extends from Bird.
 */
public class BirdL0 extends Bird {
    // images of bird up and bird down for level0
    // private static final Image BIRD_UP_L0 = new Image("res/level-0/birdWingUp.png");
    // private static final Image BIRD_DOWN_L0 = new Image("res/level-0/birdWingDown.png");

    private static final Image BIRD_UP_L0 = new Image("res/level-0/birdWingUp.png");
    private static final Image BIRD_DOWN_L0 = new Image("res/level-0/birdWingDown.png");


    /**
     * A constructor which creates a new instance of a BirdL0.
     * Calls the constructor of its parent class using super().
     */
    public BirdL0(){
        super(BIRD_DOWN_L0);
    }

    /**
     * A method to render image of BirdL0 during gameplay.
     *
     * @param currFrame The int representing the current frame of the game.
     */
    public void spawn(int currFrame) {
        if (currFrame % getRenderFrame()== 0) {
            BIRD_UP_L0.draw(this.getBirdX(), this.getBirdY());
        } else {
            BIRD_DOWN_L0.draw(this.getBirdX(), this.getBirdY());
        }
    }
}

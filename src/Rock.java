import bagel.Image;

/**
 * Child class Rock which extends from Weapon.
 */
public class Rock extends Weapon{
    // constants related to rock's image and shooting range
    private static final double ROCK_RANGE = 25;
    private static final Image ROCK_PNG = new Image("res/level-1/rock.png");

    /**
     * A constructor which creates a new instance of a Rock.
     * Calls the constructor of its parent class using super().
     */
    public Rock(){
        super(ROCK_PNG, ROCK_RANGE);
    }

    /**
     * A method to render image of Rock during gameplay.
     */
    public void spawn(){
        ROCK_PNG.draw(this.getWeaponX(), this.getWeaponY());
    }

}

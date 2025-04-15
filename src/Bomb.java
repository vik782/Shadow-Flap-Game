import bagel.Image;

/**
 * Child class Bomb which extends from Weapon.
 */
public class Bomb extends Weapon{
    // constants related to bomb's image and shooting range
    private static final double BOMB_RANGE = 50;
    private static final Image BOMB_PNG = new Image("res/level-1/bomb.png");

    /**
     * A constructor which creates a new instance of a Bomb.
     * Calls the constructor of its parent class using super().
     */
    public Bomb() {
        super(BOMB_PNG, BOMB_RANGE);
    }

    /**
     * A method to render image of Bomb during gameplay.
     */
    public void spawn(){
        BOMB_PNG.draw(this.getWeaponX(), this.getWeaponY());
    }
}

import bagel.Image;

/**
 * Child class BirdL1 which extends from BirdL1
 */
public class BirdL1 extends Bird {
    // images of bird up and bird down for level1
    private static final Image BIRD_UP_L1 = new Image("res/level-1/birdWingUp.png");
    private static final Image BIRD_DOWN_L1 = new Image("res/level-1/birdWingDown.png");

    // BirdL1 may hold a weapon
    private Weapon currWeapon;
    private boolean holdWeapon;

    /**
     * A constructor which creates a new instance of a BirdL1.
     * Calls the constructor of its parent class using super().
     */
    public BirdL1(){
        super(BIRD_UP_L1);
        // bird initially has no weapon when spawned
        holdWeapon = false;
    }

    /**
     * A method to render image of BirdL1 during gameplay.
     *
     * @param currFrame The int representing the current frame of the game.
     */
    public void spawn(int currFrame) {
        if (currFrame % getRenderFrame() == 0) {
            BIRD_UP_L1.draw(this.getBirdX(), this.getBirdY());
        } else {
            BIRD_DOWN_L1.draw(this.getBirdX(), this.getBirdY());
        }
    }

    /**
     * A method which returns a boolean representing state of bird's weapon.
     *
     * @return The boolean representing whether bird is holding a weapon or not.
     */
    public boolean getHoldWeapon(){
        return holdWeapon;
    }

    /**
     * A method which returns a Weapon representing the weapon being held by the bird.
     *
     * @return The Weapon representing the weapon being held by the bird.
     */
    public Weapon getCurrWeapon(){return currWeapon;}

    /**
     * A method which sets the new state of the bird holding a weapon.
     *
     * @param holdWeapon The boolean representing whether a weapon is being held or not by the bird.
     */
    public void setHoldWeapon(boolean holdWeapon){this.holdWeapon = holdWeapon;}

    /**
     * A method which sets the new weapon being held by the bird.
     *
     * @param currWeapon The Weapon representing the weapon being held by the bird.
     */
    public void setCurrWeapon(Weapon currWeapon){this.currWeapon = currWeapon;}

}


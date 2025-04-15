import bagel.*;
import bagel.util.*;
import bagel.util.Rectangle;

/**
 * Abstract class of Weapon which implements interface TimeScalable.
 */
public abstract class Weapon implements TimeScalable {

    // constants related to weapon's step size, shoot speed, range and image
    private final double WEAPON_RANGE;
    private final Image WEAPON_IMAGE;
    private final double SHOOT_SPEED = 5;
    // Step size of weapon is changed to improve playability in 60FPS devices
    private final double WEAPON_STEP_SIZE = 5;

    // variables relating to weapon's attributes
    private double weaponX;
    private double weaponY;
    private double weaponSpeed;
    private Rectangle weaponBox;
    private boolean beingHeld;
    private boolean active;
    private boolean wasShot;
    private int shootingDuration;

    /**
     * A constructor which initialises an instance of a subclass of a Weapon.
     * Weapon abstract class itself cannot be instantiated.
     *
     * @param weaponImage The image of the weapon.
     * @param weaponRange The double representing the weapon's shooting range.
     */
    public Weapon(Image weaponImage, double weaponRange) {
        this.WEAPON_IMAGE = weaponImage;
        this.WEAPON_RANGE = weaponRange;
        weaponX = Window.getWidth();
        weaponBox = weaponImage.getBoundingBoxAt(new Point(weaponX, weaponY));
        // when a weapon is created, it is neither being held, active, nor shot
        beingHeld = false;
        active = false;
        wasShot = false;
        shootingDuration = 0;
    }

    /**
     * A method which returns a double representing weapon's X-position.
     *
     * @return The double representing weapon's X-position.
     */
    public double getWeaponX(){
        return weaponX;
    }

    /**
     * A method which returns a double representing weapon's Y-position.
     *
     * @return The double representing weapon's Y-position.
     */
    public double getWeaponY(){
        return weaponY;
    }

    /**
     * A method which returns a boolean representing weapon's state of being held.
     *
     * @return The boolean representing whether the weapon is being held or not.
     */
    public boolean getBeingHeld(){return beingHeld;}

    /**
     * A method which returns a boolean representing weapon's state of being active.
     *
     * @return The boolean representing whether the weapon is currently active or not.
     */
    public boolean getActive(){return active;}

    /**
     * A method which returns a boolean representing weapon's state of being shot.
     *
     * @return The boolean representing whether the weapon has been shot or not.
     */
    public boolean getWasShot(){return wasShot;}

    /**
     * A method which returns a Rectangle representing weapon's Rectangle box.
     *
     * @return The Rectangle representing the weapon's Rectangle box.
     */
    public Rectangle getWeaponBox(){return weaponBox;}


    /**
     * A method which sets the weapon's X-position.
     *
     * @param weaponX The double representing weapon's new X-position.
     */
    public void setWeaponX(double weaponX){
        this.weaponX = weaponX;
    }

    /**
     * A method which sets the weapon's Y-position.
     *
     * @param weaponY The double representing weapon's new Y-position.
     */
    public void setWeaponY(double weaponY){
        this.weaponY= weaponY;
    }

    /**
     * A method which sets the weapon's state of being held.
     *
     * @param beingHeld The boolean representing weapon's new state of being held.
     */
    public void setBeingHeld(boolean beingHeld){this.beingHeld = beingHeld;}

    /**
     * A method which sets the weapon's state of being active.
     *
     * @param active The boolean representing weapon's new state of being active.
     */
    public void setActive(boolean active){this.active = active;}

    /**
     * A method which sets the weapon's state of being shot.
     *
     * @param wasShot The boolean representing weapon's new state of being shot.
     */
    public void setWasShot(boolean wasShot) {this.wasShot = wasShot;}

    /**
     * A method which updates weapon's Rectangle box.
     */
    public void updateWeaponBox(){weaponBox = WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));}

    /**
     * A method which updates the weapon's X-position during gameplay.
     *
     * @param timescale The int representing current timescale.
     */
    public void moveScaled(double timescale) {
        // if weapon is active, weapon gets shot (moves towards right instead of moving towards left)
        if (getActive()) {
            shootingDuration++;
            shoot();
        }
        // weapon continues to move towards the left border
        else{
            weaponSpeed = WEAPON_STEP_SIZE * (Math.pow(speedMultiplier, timescale - 1));
            weaponX -= weaponSpeed;
        }
    }

    /**
     * A method which updates the weapon's X-position during gameplay.
     *
     */
    public void shoot() {
        // weapon being shot is still moving within weapon range
        if (shootingDuration <= WEAPON_RANGE) {
            // shooting speed is not influenced by timescale
            weaponX += SHOOT_SPEED;
        }
        // weapon disappears
        else {
            wasShot = true;
            shootingDuration = 0;
        }
    }

    /**
     * An abstract method at which subclasses implement respective logic of rendering weapon's image to screen.
     */
    public abstract void spawn();

}

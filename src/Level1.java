import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

/**
 * Child class Level1 which extends from Level.
 */
public class Level1 extends Level {
    // level1 has a birdL1, pipe set array and weapon array
    private static Bird birdL1 = new BirdL1();
    private static ArrayList<PipeSet>pipeArrayL1 = new ArrayList<>();
    private ArrayList<Weapon> weaponArray;

    // background image of level1
    private final Image BACKGROUND_IMAGE_L1 = new Image("res/level-1/background.png");

    // max lives for level1
    private final int MAX_LIFE_L1 = 6;

    // life-bar for level1
    private Image[] lifeBarL1;

    // constant used to indicate new weapon insertion
    private final int WEAPON_RENDER_FRAME = 50;

    // random index type and gaps for pipe's and weapon's spawn position
    private int idxType;
    private int idxGap;
    private int idxWeaponType;
    private int idxWeaponGap;
    private final Random RANDOM = new Random();

    // indicates whether a pipe set has been shot through
    private boolean PipeSetShotThrough;

    /**
     * A constructor which creates a new instance of a Level1.
     * Calls the constructor of its parent class using super().
     */
    public Level1() {
        super(birdL1, pipeArrayL1);
        weaponArray = new ArrayList<Weapon>();
        initializeLifeBar();
        PipeSetShotThrough = false;
    }

    /**
     * A method which returns a boolean representing state of pipe set of being shot-through.
     *
     * @return The boolean representing state whether pipe set has been shot-through or not.
     */
    public boolean getPipeSetShotThrough(){return PipeSetShotThrough;}



    /**
     * A method to render background image of level1.
     */
    public void drawBackground(){
        BACKGROUND_IMAGE_L1.drawFromTopLeft(0,0);
    }

    /**
     * A method to initialize life-bar of level1 with images of full-life.
     */
    public void initializeLifeBar(){
        lifeBarL1 = new Image[MAX_LIFE_L1];
        for (int i = 0; i < MAX_LIFE_L1; i++) {
            lifeBarL1[i] = getFullLife();
        }
    }

    /**
     * A method to render life bar with its images of hearts.
     */
    public void spawnLifeBar() {
        int pos = getLifeBarX();
        for (int i=0; i<MAX_LIFE_L1; i++) {
            lifeBarL1[i].drawFromTopLeft(pos, getLifeBarY());
            pos+=getLifeBarGap();
        }
    }

    /**
     * A method to replace a full-life image with a no-life image in the life bar.
     */
    public void loseLife() {
        lifeBarL1[MAX_LIFE_L1 - getLoseCount()] = getNoLife();
    }

    /**
     * A method to generate new random instance of a subclass of a PipeSet in level1.
     *
     * @return The PipeSet which was randomly picked and made.
     */
    public PipeSet makeRandomPipe(){
        // picking a random instance of PipeSet
        PipeSet[] PIPE_MATERIAL = {new PlasticPipe(), new SteelPipe()};
        idxType = RANDOM.nextInt(PIPE_MATERIAL.length);
        PipeSet pipeSet = PIPE_MATERIAL[idxType];

        // picking random spawn locations
        idxGap = (int) ((Math.random() * (getLowGap() - getMidGap())) + getHighGap());
        pipeSet.setTopPipeY(pipeSet.getTopPipeY() + (idxGap - getMidGap()));
        pipeSet.setBottomPipeY(pipeSet.getBottomPipeY() + (idxGap - getMidGap()));
        return pipeSet;
    }

    /**
     * A method to insert pipe set into level1's pipe array.
     *
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public void insertPipe(int currFrame, double timescale) {
        // the rate at which pipes are inserted into the pipe array is influenced by timescale
        double frame = (double)getPipeRenderFrame() / moveSpeedChange(timescale);

        // rounding up frame, based from project clarification specs
        if ( currFrame % Math.ceil(frame) == 0) {
            // insert pipe set into pipe array
            pipeArrayL1.add(makeRandomPipe());
        }
    }

    /**
     * A method to clear the pipes
     *
     */
    public void resetPipes(){
        pipeArrayL1.clear();
    }

    /**
     * A method to insert pipe set into level1's pipe array.
     *
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public void spawnPipes(int currFrame, double timescale){
        // iterating through pipe set array
        Iterator<PipeSet> iterator = pipeArrayL1.iterator();
        PipeSetShotThrough = false;

        while(iterator.hasNext()) {
            PipeSet pipeSet = iterator.next();

            // checking for bird-pipe and weapon-pipe collision
            // if weapon is non-active but is being held by bird, bird loses life
            if ( (birdPipeCollision(pipeSet)) || !activeWeaponPipeCollision(pipeSet) ) {
                // pipe set removed, no longer being spawned
                iterator.remove();
                // loses a life
                setLoseCount(1);
                // replaces a full-life with a no-life
                loseLife();
            }

            // checking for weapon-pipe collision
            // if weapon is active, then pipe set gets removed and earns a point
            else if ( (activeWeaponPipeCollision(pipeSet)) && pipeSet.getRemove()) {
                iterator.remove();
                PipeSetShotThrough = true;
            }

            else {
                // pipe set is rendered on screen and continues to move
                pipeSet.spawn(currFrame);
                pipeSet.moveScaled(timescale);
            }
        }
    }

    /**
     * A method to generate new instance of a subclass of Weapon in level1.
     *
     * @return The Weapon which was randomly picked and made.
     */
    public Weapon makeRandomWeapon(){
        // picking a random instance of Weapon
        Weapon[] weaponTypes = {new Rock(), new Bomb()};
        idxWeaponType = RANDOM.nextInt(weaponTypes.length);
        Weapon weapon = weaponTypes[idxWeaponType];

        // picking random spawn locations
        idxWeaponGap = (int) ((Math.random() * (getLowGap() - getHighGap())) + getHighGap());
        weapon.setWeaponY(idxWeaponGap);

        return weapon;
    }

    /**
     * A method to insert pipe set into level1's pipe array.
     *
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public void insertWeapon(int currFrame, double timescale) {
        // the rate at which pipes are inserted into the pipe array is influenced by timescale
        double frame = (double) WEAPON_RENDER_FRAME / moveSpeedChange(timescale);
        Weapon newWeapon = makeRandomWeapon();
        boolean insert = false;

        if (currFrame % Math.ceil(frame) == 0) {
            insert = true;

            // checks whether weapon spawn position collides with a pipe set
            for (PipeSet pipeSet: pipeArrayL1){
                if (weaponPipeCollision(pipeSet, newWeapon)){
                    // doesn't insert if position collides
                    insert = false;
                }
            }
        }

        if (insert) {
            // insert weapon into weapon array
            weaponArray.add(newWeapon);
        }
    }

    /**
     * A method to insert weapon into level1's weapon array.
     *
     * @param timescale The int representing current timescale.
     */
    public void spawnWeapon(double timescale){
        // iterating through weapon array
        Iterator<Weapon> iterator = weaponArray.iterator();
        while(iterator.hasNext()) {
            Weapon weapon = iterator.next();
            weapon.updateWeaponBox();

            // checking for bird-weapon collision and whether bird is currently holding a weapon
            if ( (birdWeaponCollide(weapon)) && (!((BirdL1)birdL1).getHoldWeapon()) ){
                // gives weapon to bird
                weapon.setBeingHeld(true);
                ((BirdL1)birdL1).setHoldWeapon(true);
                ((BirdL1)birdL1).setCurrWeapon(weapon);
            }

            if (weapon.getBeingHeld()){
                // weapon is attached to bird's beak
                weapon.setWeaponX(birdL1.getBirdX() + (birdL1.getBirdWidth()/2));
                weapon.setWeaponY(birdL1.getBirdY());
            }

            if (weapon.getWasShot()){
                // weapon's shooting range has been met, remove weapon and is no longer being spawned
                iterator.remove();
            }

            // weapon is rendered on screen and continues to move
            weapon.spawn();
            weapon.moveScaled(timescale);
        }
    }

    /**
     * A method which returns a boolean representing state of bird-pipe collision.
     * Overrides its parent's class method
     *
     * @param pipeSet The PipeSet being iterated.
     * @return The boolean representing state whether a pipe set has collided with a bird or not.
     */
    @Override
    public boolean birdPipeCollision(PipeSet pipeSet) {
        // checks whether the pipe set is an instance of a steel pipe
        if (pipeSet instanceof SteelPipe) {
            // checks whether the steel pipe is flaming or not
            if (((SteelPipe)pipeSet).getFlaming()){
                // flames increment the top and bottom steel pipe's Rectangle box
                return ( (((SteelPipe) pipeSet).extraTopBox().intersects(birdL1.getBirdBox())) ||
                        (((SteelPipe) pipeSet).extraBottomBox().intersects(birdL1.getBirdBox())) );
            }
        }
        // either steel pipe does not have flames rendered or pipe set is an instance of plastic pipe
        return super.birdPipeCollision(pipeSet);
    }

    /**
     * A method which returns a boolean representing state of bird-weapon collision.
     *
     * @param weapon The Weapon being iterated.
     * @return The boolean representing state whether a weapon has collided with a bird or not.
     */
    public boolean birdWeaponCollide(Weapon weapon) {
        weapon.updateWeaponBox();
        birdL1.updateBirdBox();
        return  (birdL1.getBirdBox().intersects(weapon.getWeaponBox()) && !weapon.getActive()) ;
    }

    /**
     * A method which returns a boolean representing state of weapon-pipe collision.
     *
     * @param pipeSet The PipeSet being iterated.
     * @param weapon The Weapon being iterated.
     * @return The boolean representing state whether a weapon has collided with a pipe set or not.
     */
    public boolean weaponPipeCollision(PipeSet pipeSet, Weapon weapon){
        weapon.updateWeaponBox();
        pipeSet.updateBottomPipeBox();
        pipeSet.updateTopPipeBox();
        return pipeSet.getTopBox().intersects(weapon.getWeaponBox()) ||
                    pipeSet.getBottomBox().intersects(weapon.getWeaponBox());
    }

    /**
     * A method which returns a boolean representing state of an active, weapon-pipe collision.
     *
     * @param pipeSet The PipeSet being iterated.
     * @return The boolean representing state whether an active weapon has collided with a pipe set or not.
     */
    public boolean activeWeaponPipeCollision(PipeSet pipeSet) {
        // iterating through weapon array
        Iterator<Weapon> iterator = weaponArray.iterator();
        while(iterator.hasNext()){
            Weapon weapon = iterator.next();

            if  ( weaponPipeCollision(pipeSet, weapon) && !weapon.getActive() ){
                // weapon collides with pipe, but weapon is non-active
                return false;
            }

            else if ( weaponPipeCollision(pipeSet, weapon) && weapon.getActive() ){
                // weapon collides with pipe and weapon is active, pipe set might get removed
                iterator.remove();
                pipeSet.setRemove(true);

                if ( (weapon instanceof Rock) && (pipeSet instanceof SteelPipe) ) {
                    // rock collides with steel pipe
                    // rock gets removed, but steel pipe still remains
                    pipeSet.setRemove(false);
                    weapon.setWasShot(true);
                }

                return true;
            }

        }
        return true;
    }

    /**
     * A method to initiate the start of bird's shooting movement if appropriate input was pressed.
     *
     * @param input The key-input of which the user pressed.
     * @param timescale The int representing current timescale.
     */
    public void birdShoot(Input input, double timescale){
        // checks if bird is currently holding a weapon when shoot input was pressed
        if ( (input.wasPressed(Keys.S)) && (((BirdL1)birdL1).getHoldWeapon()) ){
            // once shot, bird doesn't hold a weapon anymore
            ((BirdL1)birdL1).setHoldWeapon(false);
            // weapon becomes active
            ((BirdL1)birdL1).getCurrWeapon().setActive(true);
            // weapon is no longer being held
            ((BirdL1)birdL1).getCurrWeapon().setBeingHeld(false);
        }
    }

    /**
     * A method which removes a weapon once it leaves the left border.
     */
    public void removeWeapon(){
        Iterator<Weapon> iterator = weaponArray.iterator();
        while (iterator.hasNext()){
            Weapon weapon = iterator.next();
            weapon.updateWeaponBox();

            if ( weapon.getWeaponBox().right() < 0) {
                iterator.remove();
            }
        }
    }

    /**
     * A method which operates the level during gameplay.
     * Logic of level1's gameplay is implemented here.
     *
     * @param input The key-input of which the user pressed.
     * @param currFrame The int representing the current frame of the game.
     * @param timescale The int representing current timescale.
     */
    public void implementLevel(Input input, int currFrame, double timescale) {
        drawBackground();

        // triggers birdL1 to fly once SPACE key is pressed
        if (input.wasPressed(Keys.SPACE)) {
            birdL1.birdFly();
        }
        // triggers birdL1 to start falling
        birdL1.birdFall();
        // spawns image of birdL1
        birdL1.spawn(currFrame);

        // make, insert, move and spawn pipe sets according to frame count and timescale
        insertPipe(currFrame, timescale);
        spawnPipes(currFrame, timescale);

        // spawn life bar of level1
        spawnLifeBar();

        // make, insert, move and spawn weapon according to frame count and timescale
        insertWeapon(currFrame, timescale);
        spawnWeapon(timescale);

        // weapon gets shot if appropriate input was pressed
        birdShoot(input, timescale);

        if ( birdL1.outOfBound() ) {
            // bird left top/bottom border, loses a life
            setLoseCount(1);
            loseLife();

            if ( ((BirdL1)birdL1).getHoldWeapon() ) {
                // bird is out of bound, but was holding a weapon
                Weapon tempWeapon = ((BirdL1)birdL1).getCurrWeapon();

                // re-instantiate birdL1
                birdL1 = new BirdL1();
                ResetBird(birdL1);

                // weapon doesn't get removed, but is given to the new bird
                ((BirdL1)birdL1).setCurrWeapon(tempWeapon);
                ((BirdL1)birdL1).setHoldWeapon(true);
            }

            else {
                // no weapon was being held
                birdL1 = new BirdL1();
                ResetBird(birdL1);
            }
        }

        // once pipe sets or weapons leaves left border, they get removed
        removePipe();
        removeWeapon();

    }

}

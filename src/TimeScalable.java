/**
 *  An interface for classes which can be TimeScalable
 */
public interface TimeScalable {
    // constant for multiplier of timescale
    double speedMultiplier = 1 + 0.50;

    /**
     * An abstract method at which classes implement to move, adjusted by timescale.
     *
     * @param timescale The int representing current timescale.
     */
    void moveScaled(double timescale);
}

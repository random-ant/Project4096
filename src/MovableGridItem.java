import mayflower.*;

/**
 * Represents an item in the game grid that can move.
 */
public class MovableGridItem extends Actor {
    private Coordinate targetDestination;
    private double vx, vy; // Velocity in the x and y directions
    private double ax, ay; // Acceleration in the x and y directions

    /**
     * Constructs a {@code MovableGridItem} instance with no target destination
     * and zero velocity and acceleration.
     */
    public MovableGridItem() {
        this.targetDestination = null;
        vx = vy = ax = ay = 0;
    }

    /**
     * Updates the position of the grid item based on its velocity and acceleration.
     * If the item reaches or surpasses its target destination, it snaps to the
     * target destination and stops moving.
     */
    public void act() {
        // Update the block's position based on its velocity and acceleration
        vx += ax;
        vy += ay;
        setLocation(getX() + vx, getY() + vy);

        // if past target destination, snap to target destination and stop
        if ((vy < 0 && getY() <= GameWorld.convertToPixels(this.targetDestination).getY())
                || (vy > 0 && getY() >= GameWorld.convertToPixels(this.targetDestination).getY())
                || (vx < 0 && getX() <= GameWorld.convertToPixels(this.targetDestination).getX())
                || (vx > 0 && getX() >= GameWorld.convertToPixels(this.targetDestination).getX())) {
            vx = 0;
            vy = 0;
            ax = 0;
            ay = 0;
            Coordinate blockPixel = GameWorld.calculateBlockPixel(this.targetDestination);
            setLocation(blockPixel.getX(), blockPixel.getY());

            GameWorld w = (GameWorld) getWorld();
            w.removeMovingBlock(this);
        }
    }

    /**
     * Gets the target destination of the grid item.
     *
     * @return The target destination as a {@code Coordinate}.
     */
    public Coordinate getTargetDestination() {
        return targetDestination;
    }

    /**
     * Sets the target destination for the item.
     *
     * @param targetDestination The target destination coordinate.
     */
    public void setTargetDestination(Coordinate targetDestination) {
        this.targetDestination = targetDestination;
    }

    /**
     * Sets the acceleration in the x direction.
     *
     * @param ax The acceleration in the x direction.
     */
    public void setAx(double ax) {
        this.ax = ax;
    }

    /**
     * Sets the acceleration in the y direction.
     *
     * @param ay The acceleration in the y direction.
     */
    public void setAy(double ay) {
        this.ay = ay;
    }

}

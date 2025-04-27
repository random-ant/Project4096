import mayflower.*;

public class MovableGridItem extends Actor {
    private Coordinate targetDestination;
    private double vx, vy;
    private double ax, ay;

    public MovableGridItem() {
        this.targetDestination = null;
        vx = vy = ax = ay = 0;
    }

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

    public Coordinate getTargetDestination() {
        return targetDestination;
    }

    public void setTargetDestination(Coordinate targetDestination) {
        this.targetDestination = targetDestination;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    // @Override
    // public int hashCode() {
    // final int prime = 31;
    // int result = 1;
    // result = prime * result + ((targetDestination == null) ? 0 :
    // targetDestination.hashCode());
    // long temp;
    // temp = Double.doubleToLongBits(vx);
    // result = prime * result + (int) (temp ^ (temp >>> 32));
    // temp = Double.doubleToLongBits(vy);
    // result = prime * result + (int) (temp ^ (temp >>> 32));
    // temp = Double.doubleToLongBits(ax);
    // result = prime * result + (int) (temp ^ (temp >>> 32));
    // temp = Double.doubleToLongBits(ay);
    // result = prime * result + (int) (temp ^ (temp >>> 32));
    // return result;
    // }

    // @Override
    // public boolean equals(Object obj) {
    // if (this == obj)
    // return true;
    // if (obj == null)
    // return false;
    // if (getClass() != obj.getClass())
    // return false;
    // MovableGridItem other = (MovableGridItem) obj;
    // if (targetDestination == null) {
    // if (other.targetDestination != null)
    // return false;
    // } else if (!targetDestination.equals(other.targetDestination))
    // return false;
    // if (Double.doubleToLongBits(vx) != Double.doubleToLongBits(other.vx))
    // return false;
    // if (Double.doubleToLongBits(vy) != Double.doubleToLongBits(other.vy))
    // return false;
    // if (Double.doubleToLongBits(ax) != Double.doubleToLongBits(other.ax))
    // return false;
    // if (Double.doubleToLongBits(ay) != Double.doubleToLongBits(other.ay))
    // return false;
    // return true;
    // }

}

import mayflower.*;

public class Block extends Actor {

    private int value;
    private BColor color;
    private Coordinate targetDestination;
    private double vx, vy;
    private double ax, ay;

    public Block(int value, BColor color) {
        this.value = value;
        this.color = color;
        this.targetDestination = null;
        vx = vy = ax = ay = 0;

        if (color == BColor.NEUTRAL) {
            MayflowerImage img = new MayflowerImage("src/img/blocks/neutral/neutral-block" + value + ".png");
            img.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
            setImage(img);
        }

        if (color == BColor.BLUE) {
            MayflowerImage blue = new MayflowerImage("src/img/blocks/blue/blue-block" + value + ".png");
            blue.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
            setImage(blue);
        }

        if (color == BColor.RED) {
            MayflowerImage red = new MayflowerImage("src/img/blocks/red/red-block" + value + ".png");
            red.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
            setImage(red);
        }
    }

    public void act() {
        // Update the block's position based on its velocity and acceleration
        vx += ax;
        vy += ay;
        setLocation(getX() + vx, getY() + vy);

        if ((vy < 0 && getY() <= GameWorld.convertToPixels(this.targetDestination)[1])) {
            vy = 0;
            ay = 0;
            double[] blockPixel = GameWorld.calculateBlockPixel(this.targetDestination);
            setLocation(blockPixel[0], blockPixel[1]);

            GameWorld w = (GameWorld) getWorld();
            w.removeMovingBlock(this);
            w.removeObject(this);
        }
    }

    public Coordinate getTargetDestination() {
        return targetDestination;
    }

    public void setTargetDestination(Coordinate targetDestination) {
        this.targetDestination = targetDestination;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public BColor getColor() {
        return color;
    }

    public void setColor(BColor color) {
        this.color = color;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

}

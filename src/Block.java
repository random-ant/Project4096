import mayflower.Actor;
import mayflower.Color;
import mayflower.MayflowerImage;
import mayflower.World;

public class Block extends Actor {

    private int value;
    private BColor color;
    private Coordinate coord;

    public Block(int value, BColor color) {
        this.value = value;
        this.color = color;

        if (color == BColor.NEUTRAL) {
            MayflowerImage img = new MayflowerImage("src/img/blocks/neutral-block.png");
            img.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
            setImage(img);
        }

        if (color == BColor.BLUE) {
            MayflowerImage blue = new MayflowerImage("src/img/blocks/blue-block" + value + ".png");
            blue.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
            setImage(blue);
        }

        if (color == BColor.RED) {
            MayflowerImage red = new MayflowerImage("src/img/blocks/red-block" + value + ".png");
            red.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
            setImage(red);
        }
    }

    public void act() {
        World w = getWorld();

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
}

import mayflower.Actor;
import mayflower.MayflowerImage;

/**
 * Represents a block in the game grid with a value and color.
 * The block's appearance is determined by its value and color.
 */
public class Block extends Actor {

    /**
     * The value of the block, which determines its appearance and behavior.
     */
    private int value;

    /**
     * The color of the block, which represents its ownership or type.
     */
    private BColor color;

    /**
     * Constructs a Block with the specified value and color.
     * The block's image is set based on its value and color.
     * 
     * @param value The value of the block.
     * @param color The color of the block.
     */
    public Block(int value, BColor color) {
        this.value = value;
        this.color = color;

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

    /**
     * Defines the behavior of the Block.
     * Currently, this method does not perform any actions.
     */
    public void act() {
    }

    /**
     * Retrieves the value of the block.
     * 
     * @return The value of the block.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the block.
     * 
     * @param value The new value of the block.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Retrieves the color of the block.
     * 
     * @return The color of the block.
     */
    public BColor getColor() {
        return color;
    }

    /**
     * Sets the color of the block.
     * 
     * @param color The new color of the block.
     */
    public void setColor(BColor color) {
        this.color = color;
    }
}
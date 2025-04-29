package game;
import mayflower.*;

/**
 * Represents a block in the game grid. Each block has a value and a color.
 */
public class Block extends MovableGridItem {
    private int value;
    private BColor color;

    /**
     * Constructs a block with the specified value and color.
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
     * Called every game frame.
     */
    public void act() {
        super.act();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + value;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Block other = (Block) obj;
        if (value != other.value)
            return false;
        if (color != other.color)
            return false;
        return true;
    }

    /**
     * Gets the value of the block.
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
     * Gets the color of the block.
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

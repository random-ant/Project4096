import mayflower.*;

public class Block extends MovableGridItem {
    private int value;
    private BColor color;

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

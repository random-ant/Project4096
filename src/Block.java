import mayflower.Actor;
import mayflower.MayflowerImage;
import mayflower.World;

public class Block extends Actor {

    private int value;
    private BColor color;

    public Block(int value, BColor color) {
        this.value = value;
        this.color = color;
        MayflowerImage img = new MayflowerImage("src/img/neutral-block.png");
        img.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
        setImage(img);
    }

    public void act() {
        World w = getWorld();
        w.showText("" + value, getCenterX() - (GameWorld.TILE_WIDTH - GameWorld.BLOCK_WIDTH),
                getCenterY() + (GameWorld.TILE_HEIGHT - GameWorld.BLOCK_HEIGHT));
    }
}

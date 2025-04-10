import mayflower.Actor;
import mayflower.MayflowerImage;
import mayflower.World;

public class Block extends Actor {

    private int value;
    private BColor color;

    public Block(int value, BColor color) {
        this.value = value;
        this.color = color;
      
        if (color == BColor.NEUTRAL)
        {
            MayflowerImage img = new MayflowerImage("src/img/neutral-block.png");
            img.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
            setImage(img);
        }

        if (color == BColor.BLUE)
        {
            MayflowerImage blue = new MayflowerImage("src/img/blue-block.png");
            setImage(blue);
        }
        
        if (color == BColor.RED)
        {
            MayflowerImage red = new MayflowerImage("src/img/red-block.png");
            setImage(red);
        }
    }

    public void act() {
        World w = getWorld();
        w.showText("" + value, getCenterX() - (GameWorld.TILE_WIDTH - GameWorld.BLOCK_WIDTH),
                getCenterY() + (GameWorld.TILE_HEIGHT - GameWorld.BLOCK_HEIGHT));

        
                     
    }
}

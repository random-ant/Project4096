import mayflower.*;

public class Powerup extends MovableItem {

    public Powerup() {
        MayflowerImage img = new MayflowerImage("src/img/powerup.png");
        img.scale(GameWorld.BLOCK_WIDTH, GameWorld.BLOCK_HEIGHT);
        setImage(img);
    }

    public void act() {
    }

}

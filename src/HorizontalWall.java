import mayflower.Actor;
import mayflower.MayflowerImage;

public class HorizontalWall extends Actor {
    public HorizontalWall() {
        MayflowerImage img = new MayflowerImage("src/img/wall-top.png");
        setImage(img);
    }

    public void act() {
    }
}

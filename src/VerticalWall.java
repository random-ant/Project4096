import mayflower.Actor;
import mayflower.MayflowerImage;

public class VerticalWall extends Actor {
    public VerticalWall() {
        MayflowerImage img = new MayflowerImage("src/img/wall-left.png");
        setImage(img);
    }

    public void act() {
    }
}

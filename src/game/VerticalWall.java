package game;

import mayflower.Actor;
import mayflower.MayflowerImage;

/**
 * Represents a vertical wall in the game grid.
 */
public class VerticalWall extends Actor {

    /**
     * Constructs a VerticalWall instance and sets its image.
     */
    public VerticalWall() {
        MayflowerImage img = new MayflowerImage("src/img/wall-left.png");
        setImage(img);
    }

    public void act() {
    }
}

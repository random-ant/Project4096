package game;

import mayflower.*;

/**
 * Represents a horizontal wall in the game grid.
 */
public class HorizontalWall extends Actor {

    /**
     * Constructs a HorizontalWall instance and sets its image.
     */
    public HorizontalWall() {
        MayflowerImage img = new MayflowerImage("src/img/wall-top.png");
        setImage(img);
    }

    public void act() {
    }
}

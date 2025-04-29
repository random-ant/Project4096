package game;

import mayflower.*;

/**
 * Represents the border of the game grid.
 */
public class GridBorder extends Actor {

    /**
     * Constructs a GridBorder instance and sets its image.
     */
    public GridBorder() {
        MayflowerImage border = new MayflowerImage("src/img/tile.png");
        border.scale(710, 710);
        setImage(border);
    }

    public void act() {
    }

}

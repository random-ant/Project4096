import mayflower.*;

/**
 * Represents the border of the game grid.
 * This class is responsible for displaying the grid border using an image.
 */
public class GridBorder extends Actor {

    /**
     * Constructs a GridBorder and sets its image.
     * The image is scaled to fit the grid dimensions.
     */
    public GridBorder() {
        MayflowerImage border = new MayflowerImage("src/img/tile.png");
        border.scale(710, 710);
        setImage(border);
    }

    /**
     * Defines the behavior of the GridBorder.
     * Currently, this method does not perform any actions.
     */
    public void act() {
    }
}

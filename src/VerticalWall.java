import mayflower.Actor;
import mayflower.MayflowerImage;

/**
 * Represents a vertical wall in the game grid.
 * This class is responsible for displaying the left wall using an image.
 */
public class VerticalWall extends Actor {

    /**
     * Constructs a VerticalWall and sets its image.
     * The image represents the left wall of the grid.
     */
    public VerticalWall() {
        MayflowerImage img = new MayflowerImage("src/img/wall-left.png");
        setImage(img);
    }

    /**
     * Defines the behavior of the VerticalWall.
     * Currently, this method does not perform any actions.
     */
    public void act() {
    }
}

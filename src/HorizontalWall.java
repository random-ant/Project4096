import mayflower.Actor;
import mayflower.MayflowerImage;

/**
 * Represents a horizontal wall in the game grid.
 * This class is responsible for displaying the top wall using an image.
 */
public class HorizontalWall extends Actor {

    /**
     * Constructs a HorizontalWall and sets its image.
     * The image represents the top wall of the grid.
     */
    public HorizontalWall() {
        MayflowerImage img = new MayflowerImage("src/img/wall-top.png");
        setImage(img);
    }

    /**
     * Defines the behavior of the HorizontalWall.
     * Currently, this method does not perform any actions.
     */
    public void act() {
    }
}
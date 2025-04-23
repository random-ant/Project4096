import mayflower.*;

/**
 * Represents the title graphic displayed in the game.
 * This class is responsible for displaying the game title using an image.
 */
public class Title extends Actor {

    /**
     * Constructs a Title and sets its image.
     * The image is scaled to match the desired dimensions of the title graphic.
     */
    public Title() {
        MayflowerImage title = new MayflowerImage("src/img/title.png");
        title.scale(201, 144);
        setImage(title);
    }

    /**
     * Defines the behavior of the Title.
     * Currently, this method does not perform any actions.
     */
    public void act() {
    }

}

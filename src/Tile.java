import mayflower.*;

/**
 * Represents a tile in the game grid.
 * This class is responsible for displaying a single tile using an image.
 */
public class Tile extends Actor {

    /**
     * Constructs a Tile and sets its image.
     * The image is scaled to match the dimensions of a tile in the game grid.
     */
    public Tile() {
        MayflowerImage tile = new MayflowerImage("src/img/tile.png");
        tile.scale(GameWorld.TILE_WIDTH, GameWorld.TILE_HEIGHT);
        setImage(tile);
    }

    /**
     * Defines the behavior of the Tile.
     * Currently, this method does not perform any actions.
     */
    public void act() {
    }

}

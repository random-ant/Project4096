import mayflower.*;

/**
 * Represents a tile in the game grid.
 */
public class Tile extends Actor {

    /**
     * Constructs a Tile instance and sets its image.
     */
    public Tile() {
        MayflowerImage tile = new MayflowerImage("src/img/tile.png");
        tile.scale(GameWorld.TILE_WIDTH, GameWorld.TILE_HEIGHT);
        setImage(tile);
    }

    public void act() {
    }

}

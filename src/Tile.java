import mayflower.*;

public class Tile extends Actor {

    public Tile() {
        MayflowerImage tile = new MayflowerImage("src/img/tile.png");
        tile.scale(GameWorld.TILE_WIDTH, GameWorld.TILE_HEIGHT);
        setImage(tile);
    }

    public void act() {
    }

}

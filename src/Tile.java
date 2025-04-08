import mayflower.*;

public class Tile extends Actor {

    public Tile() {
        MayflowerImage tile = new MayflowerImage("src/img/tile.png");
        tile.scale(70, 70);
        setImage(tile);
    }

    public void act() {
    }

}

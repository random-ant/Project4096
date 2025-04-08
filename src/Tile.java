import mayflower.*;

public class Tile extends Actor {

    public Tile() {
        MayflowerImage tile = new MayflowerImage("src/img/tile.png");
        tile.scale(100, 100);
        setImage(tile);
    }

    public void act() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'act'");
    }

}

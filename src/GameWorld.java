import mayflower.*;

public class GameWorld extends World {
    private Tile[][] grid;
    public int GRID_HEIGHT = 10, GRID_WIDTH = 10;

    public GameWorld() {
        grid = new Tile[GRID_HEIGHT][GRID_WIDTH];
        // setBackground("grey");
        addObject(new Tile(), 30, 30);

    }

    @Override
    public void act() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'act'");
    }

}

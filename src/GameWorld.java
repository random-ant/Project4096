import mayflower.*;

public class GameWorld extends World {
    private Tile[][] grid;
    public int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public int OFFSET_X = 45, OFFSET_Y = 250;

    public GameWorld() {
        grid = new Tile[GRID_HEIGHT][GRID_WIDTH];
        // setBackground("grey");
        // addObject(new Tile(), 30, 30);

        renderGrid();

    }

    private void renderGrid() {

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                addObject(new Tile(), (j * 70) + OFFSET_X, (i * 70) + OFFSET_Y);
            }
        }
    }

    @Override
    public void act() {
    }

}

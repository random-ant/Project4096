import mayflower.*;
import java.util.ArrayList;

public class GameWorld extends World {
    private Tile[][] grid;

    public static int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public static int OFFSET_X = 45, OFFSET_Y = 250;
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;

    public GameWorld() {
        grid = new Tile[GRID_HEIGHT][GRID_WIDTH];
        grid[1][1] = new Tile();
        // setBackground("grey");
        // addObject(new Tile(), 30, 30);

        renderGrid();

        System.out.println(getEmptyTiles());
        System.out.println(getRandomEmptyTile());

    }

    private void renderGrid() {
        addObject(new Border(), 40, 245);
        addObject(new Title(), 301, 55);

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;
                addObject(new Tile(), x_coord, y_coord);

                Block b = new Block(2, BColor.NEUTRAL, new Coordinate(i, j));
                addObject(b, x_coord + (TILE_WIDTH - BLOCK_WIDTH) / 2, y_coord + (TILE_HEIGHT - BLOCK_HEIGHT) / 2);
            }
        }
    }

    @Override
    public void act() {
    }

    public ArrayList<Coordinate> getEmptyTiles() {
        ArrayList<Coordinate> out = new ArrayList<Coordinate>(GRID_HEIGHT * GRID_WIDTH);
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (grid[i][j] == null) {
                    out.add(new Coordinate(i, j));
                }
            }
        }
        return out;
    }

    public Coordinate getRandomEmptyTile() {
        ArrayList<Coordinate> empty = getEmptyTiles();
        int numEmpty = empty.size();
        int randomIndex = (int) (Math.random() * numEmpty);
        return empty.get(randomIndex);
    }

    public void spawnBlock() {
        double spawnValue = Math.random();
        int value;
        if (spawnValue <= 0.7) {
            value = 2;
        } else if (spawnValue <= 0.9) {
            value = 4;
        } else {
            value = 8;
        }
        Block block = new Block(value, BColor.NEUTRAL, getRandomEmptyTile());

    }

}

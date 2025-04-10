import mayflower.*;
import java.util.*;

public class GameWorld extends World {
    private Block[][] grid;

    public static int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public static int OFFSET_X = 45, OFFSET_Y = 250;
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;

    public GameWorld() {
        grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        grid[1][1] = new Block(2, BColor.BLUE, new Coordinate(1, 1));
        grid[2][1] = new Block(4, BColor.BLUE, new Coordinate(1, 1));
        grid[3][1] = new Block(8, BColor.BLUE, new Coordinate(1, 1));
        grid[4][1] = new Block(16, BColor.BLUE, new Coordinate(1, 1));
        grid[5][1] = new Block(32, BColor.BLUE, new Coordinate(1, 1));
        grid[6][1] = new Block(64, BColor.BLUE, new Coordinate(1, 1));
        grid[7][1] = new Block(128, BColor.BLUE, new Coordinate(1, 1));
        grid[8][1] = new Block(256, BColor.BLUE, new Coordinate(1, 1));

        grid[1][2] = new Block(2, BColor.RED, new Coordinate(1, 1));
        grid[2][2] = new Block(4, BColor.RED, new Coordinate(1, 1));
        grid[3][2] = new Block(8, BColor.RED, new Coordinate(1, 1));
        grid[4][2] = new Block(16, BColor.RED, new Coordinate(1, 1));
        grid[5][2] = new Block(32, BColor.RED, new Coordinate(1, 1));
        grid[6][2] = new Block(64, BColor.RED, new Coordinate(1, 1));
        grid[7][2] = new Block(128, BColor.RED, new Coordinate(1, 1));
        grid[8][2] = new Block(256, BColor.RED, new Coordinate(1, 1));


        addObject(new Border(), 40, 245);
        addObject(new Title(), 301, 55);
        renderGrid();

        System.out.println(getEmptyTiles());
        System.out.println(getRandomEmptyTile());

    }

    private void renderGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;
                addObject(new Tile(), x_coord, y_coord);

                Block currBlock = grid[i][j];
                if (currBlock != null) {
                    addObject(currBlock, x_coord + (TILE_WIDTH - BLOCK_WIDTH) / 2,
                            y_coord + (TILE_HEIGHT - BLOCK_HEIGHT) / 2);
                }
            }
        }
    }

    @Override
    public void act() {
        // listen for key presses and act accordingly
        if (keyPresssed(Keyboard.KEY_UP)) {
            System.out.println("Up key pressed");
        } else if (keyPresssed(Keyboard.KEY_DOWN)) {
            System.out.println("Down key pressed");
        } else if (keyPresssed(Keyboard.KEY_LEFT)) {
            System.out.println("Left key pressed");
        } else if (keyPresssed(Keyboard.KEY_RIGHT)) {
            System.out.println("Right key pressed");
        }
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

    private boolean keyPresssed(int key) {
        return Mayflower.isKeyDown(key) && !Mayflower.wasKeyDown(key);
    }
}

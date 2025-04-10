import mayflower.*;

public class GameWorld extends World {
    private Block[][] grid;

    public static int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public static int OFFSET_X = 45, OFFSET_Y = 250;
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;

    public GameWorld() {
        grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        grid[1][1] = new Block(2, BColor.BLUE);

        addObject(new Border(), 40, 245);
        addObject(new Title(), 301, 55);
        renderGrid();
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

    private boolean keyPresssed(int key) {
        return Mayflower.isKeyDown(key) && !Mayflower.wasKeyDown(key);
    }
}

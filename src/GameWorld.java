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
        grid[1][0] = new Block(2, BColor.BLUE, new Coordinate(1, 1));
        grid[2][0] = new Block(2, BColor.BLUE, new Coordinate(1, 1));
        grid[3][0] = new Block(4, BColor.BLUE, new Coordinate(1, 1));
        grid[4][0] = new Block(2, BColor.BLUE, new Coordinate(1, 1));
        grid[5][0] = new Block(4, BColor.BLUE, new Coordinate(1, 1));

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

            for (int j = 0; j < GRID_WIDTH; j++) {
                Block[] col = new Block[GRID_HEIGHT];
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    col[i] = grid[i][j];
                }

                Block[] result = check(col);

                grid[j] = result;
            }

            renderGrid();

        } else if (true || keyPresssed(Keyboard.KEY_DOWN)) {
            System.out.println("Down key pressed");

            for (int j = 0; j < GRID_WIDTH; j++) {
                Block[] col = new Block[GRID_HEIGHT];
                for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
                    col[GRID_HEIGHT - 1 - i] = grid[i][j];
                }

                ArrayList<Block> result = check(col);

                grid[j] = result;
            }

            renderGrid();

        } else if (keyPresssed(Keyboard.KEY_SPACE)) {
            System.out.println("Space key pressed");
            spawnBlock();
        } else if (keyPresssed(Keyboard.KEY_ESCAPE)) {
            System.out.println("Escape key pressed");
        } else if (keyPresssed(Keyboard.KEY_LEFT)) {
        } else if (keyPresssed(Keyboard.KEY_RIGHT)) {
        }

    }

    // Get list of arrays
    public ArrayList<Block> check(Block[] blocks) {
        Stack<Block> stack = new Stack<Block>();

        for (Block b : blocks) {
            Block currentBlock = b;
            if (currentBlock == null)
                continue;

            while (!stack.empty() && stack.peek().getValue() == currentBlock.getValue()) {
                Block top = stack.pop();
                Block newBlock = new Block(top.getValue() * 2, BColor.NEUTRAL, top.getCoordinate());
                currentBlock = newBlock;
            }
            stack.add(currentBlock);
        }

        ArrayList<Block> result = new ArrayList<Block>();
        // add stack elements to result array
        while (!stack.empty()) {
            result.add(stack.pop());
        }

        return result;
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

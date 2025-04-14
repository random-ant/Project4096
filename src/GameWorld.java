import mayflower.*;
import java.util.*;

public class GameWorld extends World {
    private Block[][] grid;
    private Map<Coordinate, Boolean> topWalls, leftWalls;

    public static int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public static int OFFSET_X = 45, OFFSET_Y = 250;
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;

    public GameWorld() {
        grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        topWalls = new HashMap<Coordinate, Boolean>();
        leftWalls = new HashMap<Coordinate, Boolean>();
        topWalls.put(new Coordinate(1, 1), true);

        addObject(new GridBorder(), 40, 245);
        addObject(new Title(), 301, 55);
        for (int i = 0; i < 10; i++)
            spawnRandomBlock();

        renderGrid();
    }

    private void renderGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;

                // add base tiles
                addObject(new Tile(), x_coord, y_coord);

                // add blocks
                Block currBlock = grid[i][j];
                if (currBlock != null) {
                    addObject(currBlock, x_coord + (TILE_WIDTH - BLOCK_WIDTH) / 2,
                            y_coord + (TILE_HEIGHT - BLOCK_HEIGHT) / 2);
                }
            }
        }

        // spawn walls
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;

                // add walls
                Coordinate c = new Coordinate(i, j);
                if (topWalls.containsKey(c)) {
                    addObject(new HorizontalWall(), x_coord - 5, y_coord - 5);
                }
            }
        }
    }

    @Override
    public void act() {
        int BLOCKS_SPAWNED_PER_MOVE = 10;
        // listen for key presses and act accordingly
        if (keyPresssed(Keyboard.KEY_UP)) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    col.add(grid[i][j]);
                }

                ArrayList<Block> result = check(col);
                Collections.reverse(result);
                // add empty blocks
                result.addAll(result.size(), Arrays.asList(new Block[GRID_HEIGHT - result.size()]));

                // put in grid
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    grid[i][j] = result.get(i);
                }
            }

            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();
            renderGrid();

        } else if (keyPresssed(Keyboard.KEY_DOWN)) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
                    col.add(grid[i][j]);
                }

                ArrayList<Block> result = check(col);
                // add empty blocks
                result.addAll(0, Arrays.asList(new Block[GRID_HEIGHT - result.size()]));

                // put in grid
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    grid[i][j] = result.get(i);
                }
            }
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();
            renderGrid();

        } else if (keyPresssed(Keyboard.KEY_LEFT)) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                for (int j = 0; j < GRID_WIDTH; j++) {
                    row.add(grid[i][j]);
                }

                ArrayList<Block> result = check(row);
                Collections.reverse(result);
                // add empty blocks
                result.addAll(result.size(), Arrays.asList(new Block[GRID_HEIGHT - result.size()]));

                // put in grid
                for (int j = 0; j < GRID_HEIGHT; j++) {
                    grid[i][j] = result.get(j);
                }
            }
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();
            renderGrid();

        } else if (keyPresssed(Keyboard.KEY_RIGHT)) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                for (int j = GRID_WIDTH - 1; j >= 0; j--) {
                    row.add(grid[i][j]);
                }

                ArrayList<Block> result = check(row);
                // add empty blocks
                result.addAll(0, Arrays.asList(new Block[GRID_HEIGHT - result.size()]));

                // put in grid
                for (int j = 0; j < GRID_HEIGHT; j++) {
                    grid[i][j] = result.get(j);
                }
            }
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();
            renderGrid();

        }

        if (keyPresssed(Keyboard.KEY_SPACE)) {
            System.out.println("Space key pressed");
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();
        }

    }

    // Get list of arrays
    public ArrayList<Block> check(ArrayList<Block> blocks) {
        Stack<Block> stack = new Stack<Block>();

        for (Block b : blocks) {
            Block currentBlock = b;
            if (currentBlock == null)
                continue;

            currentBlock.setColor(BColor.RED);
            while (!stack.empty() && stack.peek().getValue() == currentBlock.getValue()) {
                Block top = stack.pop();
                Block newBlock = new Block(top.getValue() * 2, BColor.RED);
                currentBlock = newBlock;
            }
            stack.add(currentBlock);
        }

        ArrayList<Block> result = new ArrayList<Block>();
        // add stack elements to result array
        while (!stack.empty())
            result.add(stack.pop());

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

    public void spawnRandomBlock() {
        double spawnValue = Math.random();
        int value;
        if (spawnValue <= 0.7) // 70% chance
            value = 2;
        else if (spawnValue <= 0.9) // 20% chance
            value = 4;
        else // 10% chance
            value = 8;

        Coordinate g = getRandomEmptyTile();
        grid[g.getRow()][g.getCol()] = new Block(value, BColor.BLUE);
    }

    /**
     * Returns whether or not a key was newly pressed down on this frame
     * 
     * @param key a KEYBOARD constant that represents which key to check for
     * @return true if the key was newly pressed down on the frame, false otherwise
     */
    private boolean keyPresssed(int key) {
        return Mayflower.isKeyDown(key) && !Mayflower.wasKeyDown(key);
    }

}

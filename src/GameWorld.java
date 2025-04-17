import mayflower.*;
import java.util.*;

public class GameWorld extends World {
    private Block[][] grid;

    /**
     * The walls of the grid. The location of the walls is stored as a coordinate of
     * the block it is relative to.
     */
    private Set<Coordinate> topWalls, leftWalls;

    /**
     * The dimensions (in tiles) of the grid
     */
    public static int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public static int OFFSET_X = 45, OFFSET_Y = 250;
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;

    private TurnGraphic turnGraph = new TurnGraphic(true);
    public int RED_SCORE = 0;
    public int BLUE_SCORE = 0;

    /**
     * Which players turn it is.
     * 
     * If FALSE, then it is BLUE's turn. If TRUE, then it
     * is RED's turn.
     */
    public static boolean turn;

    public GameWorld() {
        grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        topWalls = new HashSet<Coordinate>();
        leftWalls = new HashSet<Coordinate>();
        turn = true; // blue goes first

        addWalls();
        addObject(new Title(), 20, 20);
        addObject(new GridBorder(), 40, 245);
        addObject(new Title(), 301, 55);
        addObject(turnGraph, 40, 55);
        spawnRandomBlocks(10);

        renderGrid();
    }

    /**
     * Adds walls to the grid. This method is called when the game is first created.
     * 
     * @return void
     */
    private void addWalls() {
        topWalls.add(new Coordinate(4, 0));
        leftWalls.add(new Coordinate(0, 3));
    }

    /**
     * Renders the grid and all blocks in the grid to the screen
     * 
     * @return void
     */
    private void renderGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_HEIGHT) + OFFSET_Y;

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

        // spawn top walls
        for (Coordinate c : topWalls) {
            int x_coord = (c.getCol() * TILE_WIDTH) + OFFSET_X;
            int y_coord = (c.getRow() * TILE_HEIGHT) + OFFSET_Y;
            addObject(new HorizontalWall(), x_coord - (TILE_WIDTH - BLOCK_WIDTH) / 2, y_coord - (TILE_HEIGHT
                    - BLOCK_HEIGHT) / 2);
        }

        // spawn left walls
        for (Coordinate c : leftWalls) {
            int x_coord = (c.getCol() * TILE_WIDTH) + OFFSET_X;
            int y_coord = (c.getRow() * TILE_HEIGHT) + OFFSET_Y;
            addObject(new VerticalWall(), x_coord - (TILE_WIDTH - BLOCK_WIDTH) / 2, y_coord - (TILE_HEIGHT
                    - BLOCK_HEIGHT) / 2);
        }

        // update score text
        showText("score blue: " + BLUE_SCORE, 550, 55, Color.BLACK);
        showText("score red: " + RED_SCORE, 550, 110, Color.BLACK);
    }

    private void swapActivePlayer() {
        turn = !turn;
        if (!turn)
            turnGraph.setImage("src/img/turnBoardRed.png");
        else
            turnGraph.setImage("src/img/turnBoardBlue.png");
    }

    @Override
    public void act() {
        int BLOCKS_SPAWNED_PER_MOVE = 3;

        // listen for key presses and act accordingly
        if (keyPresssed(Keyboard.KEY_UP)) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int i = 0; i <= GRID_HEIGHT; i++) {
                    // when there's a wall, merge what we already have
                    if (topWalls.contains(new Coordinate(i, j)) || i == GRID_HEIGHT) {
                        ArrayList<Block> toAdd = merge(col);
                        result.addAll(toAdd);

                        // add empty blocks
                        result.addAll(Arrays.asList(new Block[i - result.size()]));

                        col.clear();

                    }

                    if (i < GRID_HEIGHT && grid[i][j] != null) {
                        col.add(grid[i][j]);
                    }
                }

                // put in grid
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    grid[i][j] = result.get(i);
                }
            }

            spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
            renderGrid();
            swapActivePlayer();

        } else if (keyPresssed(Keyboard.KEY_DOWN)) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
                    if (grid[i][j] != null) {
                        col.add(grid[i][j]);
                    }

                    if (topWalls.contains(new Coordinate(i, j)) || i == 0) {
                        ArrayList<Block> toAdd = merge(col);
                        // add empty blocks
                        result.addAll(toAdd);
                        result.addAll(Arrays.asList(new Block[GRID_HEIGHT - i - result.size()]));

                        col.clear();
                    }
                }

                Collections.reverse(result);

                // put in grid
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    grid[i][j] = result.get(i);
                }
            }

            spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
            renderGrid();
            swapActivePlayer();

        } else if (keyPresssed(Keyboard.KEY_LEFT)) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int j = 0; j <= GRID_WIDTH; j++) {
                    if (leftWalls.contains(new Coordinate(i, j)) || j == GRID_WIDTH) {
                        ArrayList<Block> toAdd = merge(row);
                        result.addAll(toAdd);
                        // add empty blocks
                        result.addAll(Arrays.asList(new Block[j - result.size()]));
                        row.clear();
                    }

                    if (j < GRID_WIDTH && grid[i][j] != null) {
                        row.add(grid[i][j]);
                    }
                }

                // put in grid
                for (int j = 0; j < GRID_HEIGHT; j++) {
                    grid[i][j] = result.get(j);
                }
            }

            spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
            renderGrid();
            swapActivePlayer();

        } else if (keyPresssed(Keyboard.KEY_RIGHT)) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int j = GRID_WIDTH - 1; j >= 0; j--) {
                    if (grid[i][j] != null) {
                        row.add(grid[i][j]);
                    }

                    if (leftWalls.contains(new Coordinate(i, j)) || j == 0) {
                        ArrayList<Block> toAdd = merge(row);
                        result.addAll(toAdd);
                        // add empty blocks
                        result.addAll(Arrays.asList(new Block[GRID_WIDTH - j - result.size()]));
                        row.clear();
                    }
                }

                Collections.reverse(result);

                // put in grid
                for (int j = 0; j < GRID_HEIGHT; j++) {
                    grid[i][j] = result.get(j);
                }
            }

            spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
            renderGrid();
            swapActivePlayer();

        }
    }

    /**
     * Merges blocks in the given array. The blocks are merged from left to right.
     * Elements will always be merged to the largest possible element.
     * 
     * @param blocks
     * @return the merged blocks
     */
    public ArrayList<Block> merge(ArrayList<Block> blocks) {
        Stack<Block> stack = new Stack<Block>();

        for (Block b : blocks) {
            Block currentBlock = b;
            if (currentBlock == null)
                continue;

            currentBlock.setColor(BColor.RED);
            while (!stack.empty() && stack.peek().getValue() == currentBlock.getValue()) {
                Block top = stack.pop();
                int newValue = top.getValue() * 2;
                BColor newColor;

                if (turn) { // if BLUE's turn
                    newColor = BColor.BLUE;
                    BLUE_SCORE += newValue;
                } else {
                    newColor = BColor.RED;
                    RED_SCORE += newValue;
                }

                Block newBlock = new Block(newValue, newColor);
                currentBlock = newBlock;
            }
            stack.add(currentBlock);

        }

        ArrayList<Block> result = new ArrayList<Block>();
        // add stack elements to result array
        while (!stack.empty())
            result.add(stack.pop());

        Collections.reverse(result);
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

    public void spawnRandomBlocks(int numBlocks) {
        double spawnValue = Math.random();
        int value = -1;
        if (spawnValue <= 0.7) // 70% chance
            value = 2;
        else if (spawnValue <= 0.9) // 20% chance
            value = 4;
        else // 10% chance
            value = 8;

        ArrayList<Coordinate> empty = getEmptyTiles();
        Collections.shuffle(empty);
        for (int i = 0; i < numBlocks && i < empty.size(); i++) {
            Coordinate g = empty.get(i);
            grid[g.getRow()][g.getCol()] = new Block(value, BColor.NEUTRAL);
        }
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

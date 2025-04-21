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

    /**
     * The dimensions (in pixels) of the tiles (each square in the grid)
     */
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;

    /**
     * The dimensions (in pixels) of the blocks (tiles without the borders)
     */
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;

    private TurnGraphic turnGraph;
    private int RED_SCORE = 0;
    private int BLUE_SCORE = 0;

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
        // addObject(new Title(), 20, 20);
        addObject(new GridBorder(), 40, 245);
        addObject(new Title(), 301, 55);
        turnGraph = new TurnGraphic(turn);
        addObject(turnGraph, 40, 55);

        grid[1][0] = new Block(2, BColor.BLUE);
        grid[2][0] = new Block(2, BColor.BLUE);
        grid[3][0] = new Block(4, BColor.BLUE);
        grid[4][0] = new Block(2, BColor.BLUE);
        grid[5][0] = new Block(2, BColor.BLUE);

        spawnRandomBlocks(10);
        renderGrid();
    }

    /**
     * Adds walls to the grid. This method should be called when the game world is
     * first created.
     * 
     * @return void
     */
    private void addWalls() {
        // topWalls.add(new Coordinate(4, 0));
        // leftWalls.add(new Coordinate(0, 3));
    }

    /**
     * Renders the grid. Inclues all tiles, blocks, walls in the grid, plus the
     * score text. Should be called whenever game state is changed.
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

    /**
     * Swaps who can make a move. Updates turn graphic accordingly.
     * 
     * @return void
     */
    private void swapActivePlayer() {
        turn = !turn;
        turnGraph.setTurn(turn);
    }

    @Override
    public void act() {
        int BLOCKS_SPAWNED_PER_MOVE = 3;
        double ANIMATION_TIME = 150;

        // listen for key presses and act accordingly
        if (keyPresssed(Keyboard.KEY_UP)) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int i = 0; i <= GRID_HEIGHT; i++) {
                    // when there's a wall, merge what we already have
                    if (topWalls.contains(new Coordinate(i, j)) || i == GRID_HEIGHT) {
                        ArrayList<Block> toAdd = merge(col, true, j);
                        result.addAll(toAdd);

                        // add empty blocks
                        result.addAll(Arrays.asList(new Block[i - result.size()]));

                        // update velocities of blocks
                        for (Block b : col) {
                            if (b == null)
                                continue;

                            double x0 = b.getY();
                            double xf = b.getTargetDestination().getRow() * TILE_HEIGHT + OFFSET_Y
                                    + (TILE_HEIGHT - BLOCK_HEIGHT) / 2;
                            double acceleration = 2 * (xf - x0) / (ANIMATION_TIME * ANIMATION_TIME);

                            b.setAy(acceleration);
                        }

                        // reset for the next batch
                        col.clear();
                    }

                    if (i < GRID_HEIGHT) {
                        col.add(grid[i][j]);
                    }
                }

                // put in grid
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    grid[i][j] = result.get(i);
                }
            }

            spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
            // renderGrid();
            swapActivePlayer();

        }
        // else if (keyPresssed(Keyboard.KEY_DOWN)) {
        // for (int j = 0; j < GRID_WIDTH; j++) {
        // ArrayList<Block> col = new ArrayList<Block>();
        // ArrayList<Block> result = new ArrayList<Block>();

        // for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
        // col.add(grid[i][j]);

        // if (topWalls.contains(new Coordinate(i, j)) || i == 0) {
        // ArrayList<Block> toAdd = merge(col);
        // // add empty blocks
        // result.addAll(toAdd);
        // result.addAll(Arrays.asList(new Block[GRID_HEIGHT - i - result.size()]));

        // col.clear();
        // }
        // }

        // Collections.reverse(result);

        // // put in grid
        // for (int i = 0; i < GRID_HEIGHT; i++) {
        // grid[i][j] = result.get(i);
        // }
        // }

        // spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
        // renderGrid();
        // swapActivePlayer();

        // } else if (keyPresssed(Keyboard.KEY_LEFT)) {
        // for (int i = 0; i < GRID_HEIGHT; i++) {
        // ArrayList<Block> row = new ArrayList<Block>();
        // ArrayList<Block> result = new ArrayList<Block>();

        // for (int j = 0; j <= GRID_WIDTH; j++) {
        // if (leftWalls.contains(new Coordinate(i, j)) || j == GRID_WIDTH) {
        // ArrayList<Block> toAdd = merge(row);
        // result.addAll(toAdd);
        // // add empty blocks
        // result.addAll(Arrays.asList(new Block[j - result.size()]));
        // row.clear();
        // }

        // if (j < GRID_WIDTH) {
        // row.add(grid[i][j]);
        // }
        // }

        // // put in grid
        // for (int j = 0; j < GRID_HEIGHT; j++) {
        // grid[i][j] = result.get(j);
        // }
        // }

        // spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
        // renderGrid();
        // swapActivePlayer();

        // } else if (keyPresssed(Keyboard.KEY_RIGHT)) {
        // for (int i = 0; i < GRID_HEIGHT; i++) {
        // ArrayList<Block> row = new ArrayList<Block>();
        // ArrayList<Block> result = new ArrayList<Block>();

        // for (int j = GRID_WIDTH - 1; j >= 0; j--) {
        // row.add(grid[i][j]);

        // if (leftWalls.contains(new Coordinate(i, j)) || j == 0) {
        // ArrayList<Block> toAdd = merge(row);
        // result.addAll(toAdd);
        // // add empty blocks
        // result.addAll(Arrays.asList(new Block[GRID_WIDTH - j - result.size()]));
        // row.clear();
        // }
        // }

        // Collections.reverse(result);

        // // put in grid
        // for (int j = 0; j < GRID_HEIGHT; j++) {
        // grid[i][j] = result.get(j);
        // }
        // }

        // spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
        // renderGrid();
        // swapActivePlayer();

        // }
    }

    /**
     * Merges blocks in the given array. The blocks are merged from left to right.
     * Elements will always be merged to the largest possible element.
     * 
     * @param blocks The line of blocks to merge. Any {@code null} values are
     *               considered empty tiles.
     * @return An {@code ArrayList} of merged blocks. All empty blocks will have
     *         been removed.
     */
    public ArrayList<Block> merge(ArrayList<Block> blocks, boolean isVertical, int index) {
        Stack<Block> stack = new Stack<Block>();
        // index in row -> new index in row
        Map<Integer, Integer> positionShifts = new HashMap<Integer, Integer>();

        for (int i = 0; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
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

            positionShifts.put(i, stack.size() - 1);
        }

        ArrayList<Block> result = new ArrayList<Block>();
        // add stack elements to result array
        while (!stack.empty())
            result.add(stack.pop());

        Collections.reverse(result);

        // update coordinates of blocks
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == null)
                continue;

            Block b = blocks.get(i);
            int target = positionShifts.get(i);
            if (isVertical) {
                b.setTargetDestination(new Coordinate(target, index));
            } else {
                b.setTargetDestination(new Coordinate(index, target));
            }
        }

        return result;
    }

    private ArrayList<Coordinate> getEmptyTiles() {
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

    /**
     * Spawn a certain amount of blocks into the grid. Blocks will be randomly
     * placed into empty tiles. If there are no more empty tiles, nothing will
     * happen.
     * 
     * @param numBlocks number of blocks to spawn in
     */
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
            // grid[g.getRow()][g.getCol()] = new Block(value, BColor.NEUTRAL);
        }
    }

    /**
     * Returns whether or not a key was newly pressed down on this frame
     * 
     * @param key A {@code Mayflower.KEYBOARD} constant that represents which key to
     *            check for
     * @return {@code true} if the key was newly pressed down on the frame,
     *         {@code false} otherwise
     */
    private boolean keyPresssed(int key) {
        return Mayflower.isKeyDown(key) && !Mayflower.wasKeyDown(key);
    }

}

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

    public static int BLOCK_BORDER_WIDTH = (TILE_WIDTH - BLOCK_WIDTH) / 2, BLOCK_BORDER_HEIGHT = (TILE_HEIGHT
            - BLOCK_HEIGHT) / 2;

    private TurnGraphic turnGraph;
    private int RED_SCORE = 0;
    private int BLUE_SCORE = 0;

    /**
     * The number of randomly spawned blocks spawned every move.
     */
    private int BLOCKS_SPAWNED_PER_MOVE = 3;

    /**
     * Which players turn it is.
     * 
     * If FALSE, then it is BLUE's turn. If TRUE, then it
     * is RED's turn.
     */
    public static boolean turn;

    private ArrayList<Block> mergingStillBlocks = new ArrayList<Block>();
    private Set<Block> currentlyMovingBlocks = new HashSet<Block>();

    public GameWorld() {
        grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        topWalls = new HashSet<Coordinate>();
        leftWalls = new HashSet<Coordinate>();
        turn = true; // blue goes first

        addObject(new GridBorder(), 40, 245);
        addObject(new Title(), 301, 55);
        turnGraph = new TurnGraphic(turn);
        addObject(turnGraph, 40, 55);

        // grid[0][0] = new Block(16, BColor.BLUE);
        grid[0][4] = new Block(2, BColor.BLUE);
        grid[1][4] = new Block(2, BColor.BLUE);
        grid[9][4] = new Block(4, BColor.BLUE);
        grid[9][3] = new Block(4, BColor.RED);
        // grid[0][4] = new Block(2, BColor.BLUE);
        // grid[0][5] = new Block(2, BColor.BLUE);

        // spawnRandomBlocks(10);
        addWalls();
        renderBaseGrid();
        renderGrid();
    }

    /**
     * Adds walls to the grid. This method should be called when the game world is
     * first created.
     * 
     * @return void
     */
    private void addWalls() {
        topWalls.add(new Coordinate(4, 0));
        leftWalls.add(new Coordinate(0, 3));
    }

    /**
     * Renders the base grid. This includes all tiles and walls in the grid. This
     * method should be called when the game world is first created.
     */
    private void renderBaseGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int[] converted = convertToPixels(new Coordinate(i, j));
                int x_coord = converted[0], y_coord = converted[1];

                // add tiles
                addObject(new Tile(), x_coord, y_coord);
            }
        }

        // spawn top walls
        for (Coordinate c : topWalls) {
            int[] converted = convertToPixels(c);
            int x_coord = converted[0], y_coord = converted[1];
            addObject(new HorizontalWall(), x_coord - BLOCK_BORDER_WIDTH, y_coord - BLOCK_BORDER_HEIGHT);
        }

        // spawn left walls
        for (Coordinate c : leftWalls) {
            int[] converted = convertToPixels(c);
            int x_coord = converted[0], y_coord = converted[1];
            addObject(new VerticalWall(), x_coord - BLOCK_BORDER_WIDTH, y_coord - BLOCK_BORDER_HEIGHT);
        }
    }

    /**
     * Renders the grid. Inclues only blocks in the grid, plus the score text.
     * Should be called whenever game state is changed.
     */
    public void renderGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int[] converted = convertToPixels(new Coordinate(i, j));
                int x_coord = converted[0], y_coord = converted[1];

                // add blocks
                Block currBlock = grid[i][j];
                if (currBlock != null) {
                    addObject(currBlock, x_coord + BLOCK_BORDER_WIDTH, y_coord + BLOCK_BORDER_HEIGHT);
                }
            }
        }

        // update score text
        showText("score blue: " + BLUE_SCORE, 550, 55, Color.BLACK);
        showText("score red: " + RED_SCORE, 550, 110, Color.BLACK);
    }

    /**
     * Converts a coordinate in the grid to a pixel coordinate on the screen.
     * 
     * @param c the {@code Coordinate} representation of the position in the grid to
     *          convert.
     * @return an array of pixel coordinates. The first element is the X coordinate,
     *         and the second element is the Y coordinate.
     */
    public static int[] convertToPixels(Coordinate c) {
        int col_coord = (c.getCol() * TILE_WIDTH) + OFFSET_X;
        int row_coord = (c.getRow() * TILE_HEIGHT) + OFFSET_Y;
        return new int[] { col_coord, row_coord };
    }

    public static double[] calculateBlockPixel(Coordinate c) {
        double x = (c.getCol() * TILE_WIDTH) + OFFSET_X + BLOCK_BORDER_WIDTH;
        double y = (c.getRow() * TILE_HEIGHT) + OFFSET_Y + BLOCK_BORDER_HEIGHT;
        return new double[] { x, y };
    }

    /**
     * Swaps who can make a move. Updates turn graphic accordingly.
     */
    private void swapActivePlayer() {
        turn = !turn;
        turnGraph.setTurn(turn);
    }

    public void removeMovingBlock(Block b) {
        currentlyMovingBlocks.remove(b);
        removeObject(b);
        // System.out.println(currentlyMovingBlocks.size() + " left");

        // triggers once when all animations have been completed
        if (currentlyMovingBlocks.isEmpty()) {
            // remove all still blocks that need to be merged
            for (Block still : mergingStillBlocks)
                removeObject(still);
            mergingStillBlocks.clear();

            spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);
            swapActivePlayer();
            renderGrid();
        }
    }

    @Override
    public void act() {
        // if in the middle of an animation, disregard key presses
        if (!currentlyMovingBlocks.isEmpty())
            return;

        // listen for key presses and act accordingly
        if (keyPresssed(Keyboard.KEY_UP)) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();
                int startOfChain = 0;

                for (int i = 0; i <= GRID_HEIGHT; i++) {
                    // when there's a wall, merge what we already have
                    if (topWalls.contains(new Coordinate(i, j)) || i == GRID_HEIGHT) {
                        ArrayList<Block> toAdd = mergeLine(col, j, startOfChain, true, false);
                        result.addAll(toAdd);

                        // add empty blocks
                        result.addAll(Arrays.asList(new Block[i - result.size()]));

                        updateBlockVelocities(col, true);

                        // reset for the next batch
                        col.clear();
                        startOfChain = i;
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

        } else if (keyPresssed(Keyboard.KEY_DOWN)) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();
                int startOfChain = GRID_HEIGHT;

                for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
                    col.add(grid[i][j]);

                    if (topWalls.contains(new Coordinate(i, j)) || i == 0) {
                        ArrayList<Block> toAdd = mergeLine(col, j, startOfChain, true, true);
                        // add empty blocks
                        result.addAll(toAdd);
                        result.addAll(Arrays.asList(new Block[GRID_HEIGHT - i - result.size()]));

                        updateBlockVelocities(col, true);

                        col.clear();
                        startOfChain = i;
                    }
                }

                Collections.reverse(result);

                // put in grid
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    grid[i][j] = result.get(i);
                }
            }

        } else if (keyPresssed(Keyboard.KEY_LEFT)) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();
                int startOfChain = 0;

                for (int j = 0; j <= GRID_WIDTH; j++) {
                    if (leftWalls.contains(new Coordinate(i, j)) || j == GRID_WIDTH) {
                        ArrayList<Block> toAdd = mergeLine(row, i, startOfChain, false, false);
                        result.addAll(toAdd);
                        // add empty blocks
                        result.addAll(Arrays.asList(new Block[j - result.size()]));

                        updateBlockVelocities(row, false);

                        row.clear();
                        startOfChain = j;
                    }

                    if (j < GRID_WIDTH) {
                        row.add(grid[i][j]);
                    }
                }

                // put in grid
                for (int j = 0; j < GRID_WIDTH; j++) {
                    grid[i][j] = result.get(j);
                }
            }

        }
        // else if (keyPresssed(Keyboard.KEY_RIGHT)) {
        // for (int i = 0; i < GRID_HEIGHT; i++) {
        // ArrayList<Block> row = new ArrayList<Block>();
        // ArrayList<Block> result = new ArrayList<Block>();

        // for (int j = GRID_WIDTH - 1; j >= 0; j--) {
        // row.add(grid[i][j]);

        // if (leftWalls.contains(new Coordinate(i, j)) || j == 0) {
        // ArrayList<Block> toAdd = mergeLine(row);
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

    private void updateBlockVelocities(ArrayList<Block> line, boolean isVertical) {
        double ANIMATION_TIME = 8.5;
        double initialVelocity = 0;
        // ANIMATION_TIME = 200;

        // update velocities of blocks
        for (Block b : line) {
            if (b == null || b.getTargetDestination() == null)
                continue;

            // calculate acceleration and set it
            if (isVertical) {
                double initialPos = b.getY();
                double finalPos = convertToPixels(b.getTargetDestination())[1] + BLOCK_BORDER_HEIGHT;
                double acceleration = 2 * (finalPos - initialPos - initialVelocity * ANIMATION_TIME)
                        / (ANIMATION_TIME * ANIMATION_TIME);

                b.setAy(acceleration);

                // System.out.println(
                // "initialPos: " + initialPos + ", finalPos: (" +
                // b.getTargetDestination().getCol() + ","
                // + b.getTargetDestination().getRow() + ") acceleration: " + acceleration);
            } else {
                double initialPos = b.getX();
                double finalPos = convertToPixels(b.getTargetDestination())[0] + BLOCK_BORDER_WIDTH;
                double acceleration = 2 * (finalPos - initialPos) / (ANIMATION_TIME * ANIMATION_TIME);

                b.setAx(acceleration);

                // System.out.println(
                // "initialPos: " + initialPos + ", finalPos: (" +
                // b.getTargetDestination().getCol() + ","
                // + b.getTargetDestination().getRow() + ") acceleration: " + acceleration);
            }

            currentlyMovingBlocks.add(b);

        }
    }

    /**
     * Merges blocks in the given array. The blocks are merged from left to right.
     * Elements will always be merged to the largest possible element.
     * 
     * @param blocks      The line of blocks to merge. Any {@code null} values are
     *                    considered empty tiles.
     * @param isVertical  Whether or not the blocks are moving vertically or not.
     * @param lineIndex   The index of the line in the larger grid that is being
     *                    merged.
     * @param offsetIndex The index of how far up the line the merge starts. This is
     *                    used for when we merge multiple times in one
     *                    row/col because of walls.
     * @return An {@code ArrayList} of merged blocks. All empty blocks will have
     *         been removed.
     */
    public ArrayList<Block> mergeLine(ArrayList<Block> blocks, int lineIndex, int offsetIndex, boolean isVertical,
            boolean isBackwards) {
        Stack<Block> stack = new Stack<Block>();
        // index in row -> new index in row
        Map<Block, Integer> positionShifts = new HashMap<Block, Integer>();

        for (int i = 0; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
            if (currentBlock == null)
                continue;

            currentBlock.setColor(BColor.RED);

            while (!stack.empty() && stack.peek().getValue() == currentBlock.getValue()) {
                Block top = stack.pop();
                positionShifts.put(top, Math.max(stack.size() - 1, 0));
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
            positionShifts.put(blocks.get(i), stack.size() - 1);
        }

        ArrayList<Block> result = new ArrayList<Block>();
        // add stack elements to result array
        while (!stack.empty())
            result.add(stack.pop());

        Collections.reverse(result);

        // update destination coordinates for block animations
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == null)
                continue;

            Block b = blocks.get(i);
            int target = -1;
            if (!isBackwards)
                target = positionShifts.get(b) + offsetIndex;
            else if (isBackwards && isVertical) {
                target = offsetIndex - positionShifts.get(b) - 1;
            }

            // if the block is already in the right place
            if ((!isBackwards && target == i + offsetIndex) || (isBackwards && target == offsetIndex - i - 1)) {
                b.setTargetDestination(null);

                // only delete the object if it is going to be merged into something
                if (blocks.get(i).getValue() != result.get(i).getValue()) {
                    // removeObject(b);
                    mergingStillBlocks.add(b);
                }

                continue;
            } else if (isVertical) {
                b.setTargetDestination(new Coordinate(target, lineIndex));
            } else if (!isVertical) {
                b.setTargetDestination(new Coordinate(lineIndex, target));
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
            grid[g.getRow()][g.getCol()] = new Block(value, BColor.BLUE);
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

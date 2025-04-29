import java.util.*;

/**
 * Represents the game logic and data for the multiplayer game.
 * This class manages the game grid, player turns, scores, and block merging
 * logic.
 */
public class Game {
    private int GRID_WIDTH = GameWorld.GRID_WIDTH, GRID_HEIGHT = GameWorld.GRID_HEIGHT;

    /**
     * The players' scores in game.
     */
    private int blueScore = 0, redScore = 0;

    /**
     * A 2D representation of the grid.
     */
    private MovableGridItem[][] grid;

    /**
     * The Color of the current player
     */
    private BColor currentPlayer;

    /**
     * The player's color (the one represented by this instance of {@code Game}).
     */
    private BColor myColor;

    /**
     * An {@code ArrayList} of all blocks that are part of a merging chain, but are
     * not still. This is used so that the blocks that are still disappear at the
     * same time at the blocks that are moving.
     */
    private ArrayList<MovableGridItem> mergingStillBlocks = new ArrayList<>();

    /**
     * A {@code Set} of all blocks that need to animate to another tile to merge.
     * This is used to keep track of ongoing and completed animations.
     */
    private Set<MovableGridItem> currentlyMovingBlocks = new HashSet<>();

    /**
     * The walls of the grid. The location of the walls is stored as coordinates
     * relative to the blocks.
     */
    private Set<Coordinate> topWalls, leftWalls;

    /**
     * Constructs a new game instance and initializes the grid and walls.
     */
    public Game() {
        this.grid = new MovableGridItem[GRID_HEIGHT][GRID_WIDTH];
        this.currentPlayer = BColor.BLUE;
        topWalls = new HashSet<>();
        leftWalls = new HashSet<>();
        addWalls();
    }

    /**
     * Adds walls to the grid. This method should be called when the game world is
     * first created.
     */
    private void addWalls() {
        topWalls.add(new Coordinate(1, 3));
        topWalls.add(new Coordinate(3, 0));
        topWalls.add(new Coordinate(5, 2));
        topWalls.add(new Coordinate(7, 4));
        topWalls.add(new Coordinate(9, 0));

        leftWalls.add(new Coordinate(0, 9));
        leftWalls.add(new Coordinate(3, 7));
        leftWalls.add(new Coordinate(2, 4));
        leftWalls.add(new Coordinate(5, 2));
        leftWalls.add(new Coordinate(7, 8));
    }

    /**
     * Swaps the active player.
     *
     * @return The {@code BColor} of the new active player.
     */
    public BColor swapActivePlayer() {
        if (this.currentPlayer == BColor.BLUE) {
            this.currentPlayer = BColor.RED;
        } else if (this.currentPlayer == BColor.RED) {
            this.currentPlayer = BColor.BLUE;
        }
        return this.currentPlayer;
    }

    /**
     * Checks if the game is over.
     *
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public boolean isGameOver() {
        return (blueScore >= 4096 || redScore >= 4096);
    }

    /**
     * Gets the winner of the game.
     *
     * @return The color of the winning player, or {@code NEUTRAL} if no winner.
     */
    public BColor getWinner() {
        if (isGameOver()) {
            if (blueScore >= 4096) {
                return BColor.BLUE;
            } else if (redScore >= 4096) {
                return BColor.RED;
            }
        }
        return BColor.NEUTRAL;
    }

    /**
     * Shifts all blocks on the grid up. Should be called when the UP arrow key is
     * pressed.
     */
    private void shiftBlocksUp() {
        for (int j = 0; j < GRID_WIDTH; j++) {
            ArrayList<MovableGridItem> col = new ArrayList<MovableGridItem>();
            ArrayList<MovableGridItem> result = new ArrayList<MovableGridItem>();
            int startOfChain = 0;

            for (int i = 0; i <= GRID_HEIGHT; i++) {
                // when there's a wall, merge what we already have
                if (topWalls.contains(new Coordinate(i, j)) || i == GRID_HEIGHT) {
                    ArrayList<MovableGridItem> toAdd = mergeLine(col, j, startOfChain, true, false);
                    result.addAll(toAdd);

                    // add empty blocks
                    result.addAll(Arrays.asList(new MovableGridItem[i - result.size()]));

                    updateBlockAccelerations(col, true);

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
    }

    /**
     * Shifts all blocks on the grid down. Should be called when the DOWN arrow key
     * is pressed.
     */
    private void shiftBlocksDown() {
        for (int j = 0; j < GRID_WIDTH; j++) {
            ArrayList<MovableGridItem> col = new ArrayList<MovableGridItem>();
            ArrayList<MovableGridItem> result = new ArrayList<MovableGridItem>();
            int startOfChain = GRID_HEIGHT;

            for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
                col.add(grid[i][j]);

                if (topWalls.contains(new Coordinate(i, j)) || i == 0) {
                    ArrayList<MovableGridItem> toAdd = mergeLine(col, j, startOfChain, true, true);
                    // add empty blocks
                    result.addAll(toAdd);
                    result.addAll(Arrays.asList(new MovableGridItem[GRID_HEIGHT - i - result.size()]));

                    updateBlockAccelerations(col, true);

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
    }

    /**
     * Shifts all blocks on the grid left. Should be called when the LEFT arrow key
     * is pressed.
     */
    private void shiftBlocksLeft() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            ArrayList<MovableGridItem> row = new ArrayList<MovableGridItem>();
            ArrayList<MovableGridItem> result = new ArrayList<MovableGridItem>();
            int startOfChain = 0;

            for (int j = 0; j <= GRID_WIDTH; j++) {
                if (leftWalls.contains(new Coordinate(i, j)) || j == GRID_WIDTH) {
                    ArrayList<MovableGridItem> toAdd = mergeLine(row, i, startOfChain, false, false);
                    result.addAll(toAdd);
                    // add empty blocks
                    result.addAll(Arrays.asList(new MovableGridItem[j - result.size()]));

                    updateBlockAccelerations(row, false);

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

    /**
     * Shifts all blocks on the grid right. Should be called when the RIGHT arrow
     * key is pressed.
     */
    private void shiftBlocksRight() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            ArrayList<MovableGridItem> row = new ArrayList<MovableGridItem>();
            ArrayList<MovableGridItem> result = new ArrayList<MovableGridItem>();
            int startOfChain = GRID_WIDTH;

            for (int j = GRID_WIDTH - 1; j >= 0; j--) {
                row.add(grid[i][j]);

                if (leftWalls.contains(new Coordinate(i, j)) || j == 0) {
                    ArrayList<MovableGridItem> toAdd = mergeLine(row, i, startOfChain, false, true);
                    result.addAll(toAdd);
                    // add empty blocks
                    result.addAll(Arrays.asList(new MovableGridItem[GRID_WIDTH - j - result.size()]));

                    updateBlockAccelerations(row, false);

                    row.clear();
                    startOfChain = j;
                }
            }

            Collections.reverse(result);

            // put in grid
            for (int j = 0; j < GRID_HEIGHT; j++) {
                grid[i][j] = result.get(j);
            }
        }
    }

    /**
     * Calculates and updates each {@code MovableGridItem}'s acceleration based on
     * the current and target destination. Adds all moving blocks to
     * {@code currentlyMovingBlocks}.
     * 
     * @param line       The line of the grid to update
     * @param isVertical Whether or not the line is vertical or not
     */
    private void updateBlockAccelerations(ArrayList<MovableGridItem> line, boolean isVertical) {
        double ANIMATION_TIME = 8.5;
        double initialVelocity = 0;

        // update velocities of blocks
        for (MovableGridItem b : line) {
            if (b == null || b.getTargetDestination() == null)
                continue;

            // calculate acceleration and set it
            if (isVertical) {
                double initialPos = b.getY();
                double finalPos = GameWorld.calculateBlockPixel(b.getTargetDestination()).getY();
                double acceleration = 2 * (finalPos - initialPos - initialVelocity * ANIMATION_TIME)
                        / (ANIMATION_TIME * ANIMATION_TIME);

                b.setAy(acceleration);
            } else {
                double initialPos = b.getX();
                double finalPos = GameWorld.calculateBlockPixel(b.getTargetDestination()).getX();
                double acceleration = 2 * (finalPos - initialPos) / (ANIMATION_TIME * ANIMATION_TIME);

                b.setAx(acceleration);
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
    public ArrayList<MovableGridItem> mergeLine(ArrayList<MovableGridItem> blocks, int lineIndex, int offsetIndex,
            boolean isVertical,
            boolean isBackwards) {
        Stack<MovableGridItem> stack = new Stack<MovableGridItem>();
        // index in row -> new index in row
        Map<MovableGridItem, Integer> positionShifts = new HashMap<MovableGridItem, Integer>();

        for (int i = 0; i < blocks.size(); i++) {
            MovableGridItem curr = blocks.get(i);
            if (curr == null)
                continue;

            // TODO: ADD CHECK FOR IF VALUE IS > 64 FROM BEFORE
            if (curr instanceof Block) {
                while (!stack.empty() && ((Block) stack.peek()).getValue() == ((Block) curr).getValue()) {
                    Block top = (Block) stack.pop();
                    positionShifts.put(top, Math.max(stack.size() - 1, 0));

                    int newValue = top.getValue() * 2;
                    BColor newColor;
                    if (currentPlayer == BColor.BLUE) { // if BLUE's turn
                        newColor = BColor.BLUE;
                        blueScore += newValue;
                    } else {
                        newColor = BColor.RED;
                        redScore += newValue;
                    }

                    Block newBlock = new Block(newValue, newColor);
                    curr = newBlock;
                }
            }

            stack.add(curr);
            positionShifts.put(blocks.get(i), stack.size() - 1);
        }

        ArrayList<MovableGridItem> result = new ArrayList<MovableGridItem>();
        // add stack elements to result array
        while (!stack.empty())
            result.add(stack.pop());

        Collections.reverse(result);

        // update destination coordinates for block animations
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == null)
                continue;

            MovableGridItem b = blocks.get(i);
            int target = -1;
            if (!isBackwards)
                target = positionShifts.get(b) + offsetIndex;
            else if (isBackwards) {
                target = offsetIndex - positionShifts.get(b) - 1;
            }

            // if the block is already in the right place
            if ((!isBackwards && target == i + offsetIndex) || (isBackwards && target == offsetIndex - i - 1)) {
                b.setTargetDestination(null);

                // only delete the object if it is going to be merged into something
                if (blocks.get(i) instanceof Block && result.get(i) instanceof Block
                        && ((Block) blocks.get(i)).getValue() != ((Block) result.get(i)).getValue()) {
                    mergingStillBlocks.add(b);
                }
            } else if (isVertical) {
                b.setTargetDestination(new Coordinate(target, lineIndex));
            } else if (!isVertical) {
                b.setTargetDestination(new Coordinate(lineIndex, target));
            }
        }

        return result;
    }

    /**
     * Gets the color of this player
     * 
     * @return The color of this player
     */
    public BColor getMyColor() {
        return myColor;
    }

    /**
     * Sets the color of this player
     * 
     * @param color The new color this player should be
     */
    public void setMyColor(BColor color) {
        this.myColor = color;
    }

    /**
     * Gets whose turn it is currently
     * 
     * @return the {@code BColor} of whoever's turn it is.
     */
    public BColor getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player
     * 
     * @param player the {@code BColor} of the new player
     */
    public void setCurrentPlayer(BColor player) {
        currentPlayer = player;
    }

    /**
     * Adds a block to the grid at the specified position with the current player's
     * color.
     *
     * @param row   The row index of the block.
     * @param col   The column index of the block.
     * @param value The value of the block.
     * @return {@code true} if the block was added successfully, {@code false}
     *         otherwise.
     */
    public boolean addBlock(int row, int col, int value) {
        return addBlock(row, col, value, currentPlayer);
    }

    /**
     * Adds a block to the grid at the specified position with the specified color.
     *
     * @param row   The row index of the block.
     * @param col   The column index of the block.
     * @param value The value of the block.
     * @param color The color of the block.
     * @return {@code true} if the block was added successfully, {@code false}
     *         otherwise.
     */
    public boolean addBlock(int row, int col, int value, BColor color) {
        if (null != getBlock(row, col)) {
            return false;
        }
        if ((row < 0) || (col < 0) || (row >= GRID_HEIGHT) || (col >= GRID_WIDTH)) {
            return false;
        }
        this.grid[row][col] = new Block(value, color);
        return true;
    }

    /**
     * Sets a block at the specified position in the grid.
     *
     * @param row   The row index of the block.
     * @param col   The column index of the block.
     * @param block The block to set.
     */
    public void setBlock(int row, int col, MovableGridItem block) {
        this.grid[row][col] = block;
    }

    /**
     * Gets the block at the specified position in the grid.
     *
     * @param row The row index of the block.
     * @param col The column index of the block.
     * @return The block at the specified position, or {@code null} if no block
     *         exists.
     */
    public MovableGridItem getBlock(int row, int col) {
        if ((row < 0) || (col < 0) || (row >= GRID_HEIGHT) || (col >= GRID_WIDTH)) {
            return null;
        }
        return this.grid[row][col];
    }

    /**
     * Checks if there is any empty space in the grid.
     *
     * @return {@code true} if there is at least one empty space, {@code false}
     *         otherwise.
     */
    public boolean hasEmptySpace() {
        for (int r = 0; r < this.grid.length; r++) {
            for (int c = 0; c < this.grid[r].length; c++) {
                if (this.grid[r][c] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets a list of all empty tiles in the grid.
     *
     * @return A list of coordinates representing empty tiles.
     */
    public ArrayList<Coordinate> getEmptyTiles() {
        ArrayList<Coordinate> out = new ArrayList<>(GRID_HEIGHT * GRID_WIDTH);
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
     * Merges blocks in the specified direction.
     *
     * @param dir The direction to merge blocks.
     */
    public void merge(Direction dir) {
        if (dir == Direction.UP) {
            shiftBlocksUp();
        } else if (dir == Direction.DOWN) {
            shiftBlocksDown();
        } else if (dir == Direction.LEFT) {
            shiftBlocksLeft();
        } else if (dir == Direction.RIGHT) {
            shiftBlocksRight();
        }
    }

    /**
     * Gets the current game grid.
     *
     * @return A 2D array representing the game grid.
     */
    public MovableGridItem[][] getGrid() {
        return grid;
    }

    /**
     * Gets the score of the blue player.
     *
     * @return The blue player's score.
     */
    public int getBlueScore() {
        return blueScore;
    }

    /**
     * Gets the score of the red player.
     *
     * @return The red player's score.
     */
    public int getRedScore() {
        return redScore;
    }

    /**
     * Gets the top walls of the grid.
     *
     * @return A set of coordinates representing the top walls.
     */
    public Set<Coordinate> getTopWalls() {
        return topWalls;
    }

    /**
     * Gets the left walls of the grid.
     *
     * @return A set of coordinates representing the left walls.
     */
    public Set<Coordinate> getLeftWalls() {
        return leftWalls;
    }

    /**
     * Gets the blocks that are currently merging but not moving.
     *
     * @return A list of blocks that are merging but stationary.
     */
    public ArrayList<MovableGridItem> getMergingStillBlocks() {
        return mergingStillBlocks;
    }

    /**
     * Gets the blocks that are currently moving.
     *
     * @return A set of blocks that are moving.
     */
    public Set<MovableGridItem> getCurrentlyMovingBlocks() {
        return currentlyMovingBlocks;
    }

    /**
     * Checks if it is the current player's turn.
     *
     * @return {@code true} if it is the current player's turn, {@code false}
     *         otherwise.
     */
    public boolean isTurn() {
        return myColor == currentPlayer;
    }

}

// // Get list of arrays
// public ArrayList<Block> check(ArrayList<Block> blocks, Direction dir) {
// Stack<Block> stack = new Stack<Block>();

// for (Block b : blocks) {
// Block currentBlock = b;
// if (currentBlock == null)
// continue;

// // two blocks have same value

// if (!stack.empty()) {
// boolean sameValue = stack.peek().getValue() == currentBlock.getValue();

// // if they have the same value, get the value
// // if not, ensure that lessThan64 is false
// int value;
// if (sameValue) {
// value = currentBlock.getValue();
// } else {
// value = 64;
// }

// // if the identical value is 64. If not identical, false
// boolean lessThan64 = value < 64;

// // if the blocks are the same color
// boolean sameColor = stack.peek().getColor() == currentBlock.getColor();

// /*
// * only combine if:
// * blocks have same value
// * AND
// * block value is less than 64 (steal block from other team if different
// color)
// * OR
// * the blocks have the same value (combine even if value greater than 64)
// *
// * do NOT combine if the blocks are different colors and value >= 64
// */
// while (!stack.empty() && sameValue && (lessThan64 || sameColor)) {
// Block top = stack.pop();
// Block newBlock = new Block(top.getValue() * 2, currentPlayer);
// currentBlock = newBlock;

// if (!stack.empty()) {
// sameValue = stack.peek().getValue() == currentBlock.getValue();
// sameColor = stack.peek().getColor() == currentBlock.getColor();
// }

// if (sameValue) {
// value = currentBlock.getValue();
// } else {
// value = 64;
// }
// lessThan64 = value < 64;

// // FIX SCORE

// if (currentPlayer == BColor.BLUE) {
// blueScore += value;
// } else {
// redScore += value;
// }

// // if (game.isTurn()) { // if BLUE's turn
// // newColor = BColor.BLUE;
// // BLUE_SCORE += newValue;
// // } else {
// // newColor = BColor.RED;
// // RED_SCORE += newValue;
// // }
// }

// }

// stack.add(currentBlock);
// }

// ArrayList<Block> result = new ArrayList<Block>();
// // add stack elements to result array
// while (!stack.empty())
// result.add(stack.pop());

// Collections.reverse(result);

// return result;
// }
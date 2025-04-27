import java.util.*;

/**
 * The data for the game
 */
public class Game {
    private int GRID_WIDTH = GameWorld.GRID_WIDTH, GRID_HEIGHT = GameWorld.GRID_HEIGHT;
    private int blueScore = 0, redScore = 0;
    private MovableGridItem[][] grid;
    private BColor currentPlayer;
    private BColor myColor;

    private ArrayList<MovableGridItem> mergingStillBlocks = new ArrayList<MovableGridItem>();
    private Set<MovableGridItem> currentlyMovingBlocks = new HashSet<MovableGridItem>();

    /**
     * The walls of the grid. The location of the walls is stored as a coordinate
     * the block it is relative to.
     */
    private Set<Coordinate> topWalls, leftWalls;

    public Game() {
        this.grid = new MovableGridItem[GRID_HEIGHT][GRID_WIDTH];
        this.currentPlayer = BColor.BLUE;
        topWalls = new HashSet<Coordinate>();
        leftWalls = new HashSet<Coordinate>();
        addWalls();
    }

    /**
     * Swaps who can make a move.
     * 
     * @return the {@code BColor} of the NEW active player
     */
    public BColor swapActivePlayer() {
        if (this.currentPlayer == BColor.BLUE) {
            this.currentPlayer = BColor.RED;
        } else if (this.currentPlayer == BColor.RED) {
            this.currentPlayer = BColor.BLUE;
        }

        // System.out.println("NEW PLAYER: " + currentPlayer.toString());
        return this.currentPlayer;
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
    }

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
    }

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

                    updateBlockVelocities(row, false);

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

    private void updateBlockVelocities(ArrayList<MovableGridItem> line, boolean isVertical) {
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

    public BColor getMyColor() {
        return myColor;
    }

    public void setMyColor(BColor color) {
        this.myColor = color;
    }

    public BColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(BColor player) {
        currentPlayer = player;
    }

    public boolean addBlock(int row, int col, int value) {
        return addBlock(row, col, value, currentPlayer);
    }

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

    public void setBlock(int row, int col, MovableGridItem block) {
        this.grid[row][col] = block;
    }

    public MovableGridItem getBlock(int row, int col) {
        if ((row < 0) || (col < 0) || (row >= GRID_HEIGHT) || (GRID_WIDTH >= 3)) {
            return null;
        }
        return this.grid[row][col];
    }

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

    public MovableGridItem[][] getGrid() {
        return grid;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getRedScore() {
        return redScore;
    }

    public Set<Coordinate> getTopWalls() {
        return topWalls;
    }

    public Set<Coordinate> getLeftWalls() {
        return leftWalls;
    }

    public ArrayList<MovableGridItem> getMergingStillBlocks() {
        return mergingStillBlocks;
    }

    public Set<MovableGridItem> getCurrentlyMovingBlocks() {
        return currentlyMovingBlocks;
    }

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
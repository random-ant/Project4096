import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.Stack;

public class Game {
    int GRID_WIDTH = 10;
    int GRID_HEIGHT = 10;
    int blueScore = 0;
    int redScore = 0;
    Block[][] grid;
    BColor currentPlayer;
    BColor myColor;
    GameClient client;

    /**
     * The walls of the grid. The location of the walls is stored as a coordinate of
     * the block it is relative to.
     */
    private Set<Coordinate> topWalls, leftWalls;

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

    public Game() {
        this.grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        this.currentPlayer = BColor.BLUE;
        topWalls = new HashSet<Coordinate>();
        leftWalls = new HashSet<Coordinate>();
        addWalls();
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

    public Game copy() {
        Game copy = new Game();
        for (int r = 0; r < this.grid.length; r++) {
            for (int c = 0; c < this.grid[r].length; c++) {
                copy.setBlock(r, c, this.grid[r][c]);
            }
        }
        copy.setCurrentPlayer(this.currentPlayer);
        return copy;
    }

    public BColor getMyColor() {
        return myColor;
    }

    public void setMyColor(BColor color) {
        myColor = color;
    }

    public BColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(BColor player) {
        currentPlayer = player;
    }

    public void nextPlayer() {
        if (this.currentPlayer == BColor.BLUE) {
            this.currentPlayer = BColor.RED;
        } else if (this.currentPlayer == BColor.RED) {
            this.currentPlayer = BColor.BLUE;
        }
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

    public void setBlock(int row, int col, Block block) {
        this.grid[row][col] = block;
    }

    public Block getBlock(int row, int col) {
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

    /**
     * Merges blocks in the given array. The blocks are merged from left to right.
     * Elements will always be merged to the largest possible element.
     * 
     * @param dir The direction in which to merge.
     * @return void
     */
    public void merge(Direction dir) {
        int BLOCKS_SPAWNED_PER_MOVE = 10;
        // listen for key presses and act accordingly
        if (dir == Direction.UP) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int i = 0; i <= GRID_HEIGHT; i++) {
                    // when there's a wall, merge what we already have
                    if (topWalls.contains(new Coordinate(i, j)) || i == GRID_HEIGHT) {
                        ArrayList<Block> toAdd = check(col, dir);
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

                // for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                // spawnRandomBlock();
            }

        } else if (dir == Direction.DOWN) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
                    if (grid[i][j] != null) {
                        col.add(grid[i][j]);
                    }

                    if (topWalls.contains(new Coordinate(i, j)) || i == 0) {
                        ArrayList<Block> toAdd = check(col, dir);
                        result.addAll(toAdd);
                        // add empty blocks
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
            // for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
            // spawnRandomBlock();

        } else if (dir == Direction.LEFT) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int j = 0; j <= GRID_WIDTH; j++) {
                    if (leftWalls.contains(new Coordinate(i, j)) || j == GRID_WIDTH) {
                        ArrayList<Block> toAdd = check(row, dir);
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
            // for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
            // spawnRandomBlock();

        } else if (dir == Direction.RIGHT) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                ArrayList<Block> result = new ArrayList<Block>();

                for (int j = GRID_WIDTH - 1; j >= 0; j--) {
                    if (grid[i][j] != null) {
                        row.add(grid[i][j]);
                    }

                    if (leftWalls.contains(new Coordinate(i, j)) || j == 0) {
                        ArrayList<Block> toAdd = check(row, dir);
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
            // for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
            // spawnRandomBlock();
        }

    }

    // Get list of arrays
    public ArrayList<Block> check(ArrayList<Block> blocks, Direction dir) {
        Stack<Block> stack = new Stack<Block>();

        for (Block b : blocks) {
            Block currentBlock = b;
            if (currentBlock == null)
                continue;

            // two blocks have same value

            if (!stack.empty()) {
                boolean sameValue = stack.peek().getValue() == currentBlock.getValue();

                // if they have the same value, get the value
                // if not, ensure that lessThan64 is false
                int value;
                if (sameValue) {
                    value = currentBlock.getValue();
                } else {
                    value = 64;
                }

                // if the identical value is 64. If not identical, false
                boolean lessThan64 = value < 64;

                // if the blocks are the same color
                boolean sameColor = stack.peek().getColor() == currentBlock.getColor();

                /*
                 * only combine if:
                 * blocks have same value
                 * AND
                 * block value is less than 64 (steal block from other team if different color)
                 * OR
                 * the blocks have the same value (combine even if value greater than 64)
                 * 
                 * do not combine if the blocks are different colors and value >= 64
                 */
                while (!stack.empty() && sameValue && (lessThan64 || sameColor)) {
                    Block top = stack.pop();
                    Block newBlock = new Block(top.getValue() * 2, currentPlayer);
                    currentBlock = newBlock;

                    if (!stack.empty()) {
                        sameValue = stack.peek().getValue() == currentBlock.getValue();
                        sameColor = stack.peek().getColor() == currentBlock.getColor();
                    }

                    if (sameValue) {
                        value = currentBlock.getValue();
                    } else {
                        value = 64;
                    }
                    lessThan64 = value < 64;

                    // FIX SCORE

                    if (currentPlayer == BColor.BLUE) {
                        blueScore += value;
                    } else {
                        redScore += value;
                    }

                    // if (game.isTurn()) { // if BLUE's turn
                    // newColor = BColor.BLUE;
                    // BLUE_SCORE += newValue;
                    // } else {
                    // newColor = BColor.RED;
                    // RED_SCORE += newValue;
                    // }
                }

            }

            stack.add(currentBlock);
        }

        ArrayList<Block> result = new ArrayList<Block>();
        // add stack elements to result array
        while (!stack.empty())
            result.add(stack.pop());

        return result;
    }

    public Coordinate getRandomEmptyTile() {
        ArrayList<Coordinate> empty = getEmptyTiles();
        int numEmpty = empty.size();
        int randomIndex = (int) (Math.random() * numEmpty);
        return empty.get(randomIndex);
    }

    public int[] spawnRandomBlock() {
        double spawnValue = Math.random();
        int value;
        if (spawnValue <= 0.7) {
            value = 2;
        } else if (spawnValue <= 0.9) {
            value = 4;
        } else {
            value = 8;
        }
        // Block block = new Block(value, BColor.BLUE, getRandomEmptyTile());
        Coordinate g = getRandomEmptyTile();
        int row = g.getRow();
        int col = g.getCol();
        grid[row][col] = new Block(value, BColor.NEUTRAL);

        int[] ret = { row, col, value };
        return ret;
    }

    public Block[][] getGrid() {
        return grid;
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    public boolean isTurn() {
        return myColor == currentPlayer;
    }

}

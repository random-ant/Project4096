import java.util.ArrayList;
import java.util.*;

import mayflower.*;

public class Game {
    int GRID_WIDTH = 10;
    int GRID_HEIGHT = 10;
    Block[][] grid;
    BColor currentPlayer;
    GameClient client;

    public Game() {
        this.grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        this.currentPlayer = BColor.BLUE;

        for (int i = 0; i < 10; i++)
            spawnRandomBlock();
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
        this.grid[row][col] = new Block(value, color, new Coordinate(row, col));
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

    public void merge(Direction dir) {
        int BLOCKS_SPAWNED_PER_MOVE = 10;
        // listen for key presses and act accordingly
        if (dir == Direction.UP) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    col.add(grid[i][j]);
                }

                ArrayList<Block> result = check(col, dir);
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

        } else if (dir == Direction.DOWN) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
                    col.add(grid[i][j]);
                }

                ArrayList<Block> result = check(col, dir);
                // add empty blocks
                result.addAll(0, Arrays.asList(new Block[GRID_HEIGHT - result.size()]));

                // put in grid
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    grid[i][j] = result.get(i);
                }
            }
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();

        } else if (dir == Direction.LEFT) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                for (int j = 0; j < GRID_WIDTH; j++) {
                    row.add(grid[i][j]);
                }

                ArrayList<Block> result = check(row, dir);
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

        } else if (dir == Direction.RIGHT) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                for (int j = GRID_WIDTH - 1; j >= 0; j--) {
                    row.add(grid[i][j]);
                }

                ArrayList<Block> result = check(row, dir);
                // add empty blocks
                result.addAll(0, Arrays.asList(new Block[GRID_HEIGHT - result.size()]));

                // put in grid
                for (int j = 0; j < GRID_HEIGHT; j++) {
                    grid[i][j] = result.get(j);
                }
            }
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();

        }

    }

    // Get list of arrays
    public ArrayList<Block> check(ArrayList<Block> blocks, Direction dir) {
        Stack<Block> stack = new Stack<Block>();

        for (Block b : blocks) {
            Block currentBlock = b;
            if (currentBlock == null)
                continue;

            currentBlock.setColor(BColor.RED);
            int merges = 0;
            while (!stack.empty() && stack.peek().getValue() == currentBlock.getValue()) {
                Block top = stack.pop();
                Block newBlock = new Block(top.getValue() * 2, BColor.RED, new Coordinate());
                currentBlock = newBlock;
                merges++;
            }
            if (merges > 0) {
                client.send("move " + dir);
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

    public void spawnRandomBlock() {
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
        grid[row][col] = new Block(value, BColor.BLUE, new Coordinate());
        client.send("addblock " + row + " " + col + " " + value);
    }

    public Block[][] getGrid() {
        return grid;
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

}

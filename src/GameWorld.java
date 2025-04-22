import mayflower.*;
import java.util.*;

public class GameWorld extends World {
    private Game game;
    private Block[][] grid;
    private GameClient client;

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

    public GameWorld(GameClient client, Game game) {
        grid = new Block[GRID_HEIGHT][GRID_WIDTH];
        this.client = client;
        this.game = game;
        topWalls = game.getTopWalls();
        leftWalls = game.getLeftWalls();

        // addObject(new Title(), 20, 20);
        addObject(new GridBorder(), 40, 245);
        addObject(new Title(), 301, 55);

        if (game.getMyColor() == BColor.BLUE) {
            turnGraph = new TurnGraphic(BColor.BLUE);
        } else {
            turnGraph = new TurnGraphic(BColor.NEUTRAL);
        }

        addObject(turnGraph, 40, 55);

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_HEIGHT) + OFFSET_Y;

                // add blocks
                addObject(new Tile(), x_coord, y_coord);
                // addObject(currBlock, 200, 200);
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

        renderGrid();
    }

    public TurnGraphic getTurnGraph() {
        return turnGraph;
    }

    public void setTurnGraph(TurnGraphic turnGraph) {
        this.turnGraph = turnGraph;
    }

    /**
     * Renders the grid. Inclues all tiles, blocks, walls in the grid, plus the
     * score text. Should be called whenever game state is changed.
     */
    public void renderGrid() {

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;

                Block currBlock = game.getGrid()[i][j];
                // add blocks
                addObject(currBlock, x_coord + (TILE_WIDTH - BLOCK_WIDTH) / 2, y_coord +
                        (TILE_HEIGHT - BLOCK_HEIGHT) / 2);
                // addObject(currBlock, 200, 200);
            }
        }

        // update score text
        showText("score blue: " + BLUE_SCORE, 550, 55, Color.BLACK);
        showText("score red: " + RED_SCORE, 550, 110, Color.BLACK);
    }

    @Override
    public void act() {
        if (game.isTurn()) {
            if (keyPressed(Keyboard.KEY_UP) || keyPressed(Keyboard.KEY_DOWN) || keyPressed(Keyboard.KEY_LEFT)
                    || keyPressed(Keyboard.KEY_RIGHT)) {
                if (keyPressed(Keyboard.KEY_UP)) {
                    game.merge(Direction.UP);
                    client.send("move " + Direction.UP);
                } else if (keyPressed(Keyboard.KEY_DOWN)) {
                    game.merge(Direction.DOWN);
                    client.send("move " + Direction.DOWN);
                } else if (keyPressed(Keyboard.KEY_LEFT)) {
                    game.merge(Direction.LEFT);
                    client.send("move " + Direction.LEFT);
                } else if (keyPressed(Keyboard.KEY_RIGHT)) {
                    game.merge(Direction.RIGHT);
                    client.send("move " + Direction.RIGHT);
                }
                game.nextPlayer();
                turnGraph.setTurn(BColor.NEUTRAL);
                int[] add = game.spawnRandomBlock();
                renderGrid();
                String message = "addblock " + add[0] + " " + add[1] + " " + add[2];
                client.send(message);
                client.send("render");
            }
        }
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
            grid[g.getRow()][g.getCol()] = new Block(value, BColor.NEUTRAL);
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
    private boolean keyPressed(int key) {
        return Mayflower.isKeyDown(key) && !Mayflower.wasKeyDown(key);
    }

    public void updateBlock(BColor color, int row, int col, int value) {
        grid[row][col].setColor(color);
        grid[row][col].setValue(value);
    }

}

// listen for key presses and act accordingly
// if(keyPresssed(Keyboard.KEY_UP)){
// for(int j = 0;j<GRID_WIDTH;j++)
// {
// ArrayList<Block> col = new ArrayList<Block>();
// ArrayList<Block> result = new ArrayList<Block>();

// for (int i = 0; i <= GRID_HEIGHT; i++) {
// // when there's a wall, merge what we already have
// if (topWalls.contains(new Coordinate(i, j)) || i == GRID_HEIGHT) {
// ArrayList<Block> toAdd = merge(col);
// result.addAll(toAdd);

// // add empty blocks
// result.addAll(Arrays.asList(new Block[i - result.size()]));

// col.clear();

// }

// if (i < GRID_HEIGHT && grid[i][j] != null) {
// col.add(grid[i][j]);
// }
// }

// // put in grid
// for (int i = 0; i < GRID_HEIGHT; i++) {
// grid[i][j] = result.get(i);
// }
// Block add = game.spawnRandomBlock();
// renderGrid();
// String message = "addblock " + add.getCoord().getRow() + " " +
// add.getCoord().getCol() + " " + add.getValue();
// client.send(message);
// client.send("render");
// game.nextPlayer();
// }
// }

// }else if(keyPresssed(Keyboard.KEY_DOWN)){for(

// int j = 0;j<GRID_WIDTH;j++)
// {
// ArrayList<Block> col = new ArrayList<Block>();
// ArrayList<Block> result = new ArrayList<Block>();

// for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
// if (grid[i][j] != null) {
// col.add(grid[i][j]);
// }

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

// }else if(keyPresssed(Keyboard.KEY_LEFT)) {
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

// if (j < GRID_WIDTH && grid[i][j] != null) {
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
// if (grid[i][j] != null) {
// row.add(grid[i][j]);
// }

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
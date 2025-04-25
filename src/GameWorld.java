import mayflower.*;
import java.util.*;

/**
 * The drawing for the game
 */
public class GameWorld extends World {
    private Game game;
    private GameClient client;

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

    private TurnGraphic turnGraphic;

    /**
     * The number of randomly spawned blocks spawned every move.
     */
    private int BLOCKS_SPAWNED_PER_MOVE = 3;

    public GameWorld(GameClient client, Game game) {
        this.client = client;
        this.game = game;

        addObject(new GridBorder(), 40, 245);
        addObject(new Title(), 301, 55);

        if (game.getMyColor() == BColor.BLUE)
            turnGraphic = new TurnGraphic(BColor.BLUE);
        else
            turnGraphic = new TurnGraphic(BColor.NEUTRAL);

        addObject(turnGraphic, 40, 55);

        game.addBlock(1, 0, 2);
        game.addBlock(2, 0, 2);

        spawnRandomBlocks(10);
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
        Set<Coordinate> topWalls = game.getTopWalls();
        Set<Coordinate> leftWalls = game.getLeftWalls();
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
                Coordinate converted = convertToPixels(new Coordinate(i, j));
                int x_coord = converted.getX(), y_coord = converted.getY();

                // add tiles
                addObject(new Tile(), x_coord, y_coord);
            }
        }

        // spawn top walls
        for (Coordinate c : game.getTopWalls()) {
            Coordinate converted = convertToPixels(c);
            int x_coord = converted.getX(), y_coord = converted.getY();
            addObject(new HorizontalWall(), x_coord - BLOCK_BORDER_WIDTH, y_coord - BLOCK_BORDER_HEIGHT);
        }

        // spawn left walls
        for (Coordinate c : game.getLeftWalls()) {
            Coordinate converted = convertToPixels(c);
            int x_coord = converted.getX(), y_coord = converted.getY();
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
                Coordinate converted = convertToPixels(new Coordinate(i, j));
                int x_coord = converted.getX(), y_coord = converted.getY();

                // add blocks
                Block currBlock = game.getGrid()[i][j];
                if (currBlock != null) {
                    addObject(currBlock, x_coord + BLOCK_BORDER_WIDTH, y_coord + BLOCK_BORDER_HEIGHT);
                }
            }
        }

        // update score text
        showText("score blue: " + game.getBlueScore(), 550, 55, Color.BLACK);
        showText("score red: " + game.getRedScore(), 550, 110, Color.BLACK);
    }

    /**
     * Converts a coordinate in the grid to a pixel coordinate on the screen.
     * 
     * @param c the {@code Coordinate} representation of the position in the grid to
     *          convert.
     * @return an array of pixel coordinates. The first element is the X coordinate,
     *         and the second element is the Y coordinate.
     */
    public static Coordinate convertToPixels(Coordinate c) {
        int col_coord = (c.getCol() * TILE_WIDTH) + OFFSET_X;
        int row_coord = (c.getRow() * TILE_HEIGHT) + OFFSET_Y;
        return new Coordinate(row_coord, col_coord);
    }

    /**
     * returns x and y
     * 
     * @param c
     * @return
     */
    public static Coordinate calculateBlockPixel(Coordinate c) {
        int col_coord = (c.getCol() * TILE_WIDTH) + OFFSET_X + BLOCK_BORDER_WIDTH;
        int row_coord = (c.getRow() * TILE_HEIGHT) + OFFSET_Y + BLOCK_BORDER_HEIGHT;
        return new Coordinate(row_coord, col_coord);
    }

    public void removeMovingBlock(Block b) {
        Set<Block> currentlyMovingBlocks = game.getCurrentlyMovingBlocks();
        ArrayList<Block> mergingStillBlocks = game.getMergingStillBlocks();
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

            // printGrid();
            renderGrid();

            // game.nextPlayer();
            // turnGraphic.setTurn(BColor.NEUTRAL);
            // spawnRandomBlocksAndSend(2);
        }
    }

    @Override
    public void act() {
        // if not player's turn, don't allow anything to happen
        if (!game.isTurn())
            return;

        // if in the middle of an animation, disregard key presses\
        // TODO: might slow down game
        // if (!game.getCurrentlyMovingBlocks().isEmpty())
        // return;

        // listen for key presses and act accordingly
        if (keyPressed(Keyboard.KEY_UP)) {
            game.merge(Direction.UP);
            client.send("move " + Direction.UP);
            game.swapActivePlayer();
            if (game.isTurn()) {
                turnGraphic.setTurn(game.getMyColor());
            } else {
                turnGraphic.setTurn(BColor.NEUTRAL);
            }
        } else if (keyPressed(Keyboard.KEY_DOWN)) {
            game.merge(Direction.DOWN);
            client.send("move " + Direction.DOWN);
            game.swapActivePlayer();
            if (game.isTurn()) {
                turnGraphic.setTurn(game.getMyColor());
            } else {
                turnGraphic.setTurn(BColor.NEUTRAL);
            }
        } else if (keyPressed(Keyboard.KEY_LEFT)) {
            game.merge(Direction.LEFT);
            client.send("move " + Direction.LEFT);
            game.swapActivePlayer();
            if (game.isTurn()) {
                turnGraphic.setTurn(game.getMyColor());
            } else {
                turnGraphic.setTurn(BColor.NEUTRAL);
            }
        } else if (keyPressed(Keyboard.KEY_RIGHT)) {
            game.merge(Direction.RIGHT);
            client.send("move " + Direction.RIGHT);
            game.swapActivePlayer();
            if (game.isTurn()) {
                turnGraphic.setTurn(game.getMyColor());
            } else {
                turnGraphic.setTurn(BColor.NEUTRAL);
            }
        }
    }

    private ArrayList<Coordinate> getEmptyTiles() {
        ArrayList<Coordinate> out = new ArrayList<Coordinate>(GRID_HEIGHT * GRID_WIDTH);
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (game.getGrid()[i][j] == null) {
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
        Collections.shuffle(empty, new Random(324392837));
        for (int i = 0; i < numBlocks && i < empty.size(); i++) {
            Coordinate g = empty.get(i);
            game.getGrid()[g.getRow()][g.getCol()] = new Block(value, BColor.BLUE);
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

    public TurnGraphic getTurnGraphic() {
        return turnGraphic;
    }

}

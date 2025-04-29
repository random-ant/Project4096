package game;

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

    /**
     * Blocks that need to spawn after animations are finished. Represented with a
     * Coordinate, then the blocks value. Used for randomly spawned blocks.
     */
    private Map<Coordinate, MovableGridItem> queuedBlocksToSpawn;

    /**
     * Constructs a GameWorld instance with the specified client and game.
     *
     * @param client The client associated with the game world.
     * @param game   The game logic associated with the game world.
     */
    public GameWorld(GameClient client, Game game) {
        this.client = client;
        this.game = game;
        queuedBlocksToSpawn = new HashMap<Coordinate, MovableGridItem>();

        addObject(new GridBorder(), 40, 245);
        addObject(new Title(), 301, 55);

        if (game.getMyColor() == BColor.BLUE)
            turnGraphic = new TurnGraphic(BColor.BLUE);
        else
            turnGraphic = new TurnGraphic(BColor.NEUTRAL);

        addObject(turnGraphic, 40, 55);

        game.addBlock(1, 0, 2, BColor.NEUTRAL);
        game.addBlock(2, 0, 2, BColor.NEUTRAL);

        renderBaseGrid();
        renderGrid();
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
                MovableGridItem currBlock = game.getGrid()[i][j];
                if (currBlock instanceof Block) {
                    addObject(currBlock, x_coord + BLOCK_BORDER_WIDTH, y_coord + BLOCK_BORDER_HEIGHT);
                }
            }
        }

        // update score text
        showText("score blue: " + game.getBlueScore(), 500, 55, Color.BLACK);
        showText("score red: " + game.getRedScore(), 500, 110, Color.BLACK);
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
     * Calculates what pixel a given block should be placed at given a Coordinate on
     * the grid.
     * 
     * @param c The {@code Coordinate} (in tiles) of the wanted conversation
     * @return The converted {@code Coordinate} in terms of pixels
     */
    public static Coordinate calculateBlockPixel(Coordinate c) {
        int col_coord = (c.getCol() * TILE_WIDTH) + OFFSET_X + BLOCK_BORDER_WIDTH;
        int row_coord = (c.getRow() * TILE_HEIGHT) + OFFSET_Y + BLOCK_BORDER_HEIGHT;
        return new Coordinate(row_coord, col_coord);
    }

    /**
     * A method that processes whenever a block finishes its animation. Once all
     * blocks are done with their animation, the grid is rendered and queued blocks
     * are spawned.
     * 
     * @param b The {@code MovableGridItem} that has finished its animation
     */
    public void removeMovingBlock(MovableGridItem b) {
        Set<MovableGridItem> currentlyMovingBlocks = game.getCurrentlyMovingBlocks();
        ArrayList<MovableGridItem> mergingStillBlocks = game.getMergingStillBlocks();
        currentlyMovingBlocks.remove(b);
        removeObject(b);
        // System.out.println(currentlyMovingBlocks.size() + " left");

        // triggers once when all animations have been completed
        if (currentlyMovingBlocks.isEmpty()) {
            // remove all still blocks that need to be merged
            for (MovableGridItem still : mergingStillBlocks)
                removeObject(still);
            mergingStillBlocks.clear();

            spawnQueuedBlocks();
            renderGrid();
        }
    }

    /**
     * Called every gameframe.
     */
    @Override
    public void act() {
        // if not player's turn OR in the middle of an animation, don't allow anything
        // to happen
        if (!game.isTurn() || !game.getCurrentlyMovingBlocks().isEmpty())
            return;

        // listen for key presses and act accordingly
        if (keyPressed(Keyboard.KEY_UP)) {
            game.merge(Direction.UP);
            client.send("move " + Direction.UP);
            spawnAndSwap();
        } else if (keyPressed(Keyboard.KEY_DOWN)) {
            game.merge(Direction.DOWN);
            client.send("move " + Direction.DOWN);
            spawnAndSwap();
        } else if (keyPressed(Keyboard.KEY_LEFT)) {
            game.merge(Direction.LEFT);
            client.send("move " + Direction.LEFT);
            spawnAndSwap();
        } else if (keyPressed(Keyboard.KEY_RIGHT)) {
            game.merge(Direction.RIGHT);
            client.send("move " + Direction.RIGHT);
            spawnAndSwap();
        }

        // check if game is over
        if (game.isGameOver()) {
            BColor color = game.getMyColor();
            boolean win = game.getWinner() == color;
            Mayflower.setWorld(new EndWorld(win, color));
        }
    }

    /**
     * Puts the queued blocks in {@code queuedBlocksToSpawn} into the grid.
     */
    private void spawnQueuedBlocks() {
        for (Map.Entry<Coordinate, MovableGridItem> entry : queuedBlocksToSpawn.entrySet()) {
            Coordinate coord = entry.getKey();
            MovableGridItem item = entry.getValue();

            game.getGrid()[coord.getRow()][coord.getCol()] = item;
        }
        queuedBlocksToSpawn.clear();
    }

    /**
     * Helper method to spawn random blocks in, then swap whose
     * turn it is.
     */
    private void spawnAndSwap() {
        spawnRandomBlocks(BLOCKS_SPAWNED_PER_MOVE);

        game.swapActivePlayer();
        if (game.isTurn()) {
            turnGraphic.setTurn(game.getMyColor());
        } else {
            turnGraphic.setTurn(BColor.NEUTRAL);
        }
    }

    /**
     * Gets all empty tiles on the grid.
     * 
     * @return All empty tiles.
     */
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
     * happen. Sends the blocks created over the client to be placed.
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
            int row = g.getRow(), col = g.getCol();

            // queue the blocks up to be spawned
            addQueuedBlock(row, col, value);

            // send spawned blocks to other client
            String message = "addblock " + row + " " + col + " " + value;
            client.send(message);
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

    /**
     * Gets the object representation of the {@code TurnGraphic}
     * 
     * @return The object representation of the {@code TurnGraphic}
     */
    public TurnGraphic getTurnGraphic() {
        return turnGraphic;
    }

    /**
     * Adds a block to spawn into the queue.
     * 
     * @param row   the row of the block to spawn in
     * @param col   the column of the block to spawn in
     * @param value the value of the block to spawn in
     */
    public void addQueuedBlock(int row, int col, int value) {
        Block b = new Block(value, BColor.NEUTRAL);
        queuedBlocksToSpawn.put(new Coordinate(row, col), b);
    }
}

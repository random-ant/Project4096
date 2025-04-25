import mayflower.*;
import java.util.*;

public class GameWorld extends World {
    private Game game;
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

    private TurnGraphic turnGraphic;

    public GameWorld(GameClient client, Game game) {
        this.client = client;
        this.game = game;
        topWalls = game.getTopWalls();
        leftWalls = game.getLeftWalls();

        // addObject(new Title(), 20, 20);
        addObject(new GridBorder(), 40, 245);
        addObject(new Title(), 301, 55);

        if (game.getMyColor() == BColor.BLUE) {
            turnGraphic = new TurnGraphic(BColor.BLUE);
        } else {
            turnGraphic = new TurnGraphic(BColor.NEUTRAL);
        }

        addObject(turnGraphic, 40, 55);

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_HEIGHT) + OFFSET_Y;

                // add blocks
                addObject(new Tile(), x_coord, y_coord);
                // addObject(currBlock, 200, 200);
            }
        }

        renderGrid();
    }

    public TurnGraphic getTurnGraph() {
        return turnGraphic;
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
                if (currBlock == null) {
                    addObject(new Tile(), x_coord, y_coord);
                } else {
                    // add blocks
                    addObject(currBlock, x_coord + (TILE_WIDTH - BLOCK_WIDTH) / 2, y_coord +
                            (TILE_HEIGHT - BLOCK_HEIGHT) / 2);
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
        showText("score blue: " + game.getBlueScore(), 550, 55, Color.BLACK);
        showText("score red: " + game.getRedScore(), 550, 110, Color.BLACK);
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

                game.swapActivePlayer();
                turnGraphic.setTurn(BColor.NEUTRAL);
                
                spawnRandomBlocksAndSend(2);
            }
        }
    }

    public void spawnRandomBlocksAndSend(int numBlocks) {
        int[][] add = new int[numBlocks][3];
        for (int block = 0; block < numBlocks; block++) {
            add[block] = game.spawnRandomBlock();
            String message = "addblock " + add[block][0] + " " + add[block][1] + " " + add[block][2];
            client.send(message);
        }
        renderGrid();
        client.send("render");
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
}
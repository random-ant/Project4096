import mayflower.*;
import java.util.*;

public class GameWorld extends World {
    private Game game;
    private Block[][] blocks;
    private GameClient client;

    public static int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public static int OFFSET_X = 45, OFFSET_Y = 250;
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;

    public GameWorld(GameClient client, Game game) {
        blocks = new Block[GRID_HEIGHT][GRID_WIDTH];
        this.client = client;

        this.game = game;
        game.setClient(client);

        addObject(new Border(), 40, 245);
        addObject(new Title(), 301, 55);

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;
                addObject(new Tile(), x_coord, y_coord);
            }
        }

        renderGrid();

    }

    public void renderGrid() {

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;

                Block currBlock = game.getGrid()[i][j];
                if (currBlock != null) {
                    addObject(currBlock, x_coord + (TILE_WIDTH - BLOCK_WIDTH) / 2, y_coord +
                            (TILE_HEIGHT - BLOCK_HEIGHT) / 2);
                    // addObject(currBlock, 200, 200);
                }
            }
        }
    }

    @Override
    public void act() {
        showText(game.getMyColor().toString(), 100, 100);

        showText(game.getCurrentPlayer().toString(), 200, 200);
        if (game.isTurn()) {
            if (keyPressed(Keyboard.KEY_UP) || keyPressed(Keyboard.KEY_DOWN) || keyPressed(Keyboard.KEY_LEFT) || keyPressed(Keyboard.KEY_RIGHT)) {
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
                Block add = game.spawnRandomBlock();
                renderGrid();
                String message = "addblock " + add.getCoord().getRow() + " " + add.getCoord().getCol() + " " + add.getValue();
                client.send(message);
                client.send("render");
                game.nextPlayer();
            }
        }

    }

    private boolean keyPressed(int key) {
        return Mayflower.isKeyDown(key) && !Mayflower.wasKeyDown(key);
    }

    public void updateBlock(BColor color, int row, int col, int value) {
        blocks[row][col].setColor(color);
        blocks[row][col].setValue(value);
    }

}

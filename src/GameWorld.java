import mayflower.*;
import java.util.*;

public class GameWorld extends World {
    private Game game;
    private BColor color;
    private BColor currentColor;
    private Block[][] blocks;

    public static int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public static int OFFSET_X = 45, OFFSET_Y = 250;
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;

    public GameWorld(GameClient client, Game game) {
        this.color = game.getMyColor();
        this.currentColor = game.getCurrentPlayer();
        blocks = new Block[GRID_HEIGHT][GRID_WIDTH];

        this.game = game;
        game.setClient(client);

        renderGrid();

        showText(color.toString(), 100, 100);
    }

    public void renderGrid() {
        addObject(new Border(), 40, 245);
        addObject(new Title(), 301, 55);

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;
                addObject(new Tile(), x_coord, y_coord);

                Block currBlock = game.getGrid()[i][j];
                if (currBlock != null) {
                    System.out.println("idk");
                    addObject(currBlock, x_coord + (TILE_WIDTH - BLOCK_WIDTH) / 2, y_coord +
                            (TILE_HEIGHT - BLOCK_HEIGHT) / 2);
                    // addObject(currBlock, 200, 200);
                }
            }
        }
    }

    @Override
    public void act() {
        if (color == currentColor) {
            if (keyPressed(Keyboard.KEY_UP)) {
                game.merge(Direction.UP);
                renderGrid();
                System.out.println("HERE");
            } else if (keyPressed(Keyboard.KEY_DOWN)) {
                game.merge(Direction.DOWN);
                renderGrid();
            } else if (keyPressed(Keyboard.KEY_LEFT)) {
                game.merge(Direction.LEFT);
                renderGrid();
            } else if (keyPressed(Keyboard.KEY_RIGHT)) {
                game.merge(Direction.RIGHT);
                renderGrid();
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

    public BColor getCurrentColor() {
        return currentColor;
    }

    public void nextPlayer() {
        if (this.currentColor == BColor.BLUE) {
            this.currentColor = BColor.RED;
        } else if (this.currentColor == BColor.RED) {
            this.currentColor = BColor.BLUE;
        }
    }

}

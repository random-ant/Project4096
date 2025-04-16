import mayflower.*;
import java.util.*;

public class GameWorld extends World {
    private Block[][] grid;

    public static int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public static int OFFSET_X = 45, OFFSET_Y = 250;
    public static int TILE_WIDTH = 70, TILE_HEIGHT = 70;
    public static int BLOCK_WIDTH = 60, BLOCK_HEIGHT = 60;
    public static TurnGraphic turnGraph = new TurnGraphic(true);
    public static int RED_SCORE = 0;
    public static int BLUE_SCORE = 0;
    public static Title ex = new Title();

    public static boolean turn;

    public GameWorld() {
        grid = new Block[GRID_HEIGHT][GRID_WIDTH];

        
        addObject(ex, 20, 20);
        addObject(new Border(), 40, 245);
        addObject(new Title(), 301, 55);
        addObject(turnGraph, 40, 55);
        for (int i = 0; i < 10; i++)
            spawnRandomBlock();

        renderGrid();
        
        turn = true;
    }

    private void renderGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int x_coord = (j * TILE_WIDTH) + OFFSET_X;
                int y_coord = (i * TILE_WIDTH) + OFFSET_Y;
                addObject(new Tile(), x_coord, y_coord);

                Block currBlock = grid[i][j];
                if (currBlock != null) {
                    addObject(currBlock, x_coord + (TILE_WIDTH - BLOCK_WIDTH) / 2,
                            y_coord + (TILE_HEIGHT - BLOCK_HEIGHT) / 2);
                }
            }
        }
        showText("score blue: " + BLUE_SCORE, 550, 55, Color.BLACK);
        showText("score red: " + RED_SCORE, 550, 110, Color.BLACK);
    }

    @Override
    public void act() {
        int BLOCKS_SPAWNED_PER_MOVE = 3;
        

        if (!turn)
        {
            turnGraph.setImage("src/img/turnBoardRed.png");
        }
        if (turn)
        {
            turnGraph.setImage("src/img/turnBoardBlue.png");
        }

        // boolean validMove = false;
        // listen for key presses and act accordingly
        if (keyPresssed(Keyboard.KEY_UP)) {
            // boolean validMovee = false;
            ex.setLocation(ex.getX()+1, ex.getY());
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    col.add(grid[i][j]);
                }
                
                ArrayList<Block> result = check(col);
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
            renderGrid();
            turn = !turn;
            
            

        } else if (keyPresssed(Keyboard.KEY_DOWN)) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                ArrayList<Block> col = new ArrayList<Block>();
                for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
                    col.add(grid[i][j]);
                }

                ArrayList<Block> result = check(col);
                // add empty blocks
                result.addAll(0, Arrays.asList(new Block[GRID_HEIGHT - result.size()]));

                // put in grid
                for (int i = 0; i < GRID_HEIGHT; i++) {
                    grid[i][j] = result.get(i);
                }
            }
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();
            renderGrid();
            turn = !turn;

        } else if (keyPresssed(Keyboard.KEY_LEFT)) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                for (int j = 0; j < GRID_WIDTH; j++) {
                    row.add(grid[i][j]);
                }

                ArrayList<Block> result = check(row);
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
            renderGrid();
            turn = !turn;

        } else if (keyPresssed(Keyboard.KEY_RIGHT)) {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                ArrayList<Block> row = new ArrayList<Block>();
                for (int j = GRID_WIDTH - 1; j >= 0; j--) {
                    row.add(grid[i][j]);
                }

                ArrayList<Block> result = check(row);
                // add empty blocks
                result.addAll(0, Arrays.asList(new Block[GRID_HEIGHT - result.size()]));

                // put in grid
                for (int j = 0; j < GRID_HEIGHT; j++) {
                    grid[i][j] = result.get(j);
                }
            }
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();
            renderGrid();
            turn = !turn;

        }

        if (keyPresssed(Keyboard.KEY_SPACE)) {
            System.out.println("Space key pressed");
            for (int i = 0; i < BLOCKS_SPAWNED_PER_MOVE; i++)
                spawnRandomBlock();
                turn = !turn;
        }

    }

    // Get list of arrays
    public ArrayList<Block> check(ArrayList<Block> blocks) {
        Stack<Block> stack = new Stack<Block>();

        for (Block b : blocks) {
            Block currentBlock = b;
            if (currentBlock == null)
                continue;

            currentBlock.setColor(BColor.RED);
            while (!stack.empty() && stack.peek().getValue() == currentBlock.getValue()) {
                Block top = stack.pop();
                if (turn == true)
                {
                    Block newBlock = new Block(top.getValue() * 2, BColor.BLUE, new Coordinate());
                    currentBlock = newBlock;
                    BLUE_SCORE += top.getValue() * 2;
                }
                else if (turn == false)
                {
                    Block newBlock = new Block(top.getValue() * 2, BColor.RED, new Coordinate());
                    currentBlock = newBlock;
                    RED_SCORE += top.getValue() * 2;
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
        grid[g.getRow()][g.getCol()] = new Block(value, BColor.NEUTRAL, new Coordinate());
    }

    private boolean keyPresssed(int key) {
        return Mayflower.isKeyDown(key) && !Mayflower.wasKeyDown(key);
    }

}

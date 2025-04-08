import mayflower.*;
import java.util.ArrayList;

public class GameWorld extends World {
    private Tile[][] grid;
    public int GRID_HEIGHT = 10, GRID_WIDTH = 10;
    public int OFFSET_X = 45, OFFSET_Y = 250;

    public GameWorld() {
        grid = new Tile[GRID_HEIGHT][GRID_WIDTH];
        grid[1][1] = new Tile();
        // setBackground("grey");
        // addObject(new Tile(), 30, 30);

        renderGrid();

        System.out.println(getEmptyTilesInRow(0));
        System.out.println(getEmptyTilesInColumn(1));

    }

    private void renderGrid() {
        addObject(new Border(), 40, 245);
        addObject(new Title(), 301, 55);

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                addObject(new Tile(), (j * 70) + OFFSET_X, (i * 70) + OFFSET_Y);
            }
        }
    }

    @Override
    public void act() {
    }

    // returns arraylist of column indexes of grid in row input that are empty
    public ArrayList<Integer> getEmptyTilesInRow(int row) {
        ArrayList<Integer> out = new ArrayList<Integer>(GRID_WIDTH);
        for (int i = 0; i < GRID_WIDTH; i++) {
            if (grid[row][i] == null) {
                out.add(i);
            }
        }
        return out;
    }

    public ArrayList<Integer> getEmptyTilesInColumn(int column) {
        ArrayList<Integer> out = new ArrayList<Integer>(GRID_WIDTH);
        for (int i = 0; i < GRID_HEIGHT; i++) {
            if (grid[i][column] == null) {
                out.add(i);
            }
        }
        return out;
    }

    

    

}

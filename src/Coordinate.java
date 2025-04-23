/**
 * Represents a coordinate in the game grid with a row and column.
 */
public class Coordinate {
    private int row;
    private int col;

    /**
     * Constructs a Coordinate with the specified row and column.
     * 
     * @param row The row index.
     * @param col The column index.
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Retrieves the row index of the coordinate.
     * 
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row index of the coordinate.
     * 
     * @param row The new row index.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Retrieves the column index of the coordinate.
     * 
     * @return The column index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the column index of the coordinate.
     * 
     * @param col The new column index.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Returns a string representation of the coordinate.
     * 
     * @return A string in the format "(row, col)".
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
/**
 * Represents a coordinate in the game grid.
 */
public class Coordinate {
    private int row;
    private int col;

    /**
     * Constructs a coordinate with the specified row and column.
     *
     * @param row The row of the coordinate.
     * @param col The column of the coordinate.
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        if (row != other.row)
            return false;
        if (col != other.col)
            return false;
        return true;
    }

    /**
     * Gets the row of the coordinate.
     *
     * @return The row of the coordinate.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row of the coordinate.
     *
     * @param row The new row of the coordinate.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the column of the coordinate.
     *
     * @return The column of the coordinate.
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the column of the coordinate.
     *
     * @param col The new column of the coordinate.
     */
    public void setCol(int col) {
        this.col = col;
    }

    public int getX() {
        return getCol();
    }

    public int getY() {
        return getRow();
    }

    @Override
    public String toString() {
        return "(" + col + ", " + row + ")";
    }

}

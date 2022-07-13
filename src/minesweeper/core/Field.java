package minesweeper.core;

import java.util.Random;

/**
 * Field represents playing field and game logic.
 */
public class Field {

    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    private final int rowCount;

    private final int columnCount;

    private final int mineCount;

    private GameState state = GameState.PLAYING;

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        //generate the field content
        generate();

        for (int r = 0; r < this.rowCount; r++) {
            for (int i = 0; i < this.columnCount; i++) {
                if(tiles[r][i] instanceof Mine) {
                    System.out.print("X ");

                } else if(tiles[r][i] instanceof Clue) {
                    System.out.print(countAdjacentMines(r,i) + " ");

                } else {
                    System.out.print(tiles[r][i] + " ");
                }
            }
            System.out.println("\n");
        }
    }

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Mine count.
     */
    public int getMineCount() {
        return mineCount;
    }

    /**
     * Game state.
     */
    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Tile getTile(int row, int column) {
        Tile tile = tiles[row][column];
        return tile;
    }


    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Mine) {
                setState(GameState.FAILED);
                return;
            }

            if (isSolved()) {
                setState(GameState.SOLVED);
                return;
            }
        }
    }

    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
//        throw new UnsupportedOperationException("Method markTile not yet implemented");
        Tile tile = tiles[row][column];
        if(tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.MARKED);
        }
        if(tile.getState() == Tile.State.MARKED) {
            tile.setState(Tile.State.CLOSED);
        }
    }

    /**
     * Generates playing field.
     */
    private void generate() {
//        throw new UnsupportedOperationException("Method generate not yet implemented");
        Random r = new Random();

        int n1, n2, count = 0;

        while(count < mineCount) {
            n1 = r.nextInt(this.rowCount);
            n2 = r.nextInt(this.columnCount);
            if(tiles[n1][n2] == null) {
                tiles[n1][n2] = new Mine();
                count++;
            }
        }

        for (int i = 0; i < this.rowCount; i++) {
            for (int j = 0; j < this.columnCount; j++) {
                if(tiles[i][j] == null) {
                    tiles[i][j] = new Clue(countAdjacentMines(i,j));
                }
            }

        }
    }

    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    private boolean isSolved() {
        throw new UnsupportedOperationException("Method isSolved not yet implemented");
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < getRowCount()) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < getColumnCount()) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }
}

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

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
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

        for(Tile[] row : tiles) {
            for(Tile t : row) {
                System.out.print(t);
            }
            System.out.println();
        }

        System.out.println(getNumberOf(Tile.State.CLOSED));
        System.out.println(tiles.length * tiles[0].length);
    }

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
                state = GameState.FAILED;
                return;
            }

            if (isSolved()) {
                state = GameState.SOLVED;
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
        if(tiles[row][column].getState() == Tile.State.MARKED) {
            tiles[row][column].setState(Tile.State.CLOSED);
        } else {
            tiles[row][column].setState(Tile.State.MARKED);
        }
    }

    /**
     * Generates playing field.
     */
    private void generate() {
        Random rand = new Random();
        int counter = 0, r, c;

        while(counter < mineCount) {
            r = rand.nextInt(rowCount);
            c = rand.nextInt(columnCount);
            if(tiles[r][c] == null) {
                tiles[r][c] = new Mine();
                counter++;
            }
        }

        for (r = 0; r < rowCount; r++) {
            for (c = 0; c < columnCount; c++) {
                if(tiles[r][c] == null) {
                    tiles[r][c] = new Clue(countAdjacentMines(r, c));
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
        //počet všetkých dlaždíc - počet odokrytých dlaždíc = počet mín
        return (rowCount * columnCount - getNumberOf(Tile.State.OPEN)) == mineCount;
    }
    private int getNumberOf(Tile.State state) {
        int counter = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if(state == Tile.State.CLOSED) {
                    counter++;
                }
            }
        }
        return counter;
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
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
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

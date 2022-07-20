package test;



import minesweeper.core.Field;
import minesweeper.core.GameState;
import minesweeper.core.Mine;
import minesweeper.core.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Random randomGenerator = new Random();
    private Field field;
    private int rowCount;
    private int columnCount;
    private int minesCount;

    @BeforeEach
    public void initTest() {
        rowCount = randomGenerator.nextInt(10) + 5;
        columnCount = rowCount;
        minesCount = Math.max(1, randomGenerator.nextInt(rowCount * columnCount));
        field = new Field(rowCount, columnCount, minesCount);
    }

    @Test
    public void checkMinesCount() {
        int minesCounter = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (field.getTile(row, column) instanceof Mine) {
                    minesCounter++;
                }
            }
        }

        assertEquals(minesCount, minesCounter, "Field was initialized incorrectly - " +
                "a different amount of mines was counted in the field than amount given in the constructor.");
    }

    @Test
    public void checkFieldInitialization() {
        assertEquals(rowCount, field.getRowCount(),"Rows are not equal");
        assertEquals(columnCount, field.getColumnCount(),"Column are not equal");
        assertEquals(minesCount, field.getMineCount(),"Mines are not equal");
        assertEquals(GameState.PLAYING, field.getState(),"Game state is not equal");
    }

    @Test
    public void checkMarkTile() {
        int rowIndex = randomGenerator.nextInt(10);
        int columnIndex = randomGenerator.nextInt(10);

        assertEquals(field.getTile(rowIndex,columnIndex).getState(), Tile.State.OPEN);
        assertEquals(field.getTile(rowIndex,columnIndex).getState(), Tile.State.CLOSED);
        assertEquals(field.getTile(rowIndex,columnIndex).getState(), Tile.State.MARKED);
    }

    @Test
    public void fieldWithTooManyMines() {
        Field fieldWithTooManyMines = null;
        int higherMineCount = rowCount * columnCount + randomGenerator.nextInt(10) + 1;
        try {
            fieldWithTooManyMines = new Field(rowCount, columnCount, higherMineCount);
        } catch (Exception e) {
            // field with more mines than tiles should not be created - it may fail on exception
        }
        assertTrue((fieldWithTooManyMines == null) || (fieldWithTooManyMines.getMineCount() <= (rowCount * columnCount)));
    }

    // ... dalsie testy
}
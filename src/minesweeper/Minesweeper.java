package minesweeper;

import minesweeper.consoleui.ConsoleUI;
import minesweeper.core.Field;

/**
 * Main application class.
 */
public class Minesweeper {
    /** User interface. */
    private UserInterface userInterface;

    private long startMillis = System.currentTimeMillis();
 
    /**
     * Constructor.
     */
    private Minesweeper() {
        userInterface = new ConsoleUI();
        System.currentTimeMillis();
        System.out.println("Hello " + System.getProperty("user.name"));
        
        Field field = new Field(9, 9, 50);
        userInterface.newGameStarted(field);

    }

    public int getPlayingSeconds() {
        startMillis;
        return 0;
    }

    /**
     * Main method.
     * @param args arguments
     */
    public static void main(String[] args) {
        new Minesweeper();
    }
}

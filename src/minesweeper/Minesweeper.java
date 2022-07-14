package minesweeper;

import minesweeper.consoleui.ConsoleUI;
import minesweeper.core.Field;

/**
 * Main application class.
 */
public class Minesweeper {
    /** User interface. */
    private UserInterface userInterface;
 
    /**
     * Constructor.
     */
    private Minesweeper() {
        userInterface = new ConsoleUI();
//        System.out.println("Hello " + System.getProperty("user.name"));
        
        Field field = new Field(10, 20, 10);
        userInterface.newGameStarted(field);

    }

    /**
     * Main method.
     * @param args arguments
     */
    public static void main(String[] args) {
        new Minesweeper();
    }
}

package minesweeper.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import minesweeper.UserInterface;
import minesweeper.core.Field;
import minesweeper.core.GameState;

/**
 * Console user interface.
 */
public class ConsoleUI implements UserInterface {
    /**
     * Playing field.
     */
    private Field field;

    private String format = "%2s";

    /**
     * Input reader.
     */
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Reads line of text from the reader.
     *
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Starts the game.
     *
     * @param field field of mines and clues
     */
    @Override
    public void newGameStarted(Field field) {
        this.field = field;

        this.format = "%"
                + (1 + String.valueOf(field.getColumnCount()).length())
                + "s";
        do {
            update();
            processInput();
            if(field.getState() == GameState.FAILED) {
                System.out.println("Ukoncenie hry");
                System.exit(0);
            }
        } while (true);


    }

    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {
        System.out.println("Metoda update():");

        System.out.printf(format, "");
        for (int c = 0; c < field.getColumnCount(); c++) {
            System.out.printf(format, c);
        }
        System.out.println();

        //vypis riadky so zvislo osou na zaciatku
        for (int r = 0; r < field.getRowCount(); r++) {
            System.out.printf(format, (char)(r + 65));
            for (int c = 0; c < field.getColumnCount(); c++) {
                System.out.printf(format, field.getTile(r, c));
            }
            System.out.println();
        }
    }

    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
    private void processInput() {
        //Vypísať požiadavku na zadanie vstupu so vzorom očakávaného vstupu od používateľa: X – ukončenie hry, MA1 – označenie dlaždice v riadku A a stĺpci 1, OB4 – odkrytie dlaždice v riadku B a stĺpci 4.
        System.out.println("X - ukoncenie hry, MA1 - oznacenie dlazdice v riadku A a stlpci 1, OB4 - odkrytie dlazdice v riadku B a stlpci 4.");

        String line = readLine();

        if(line.equalsIgnoreCase("X")) {
            field.setState(GameState.FAILED);
        }

        Pattern pt = Pattern.compile("([M-O])([0-8])([0-8])");
        Matcher mt = pt.matcher(line);

        boolean result = mt.matches();

        if(result) {
            System.out.println("Zhoduje sa.");
            if(mt.group(1).equalsIgnoreCase("O")) {
                int row = Integer.parseInt(mt.group(2));
                int column = Integer.parseInt(mt.group(3));

//                field.getTile(row,column) = Tile.State.OPEN;
            }

            if(mt.group(1).equalsIgnoreCase("M")) {
                int row = Integer.parseInt(mt.group(2));
                int column = Integer.parseInt(mt.group(3));

//                field.getTile(row,column) = Tile.State.MARKED;
            }
        } else {
            System.out.println("Nespravne zadanie, zadaj este raz.");
        }
    }
}

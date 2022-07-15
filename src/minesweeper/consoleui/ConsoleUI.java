package minesweeper.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import minesweeper.UserInterface;
import minesweeper.core.Field;
import minesweeper.core.GameState;
import minesweeper.core.Tile;

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
                + (4 + String.valueOf(field.getColumnCount()).length())
                + "s";
        do {
            update();
            processInput();
        } while (true);



    }

    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {

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

        System.out.println("X - ukoncenie hry, MA1 - oznacenie dlazdice v riadku A a stlpci 1, OB4 - " +
                "odkrytie dlazdice v riadku B a stlpci 4.");
        String line = readLine().trim().toUpperCase(Locale.ROOT);

        try {
            handleInput(line);
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleInput(String input) throws WrongFormatException {

        if(input.equalsIgnoreCase("X")) {
            System.out.println("Ukoncenie hry");
            System.exit(0);
        }

        Pattern pt = Pattern.compile("([M-O])([A-I])([0-8])", Pattern.CASE_INSENSITIVE);
        Matcher mt = pt.matcher(input);
        boolean result = mt.matches();

        int row = mt.group(2).charAt(0) - 65;
        int column = Integer.parseInt(mt.group(3));

        if(result) {
            if(row <= field.getRowCount() && column <= field.getColumnCount()) {
                if(mt.group(1).equalsIgnoreCase("O")) {
                    field.openTile(row,column);
                }

                if(mt.group(1).equalsIgnoreCase("M")) {
                    field.markTile(row,column);
                }
            } else {
                throw new WrongFormatException("Nevychadzaj mimo pole.");
            }

        } else {
            System.out.println("Nespravne zadanie, zadaj este raz.");
        }
    }
}

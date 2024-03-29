package minesweeper.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.Comment;
import entity.Rating;
import entity.Score;
import minesweeper.Minesweeper;
import minesweeper.core.Field;
import minesweeper.core.GameState;
import minesweeper.core.Tile;
import service.*;

/**
 * Console user interface.
 */
public class ConsoleUI implements UserInterface {
    /**
     * Playing field. MA1 OB99
     */
    private Field field;
    Pattern OPEN_MARK_PATTERN = Pattern.compile("([OM]{1})([A-Z]{1})([0-9]{1,2})");

    /**
     * Input reader.
     */
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * name of the player
     */
    private String userName ="";

    private Settings setting;

    private static final String GAME = "minesweeper";

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

        int gameScore=0;

        this.field = field;
        System.out.println("Zadaj svoje meno:");
        userName = readLine();
        System.out.println("Vyber obtiaznost:");
        System.out.println("(1) BEGINNER, (2) INTERMEDIATE, (3) EXPERT, (ENTER) NECHAT DEFAULT");
        String level = readLine();
        if(level != null && !level.equals("")) {
            try {
                int intLevel = Integer.parseInt(level);
                Settings s = switch (intLevel) {
                    case 2 -> Settings.INTERMEDIATE;
                    case 3 -> Settings.EXPERT;
                    default -> Settings.BEGINNER;
                };
                this.setting = s;
                this.setting.save();
                this.field = new Field(s.getRowCount(), s.getColumnCount(), s.getMineCount());
            } catch (NumberFormatException e) {
                //empty naschval
            }
        }

        do {
            update();
            processInput();

            var fieldState=this.field.getState();

            if (fieldState == GameState.FAILED) {
                System.out.println(userName+", odkryl si minu. Prehral si. Tvoje skore je "+gameScore+".");
                break;
            }
            if (fieldState == GameState.SOLVED) {
                gameScore=this.field.getScore();
                System.out.println(userName+", vyhral si. Tvoje skore je "+gameScore+".");
                break;
            }
        } while (true);
        handlerComment();
        handlerRating();
        score();
        System.exit(0);

    }

    public void score() {
        int gameScore = 0;

        ScoreService scoreService = new ScoreServiceJDBC();
        var scores = scoreService.getBestScores(GAME);

        scoreService.addScore(new Score(GAME, userName, gameScore, new Date()));

        System.out.println("Top 5 hracov:");
        for (int i = 0; i < scores.size(); i++) {
            System.out.println((i + 1) + ". " +scores.get(i));
        }
    }

    private void handlerComment() {
        try {
            comment();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            handlerComment();
        }
    }

    private void handlerRating() {
        try {
            rating();

        } catch (NumberFormatException e) {
            System.out.println("Musi zadat len cislo, nie text.");
            handlerRating();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            handlerRating();
        }
    }

    public void comment() throws GameStudioException {
        CommentService commentService = new CommentServiceJDBC();

        var comments = commentService.getComments("minesweeper");

        System.out.println("Zanechaj komentar k tvojej hre: ");
        String userComment = readLine();
        if(userComment.length() > 1000 || userComment.length() == 0) {
            throw new GameStudioException("Komentar je prilis dlhy alebo prilis kratky.");
        }
        commentService.addComment(new Comment(GAME, userName, userComment, new Date()));

        System.out.println("Poslednych 5 komentarov:");
        for (int i = 0; i < comments.size(); i++) {
            System.out.println((i + 1) + ". " +comments.get(i));
        }
    }

    public void rating() throws GameStudioException {

        RatingService ratingService = new RatingServiceJDBC();
        System.out.println("Zanechaj hodnotenie k tvojej hre: ");

        String userRating = readLine();
        if(Integer.parseInt(userRating) < 1 || Integer.parseInt(userRating) > 5) {
            throw new GameStudioException("Nespravne hodnotenie. Zadaj hodnotenie este raz(1-5)");
        }

        ratingService.setRating(new Rating(GAME, userName, Integer.parseInt(userRating), new Date()));

        var ratings = ratingService.getRating(GAME, userName);

        System.out.println("Hru si hodnotil " +ratings+ "*");
    }

    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {
        //System.out.println("Metoda update():");
        System.out.printf("Cas hrania: %d%n",
                field.getPlayTimeInSeconds()
        );
        System.out.printf("Pocet poli neoznacenych ako mina je %s (pocet min: %s)%n", field.getRemainingMineCount(), field.getMineCount());

        //vypis horizontalnu os
        StringBuilder hornaOs = new StringBuilder("   ");
        for (int i = 0; i < field.getColumnCount(); i++) {
            hornaOs.append(String.format("%3s", i));
        }
        System.out.println(hornaOs);

        //vypis riadky so zvislo osou na zaciatku
        for (int r = 0; r < field.getRowCount(); r++) {
            System.out.printf("%3s", Character.toString(r + 65));
            for (int c = 0; c < field.getColumnCount(); c++) {
                System.out.printf("%3s", field.getTile(r, c));
            }
            System.out.println();
        }
    }

    @Override
    public void play() {
        setting = Settings.load();

        Field field = new Field(
                setting.getRowCount(),
                setting.getColumnCount(),
                setting.getMineCount()
        );
        newGameStarted(field);
    }

    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
    private void processInput() {
        System.out.println("Zadaj svoj vstup.");
        System.out.println("Ocakavany vstup:  X - ukoncenie hry, M - mark, O - open, U - unmark. Napr.: MA1 - oznacenie dlazdice v riadku A a stlpci 1");
        String playerInput = readLine();

        if(playerInput.trim().equals("X")) {
            System.out.println("Ukoncujem hru");
            System.exit(0);
        }

        // overi format vstupu - exception handling
        try {
            handleInput(playerInput);
        } catch (WrongFormatException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
            processInput();
        }
    }

    private void doOperation(char operation, char osYRow, int osXCol) {

        int osYRowInt = osYRow - 65;

        // M - oznacenie dlzadice
        if (operation == 'M') {
            field.markTile(osYRowInt, osXCol);

        }

        // O - Odkrytie dlazdice
        if (operation == 'O') {
            if (field.getTile(osYRowInt, osXCol).getState() == Tile.State.MARKED) {
                System.out.println("!!! Nie je mozne odkryt dlazdicu v stave MARKED");
                return;
            } else {
                field.openTile(osYRowInt, osXCol);
            }

        }

        System.out.println("Vykonal som pozadovanu operaciu");
    }

    private boolean isInputInBorderOfField(String suradnicaZvislaPismeno, String suradnicaHorizontalnaCislo) {
        boolean result = true;

        if ((int) suradnicaZvislaPismeno.charAt(0) >= (65 + field.getRowCount())) {
            result = false;
            System.out.print("!!! Pismeno prekracuje pocet riadkov.");
        }
        if (Integer.parseInt(suradnicaHorizontalnaCislo) >= field.getColumnCount()) {
            result = false;
            System.out.print(" !!! Cislo prekracuje pocet stlpcov.");

        }
        if (!result) {
            System.out.println(" Opakuj vstup.");
        }

        return result;
    }

    void handleInput(String playerInput) throws WrongFormatException {
        Matcher matcher1 = OPEN_MARK_PATTERN.matcher(playerInput);

        if (!OPEN_MARK_PATTERN.matcher(playerInput).matches()) {
            throw new WrongFormatException("!!! Zadal si nespravny format vstupu, opakuj vstup.");
        }

        matcher1.find();

        if (!isInputInBorderOfField(matcher1.group(2), matcher1.group(3))) {
            System.out.println("");
            processInput();
            return;
        }

        if(OPEN_MARK_PATTERN.matcher(playerInput).matches()) {
            doOperation(matcher1.group(1).charAt(0), matcher1.group(2).charAt(0), Integer.parseInt(matcher1.group(3)));
        }

    }

}

package test;

import entity.Comment;
import entity.Score;
import org.junit.jupiter.api.Test;
import service.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreServiceTest {

//    private ScoreService scoreService = new ScoreServiceJDBC();
    private ScoreService scoreService = new ScoreServiceFile();


    @Test
    public void testScoreReset() {
        scoreService.addScore(new Score("minesweeper", "Matej", 123, new Date()));
        scoreService.reset();
        assertEquals(0, scoreService.getBestScores("minesweeper").size());
    }

    @Test
    public void testAddScore() {
        scoreService.reset();
        var date = new Date();

        scoreService.addScore(new Score("minesweeper", "Matej", 123, date));

        var scores = scoreService.getBestScores("minesweeper");
        assertEquals(1, scores.size());

        assertEquals("minesweeper", scores.get(0).getGame());
        assertEquals("Matej", scores.get(0).getUserName());
        assertEquals(123, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());
    }


    @Test
    public void testGetBestScores() {
        scoreService.reset();
        var date = new Date();
        scoreService.addScore(new Score("minesweeper", "Peto", 140, date));
        scoreService.addScore(new Score("minesweeper", "Katka", 150, date));
        scoreService.addScore(new Score("tiles", "Zuzka", 290, date));
        scoreService.addScore(new Score("minesweeper", "Jergus", 100, date));

        var scores = scoreService.getBestScores("minesweeper");

        assertEquals(3, scores.size());

        assertEquals("minesweeper", scores.get(0).getGame());
        assertEquals("Katka", scores.get(0).getUserName());
        assertEquals(150, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());

        assertEquals("minesweeper", scores.get(1).getGame());
        assertEquals("Peto", scores.get(1).getUserName());
        assertEquals(140, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedOn());

        assertEquals("minesweeper", scores.get(2).getGame());
        assertEquals("Jergus", scores.get(2).getUserName());
        assertEquals(100, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedOn());
    }
}

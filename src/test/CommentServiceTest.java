package test;

import entity.Comment;
import entity.Score;
import org.junit.jupiter.api.Test;
import service.CommentService;
import service.CommentServiceJDBC;
import service.ScoreService;
import service.ScoreServiceFile;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceTest {

    private CommentService commentService = new CommentServiceJDBC();

    @Test
    public void testCommentReset() {
        commentService.addComment(new Comment("minesweeper", "Matej", "Ako sa mas", new Date()));
        commentService.reset();
        assertEquals(0, commentService.getComments("minesweeper").size());
    }


    @Test
    public void testAddComment() {
        commentService.reset();
        var date = new Date();

        commentService.addComment(new Comment("minesweeper", "Matej", "Ako sa mas", date));

        var comments = commentService.getComments("minesweeper");
        assertEquals(1, comments.size());

        assertEquals("minesweeper", comments.get(0).getGame());
        assertEquals("Matej", comments.get(0).getUserName());
        assertEquals("Ako sa mas", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

    @Test
    public void testGetComments() {
        commentService.reset();
        var date = new Date();
        commentService.addComment(new Comment("minesweeper", "Peto", "Moze byt", date));
        commentService.addComment(new Comment("minesweeper", "Fero", "Nemoze byt", date));
        commentService.addComment(new Comment("minesweeper", "Oliver", "Chod domov", date));
        commentService.addComment(new Comment("minesweeper", "David", "Podme von", date));

        var scores = commentService.getComments("minesweeper");

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








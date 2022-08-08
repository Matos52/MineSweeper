package test;

import entity.Rating;
import org.junit.jupiter.api.Test;
import service.RatingService;
import service.RatingServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {

    private RatingService ratingService = new RatingServiceJDBC();

    @Test
    public void getRating() {
        ratingService.reset();
        ratingService.setRating(new Rating("minesweeper", "Matej", 4, new Date()));
        assertEquals(4,ratingService.getRating("minesweeper", "Matej"));
    }

    @Test
    public void avgRatingTest() {
        ratingService.reset();
        var date = new Date();

        ratingService.setRating(new Rating("minesweeper", "Peto", 5, date));
        ratingService.setRating(new Rating("minesweeper", "Katka", 1, date));
        ratingService.setRating(new Rating("minesweeper", "Zuzka", 4, date));

        assertEquals((5+1+4)/3,ratingService.getAverageRating("minesweeper"));
    }

    @Test
    public void Duplicity() {
        ratingService.reset();
        var date = new Date();
        ratingService.setRating(new Rating("minesweeper", "Peto", 5, date));
        ratingService.setRating(new Rating("minesweeper", "Peto", 2, date));
        assertEquals(2,ratingService.getRating("minesweeper", "Peto"));
    }


}

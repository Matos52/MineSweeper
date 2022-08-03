package minesweeper;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import entity.Score;
import service.ScoreService;
import service.ScoreServiceJDBC;

public class TestJDBC {

    public static void main(String[] args) throws Exception {

        ScoreService service = new ScoreServiceJDBC();
//        service.reset();
        service.addScore(new Score("minesweeper", "David", 359, new Date()));
        service.addScore(new Score("minesweeper", "Matej", 136, new Date()));
        service.addScore(new Score("minesweeper", "Oliver", 259, new Date()));

        var scores = service.getBestScores("minesweeper");
        System.out.println(scores);
    }

//    public static void main(String[] args) throws Exception {
//
//        try(var connection = DriverManager.getConnection("jdbc:postgresql://localhost/gamestudio", "postgres", "postgres");
//            var statement = connection.createStatement();
//            var rs = statement.executeQuery("SELECT game, username, points, played_on FROM score WHERE game='minesweeper' ORDER BY points DESC LIMIT 5")
//                ) {
//
//            while(rs.next()) {
//                System.out.printf("%s, %s, %d, %s \n",rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4));
//            }
//
//            System.out.println("Pripojenie uspesne");
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}

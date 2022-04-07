package main;

import com.mysql.cj.jdbc.Driver;
import config.Config;
import data.Movie;

import java.sql.*;

public class MoviesJDBCTest {
    private static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        Config config = new Config();

        DriverManager.registerDriver(new Driver());
        Connection connection = DriverManager.getConnection(
                config.DB_HOST,
                config.DB_USER,
                config.DB_PW
        );

        PreparedStatement ps = connection.prepareStatement
                ("insert into movies (title, year, director, actors, rating, poster, genre, plot)values (?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, "The Other Guys");
        ps.setInt(2, 2010);
        ps.setString(3, "Adam McKay");
        ps.setString(4, "Mark Wahlberg, Will Ferral");
        ps.setDouble(5, 6.6);
        ps.setString(6, "https://m.media-amazon.com/images/I/71Ft68fi2QL._SL1500_.jpg");
        ps.setString(7, "comedy");
        ps.setString(8, "Unlike their heroic counterparts on the force, desk-bound NYPD detectives Gamble (Will Ferrell) and Hoitz (Mark Wahlberg) garner no headlines as they work day to day. Gamble relishes his job as a paper pusher, but Hoitz is itching to get back on the street and make a name for himself. When a seemingly minor case turns out to be a big deal, the two cops get the opportunity to finally prove to their comrades that they have the right stuff.\n");

        ps.executeUpdate();

        ResultSet newkeys = ps.getGeneratedKeys();
        newkeys.next();
        int newId = newkeys.getInt(1);
        System.out.println("New record id is " + newId);

        ps = connection.prepareStatement("SELECT * FROM movies");
        ResultSet movieRows = ps.executeQuery();

        while (movieRows.next()) {
            Movie movie = new Movie();
            movie.setId(movieRows.getInt("id"));
            movie.setTitle(movieRows.getString("title"));
            movie.setRating(movieRows.getInt("rating"));
            movie.setPoster(movieRows.getString("poster"));
            movie.setGenre(movieRows.getString("genre"));
            movie.setDirector(movieRows.getString("director"));
            movie.setPlot(movieRows.getString("plot"));
            movie.setActors(movieRows.getString("actors"));
            System.out.println(movie.toString());
        }




    }

//    public static void fetchMovies(Connection connection) throws SQLException {
//        PreparedStatement ps = connection.prepareStatement("SELECT * FROM ?");
//        ps.setString(1, "movies");
//        ps.executeUpdate();
//
//        ResultSet rs = ps
//
//    }
}

package dao;

import config.Config;
import data.Movie;
import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlMoviesDao implements MoviesDao{

    private Connection connection = null;

    public MySqlMoviesDao(Config config) {
        try {
            DriverManager.registerDriver(new Driver());

            this.connection = DriverManager.getConnection(
                    config.getUrl(), // <-- WHERE IS THE DB?
                    config.getUser(), // <-- WHO IS ACCESSING?
                    config.getPW() // <-- WHAT IS THEIR PASSWORD?
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database!", e);
        }
    }

    @Override
    public List<Movie> all() throws SQLException {
        List<Movie> movies = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("select * from movies");
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
            movies.add(movie);
        }

        ps.close();
        return movies;
    }

    @Override
    public Movie findOne(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("select * from movies where id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();

            ResultSet movieRows = ps.executeQuery();

            Movie movie = new Movie();
            movieRows.next();
            movie.setId(movieRows.getInt("id"));
            movie.setTitle(movieRows.getString("title"));
            movie.setRating(movieRows.getInt("rating"));
            movie.setPoster(movieRows.getString("poster"));
            movie.setGenre(movieRows.getString("genre"));
            movie.setDirector(movieRows.getString("director"));
            movie.setPlot(movieRows.getString("plot"));
            movie.setActors(movieRows.getString("actors"));

            return movie;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void insert(Movie movie) {

    }

    @Override
    public void insertAll(Movie[] movies) throws SQLException {

    }

    @Override
    public void update(Movie movie) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }
}

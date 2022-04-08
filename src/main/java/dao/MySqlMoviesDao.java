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

    public MySqlMoviesDao() {
        try {
            DriverManager.registerDriver(new Driver());

            this.connection = DriverManager.getConnection(
                    Config.getUrl(), // <-- WHERE IS THE DB?
                    Config.getUser(), // <-- WHO IS ACCESSING?
                    Config.getPW() // <-- WHAT IS THEIR PASSWORD?
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
            movies.add(createMovie(movieRows));
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
            ps.close();
            return createMovie(movieRows);

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
        for (Movie movie : movies) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO movies (title, year, director, actors, rating, poster, genre, plot) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getYear());
            ps.setString(3, movie.getDirector());
            ps.setString(4, movie.getActors());
            ps.setDouble(5, movie.getRating());
            ps.setString(6, movie.getPoster());
            ps.setString(7, movie.getGenre());
            ps.setString(8, movie.getPlot());
            ps.executeUpdate();
            ps.close();
        }
    }

    @Override
    public void update(Movie movie) throws SQLException {

        int movieId = movie.getId();

        PreparedStatement og = connection.prepareStatement("SELECT * from movies where id = ?");
        og.setInt(1, movieId);
        ResultSet ogInfo = og.executeQuery();
        ogInfo.next();

        PreparedStatement updatedInfo = connection.prepareStatement("UPDATE movies SET title = ?, year = ?, director = ?, actors = ?, rating = ?, poster = ?, genre = ?, plot = ? WHERE id = ?");


            if (movie.getTitle() != null) {
                updatedInfo.setString(1, movie.getTitle());
            }
            else updatedInfo.setString(1, ogInfo.getString("title"));
            if (movie.getYear() != null) {
                updatedInfo.setInt(2, movie.getYear());
            }
            else updatedInfo.setInt(2, ogInfo.getInt("year"));
            if (movie.getDirector() != null) {
                updatedInfo.setString(3, movie.getDirector());
            }
            else updatedInfo.setString(3, ogInfo.getString("director"));
            if (movie.getActors() != null) {
                updatedInfo.setString(4, movie.getActors());
            }
            else updatedInfo.setString(4, ogInfo.getString("actors"));
            if (movie.getRating() != null) {
                updatedInfo.setDouble(5, movie.getRating());
            }
            else updatedInfo.setDouble(5, ogInfo.getDouble("rating"));
            if (movie.getPoster() != null) {
                updatedInfo.setString(6, movie.getPoster());
            }
            else updatedInfo.setString(6, ogInfo.getString("poster"));
            if (movie.getGenre() != null) {
                updatedInfo.setString(7, movie.getGenre());
            }
            else updatedInfo.setString(7, ogInfo.getString("genre"));
            if (movie.getPlot() != null) {
                updatedInfo.setString(8, movie.getPlot());
            }
            else updatedInfo.setString(8, ogInfo.getString("plot"));

        updatedInfo.setInt(9, movieId);
        updatedInfo.executeUpdate();
        ogInfo.close();
        updatedInfo.close();
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("delete from movies where id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
    }

    public Movie createMovie(ResultSet movieRows) {
        try {
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
}

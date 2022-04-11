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
            Movie movie = createMovie(movieRows);
            movies.add(movie);
        }

        ps.close();
        return movies;
    }

    @Override
    public Movie findOne(int id) {

        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM movies WHERE id = ?");
            ps.setInt(1, id);
            ResultSet movieRow = ps.executeQuery();
            movieRow.next();
            Movie movie = createMovie(movieRow);
//            Movie movie = new Movie();
//            movie.setId(movieRow.getInt("id"));
//            movie.setTitle(movieRow.getString("title"));
//            movie.setYear(movieRow.getInt("year"));
//            movie.setRating(movieRow.getInt("rating"));
//            movie.setPoster(movieRow.getString("poster"));
//            movie.setGenre(movieRow.getString("genre"));
//            movie.setDirector(movieRow.getString("director"));
//            movie.setPlot(movieRow.getString("plot"));
//            movie.setActors(movieRow.getString("actors"));

            ps.close();
            return movie;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("findone being called!");
//        try {
//            PreparedStatement ps = connection.prepareStatement("select * from movies where id = ?");
//            ps.setInt(1, id);
//            ps.executeUpdate();
//
//            ResultSet movieRows = ps.executeQuery();
//
//            return createMovie(movieRows);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
        return null;
    }

    @Override
    public void insert(Movie movie) {
        try {
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

            //gets id for record
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            int newId = keys.getInt(1);

            System.out.println("Id of newly added movie is: " + newId);

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertAll(Movie[] movies) throws SQLException {
        for (Movie movie : movies) {
            insert(movie);
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
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("delete from movies where id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public void cleanUp() {
        System.out.println("System cleanup...");

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Movie createMovie(ResultSet movieRow) {
        try {
            Movie movie = new Movie();
            movie.setId(movieRow.getInt("id"));
            movie.setTitle(movieRow.getString("title"));
            movie.setYear(movieRow.getInt("year"));
            movie.setRating(movieRow.getInt("rating"));
            movie.setPoster(movieRow.getString("poster"));
            movie.setGenre(movieRow.getString("genre"));
            movie.setDirector(movieRow.getString("director"));
            movie.setPlot(movieRow.getString("plot"));
            movie.setActors(movieRow.getString("actors"));
            return movie;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

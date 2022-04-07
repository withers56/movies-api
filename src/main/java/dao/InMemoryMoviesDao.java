package dao;

import data.Movie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMoviesDao implements MoviesDao{
    private ArrayList<Movie> movies = new ArrayList<>();
    int nextId = 1;

    @Override
    public List<Movie> all() throws SQLException {
        return movies;
    }

    @Override
    public void insertAll(Movie[] newMovies) throws SQLException {
        for (Movie movie : newMovies) {
            movie.setId(nextId++);
            movies.add(movie);
        }
    }

    @Override
    public void update(Movie updatedMovie) throws SQLException {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == updatedMovie.getId()) {
                updateMovie( i, updatedMovie);
                return;
            }
        }
    }

    @Override
    public void delete(int targetId) throws SQLException {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == targetId) {
                movies.remove(i);
                return;
            }
        }
    }
    //NO SUPPORT YET
    @Override
    public Movie findOne(int id) {
        return null;
    }
    //NO SUPPORT YET
    @Override
    public void insert(Movie movie) {

    }

    public void updateMovie(int index,  Movie updatedMovie) {
        updatedMovie.setId(movies.get(index).getId());

        if (updatedMovie.getTitle() != null) {
            movies.get(index).setTitle(updatedMovie.getTitle());
        }
        if (updatedMovie.getRating() > 0 && updatedMovie.getRating() <= 5) {
            movies.get(index).setRating(updatedMovie.getRating());
        }
        if (updatedMovie.getPoster() != null) {
            movies.get(index).setPoster(updatedMovie.getPoster());
        }
        if (updatedMovie.getYear() > 0) {
            movies.get(index).setYear(updatedMovie.getYear());
        }
        if (updatedMovie.getGenre() != null) {
            movies.get(index).setGenre(updatedMovie.getGenre());
        }
        if (updatedMovie.getDirector() != null) {
            movies.get(index).setDirector(updatedMovie.getDirector());
        }
        if (updatedMovie.getPlot() != null) {
            movies.get(index).setPlot(updatedMovie.getPlot());
        }
        if (updatedMovie.getActors() != null) {
            movies.get(index).setActors(updatedMovie.getActors());
        }
    }
}

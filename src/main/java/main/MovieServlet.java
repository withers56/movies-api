package main;

import com.google.gson.Gson;
import data.Movie;

import java.io.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {



    ArrayList<Movie> movies = new ArrayList<>();
    int nextId = 1;



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        response.setContentType("application/json");
        try{
            PrintWriter out = response.getWriter();
            Movie uncharted = new Movie("Uncharted", 6.7, "poster", 2022, "Hero, Treasure", "Ruben Fleischer"
                    , "Street-smart Nathan Drake is recruited by seasoned treasure hunter Victor \"Sully\" Sullivan to recover a fortune amassed by Ferdinand Magellan, and lost 500 years ago by the House of Moncada."
                    , "Tom Holland, Mark Wahlberg", 100);
            String movieString = new Gson().toJson(movies);

            out.println(movieString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");



        try {
            PrintWriter out = response.getWriter();
            out.println("Movie(s) added.");
            BufferedReader br = request.getReader();

            Movie[] newMovies = new Gson().fromJson(br, Movie[].class);
            for (Movie movie : newMovies) {
                movie.setId(nextId++);
                movies.add(movie);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String [] uriParts = request.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);

//        BufferedReader br = request.getReader();
        Movie updatedMovie = new Gson().fromJson(request.getReader(), Movie.class);

        updatedMovie.setId(movies.get(targetId - 1).getId());

        if (updatedMovie.getTitle() != null) {
            movies.get(targetId - 1).setTitle(updatedMovie.getTitle());
        }
        if (updatedMovie.getRating() > 0 && updatedMovie.getRating() <= 5) {
            movies.get(targetId - 1).setRating(updatedMovie.getRating());
        }
        if (updatedMovie.getPoster() != null) {
            movies.get(targetId - 1).setPoster(updatedMovie.getPoster());
        }
        if (updatedMovie.getYear() > 0) {
            movies.get(targetId - 1).setYear(updatedMovie.getYear());
        }
        if (updatedMovie.getGenre() != null) {
            movies.get(targetId - 1).setGenre(updatedMovie.getGenre());
        }
        if (updatedMovie.getDirector() != null) {
            movies.get(targetId - 1).setDirector(updatedMovie.getDirector());
        }
        if (updatedMovie.getPlot() != null) {
            movies.get(targetId - 1).setPlot(updatedMovie.getPlot());
        }
        if (updatedMovie.getActors() != null) {
            movies.get(targetId - 1).setActors(updatedMovie.getActors());
        }
//        movies.set(targetId - 1, updatedMovie);

        out.println("updated movie with id of: " + targetId);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String [] uriParts = request.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);

        movies.remove(targetId - 1);
        out.println("Removed movie with id of: " + targetId);
    }
}

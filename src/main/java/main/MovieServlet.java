package main;

import com.google.gson.Gson;
import config.Config;
import dao.InMemoryMoviesDao;
import dao.MoviesDao;
import dao.MySqlMoviesDao;
import data.Movie;
import data.MoviesDaoFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {
    private MoviesDao dao = MoviesDaoFactory.getMoviesDao(MoviesDaoFactory.DaoType.MYSQL);

    //fetch all the movies
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{

        String [] uriParts = request.getRequestURI().split("/");

        try{
            int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
            String movieString = new Gson().toJson(dao.findOne(targetId));
            sendOutputToResponse(response, "application/json", movieString);

        }catch (NumberFormatException | SQLException e) {
            try{
                String moviesString = new Gson().toJson(dao.all());

                sendOutputToResponse(response, "application/json", moviesString);
            } catch (SQLException message) {
                message.printStackTrace();
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {




        try {
            Movie[] newMovies = new Gson().fromJson(request.getReader(), Movie[].class);

            dao.insertAll(newMovies);
            sendOutputToResponse(response, "text/plain", "Movie(s) added");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response){
        int targetId = getId(request.getRequestURI());

        try {
            Movie updatedMovie = new Gson().fromJson(request.getReader(), Movie.class);
            updatedMovie.setId(targetId);

            dao.update(updatedMovie);

            sendOutputToResponse(response, "text/plain", "movie updated");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response){
        int targetId = getId(request.getRequestURI());

        try {
            dao.delete(targetId);
            sendOutputToResponse(response,"text/plain",  "Removed the movie");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        dao.cleanUp();
    }

    private void sendOutputToResponse(HttpServletResponse response, String contentType, String output){

        try {
            response.setContentType(contentType);
            PrintWriter out = null;
            out = response.getWriter();
            out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId(String uri){
        String [] uriParts = uri.split("/");
        return Integer.parseInt(uriParts[uriParts.length - 1]);
    }
}

package data;

import config.Config;
import dao.InMemoryMoviesDao;
import dao.MySqlMoviesDao;
import dao.MoviesDao;

public class MoviesDaoFactory {

//    private static Config config = new Config();
    public enum DaoType {MYSQL, IN_MEMORY};

    public static MoviesDao getMoviesDao(DaoType daoType){

        switch(daoType){
            case IN_MEMORY:{
                return new InMemoryMoviesDao();
            }
            case MYSQL:{ // <-- added this
                return new MySqlMoviesDao();
            }
        }
        return null;
    }
}
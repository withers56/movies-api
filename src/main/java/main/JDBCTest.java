package main;

import com.mysql.cj.jdbc.Driver;
import config.Config;

import java.sql.*;

public class JDBCTest {
    private static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        DriverManager.registerDriver(new Driver());
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://" + Config.DB_HOST + ":3306/william",
                Config.DB_USER,
                Config.DB_PW
        );

        //use the connection for sql work
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM albums");

        while (rs.next()) {

            if (rs.getString("artist").equalsIgnoreCase("metallica")){
                System.out.println("Here's an album:");
                System.out.println("  id: " + rs.getLong("id"));
                System.out.println("  artist: " + rs.getString("artist"));
                System.out.println("  name: " + rs.getString("name"));
            }

        }

        st.close();
        connection.close();
    }
}

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
        ResultSet albumRows = st.executeQuery("SELECT * FROM albums");

        while (albumRows.next()) {

            if (albumRows.getString("artist").equalsIgnoreCase("metallica")){
                System.out.println("Here's an album:");
                System.out.println("  id: " + albumRows.getLong("id"));
                System.out.println("  artist: " + albumRows.getString("artist"));
                System.out.println("  name: " + albumRows.getString("name"));
            }

        }

        //general sql prevention technique is use paramaterized queries
        //jdbc calls them prepared statements
        PreparedStatement ps = connection.prepareStatement(
                "insert into albums (artist, name) values (?, ?)"
                    , PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, "jack russel");
        ps.setString(2, "yeetus deleteus");
        ps.executeUpdate();

        ResultSet newkeys = ps.getGeneratedKeys();
        newkeys.next();
        int newId = newkeys.getInt(1);
        System.out.println("New record id is" + newId);

        ps = connection.prepareStatement("update albums set genre = ? where id = ?");

        ps.setString(1, "Metal");
        ps.setInt(2, newId);
        ps.executeUpdate();






        newkeys.close();
        ps.close();
        albumRows.close();
        st.close();
        connection.close();
    }
}

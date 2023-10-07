package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/db/database.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String jdbcUrl = properties.getProperty("jdbc.url");
            String dbUser = properties.getProperty("db.user");
            String dbPassword = properties.getProperty("db.password");

            return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
        } catch (IOException e) {
            throw new SQLException("Unable to load database.properties", e);
        }
    }
}

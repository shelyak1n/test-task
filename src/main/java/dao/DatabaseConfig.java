package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private String jdbcUrl;
    private String dbUser;
    private String dbPassword;

    public DatabaseConfig() {
        loadConfig();
    }

    private void loadConfig() {
        Properties properties = new Properties();
        try (InputStream inputStream = DatabaseConfig.class.getResourceAsStream("/resources/dao/database.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                jdbcUrl = properties.getProperty("jdbc.url");
                dbUser = properties.getProperty("db.user");
                dbPassword = properties.getProperty("db.password");
            } else {
                System.err.println("Unable to load database.properties");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}

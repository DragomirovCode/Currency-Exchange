package ru.dragomirov.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
public class ConnectionUtils {
    private static final String URL = "jdbc:sqlite::resource:db/database.db";
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final HikariDataSource HIKARI_DATA_SOURCE;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setDriverClassName(DRIVER);
        HIKARI_DATA_SOURCE = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return HIKARI_DATA_SOURCE.getConnection();
    }
}

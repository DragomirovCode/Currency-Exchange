package ru.dragomirov.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionDB {
    private static final String URL = "jdbc:sqlite::resource:db/database.db";
    private static Connection connection;

    // Приватный конструктор, чтобы предотвратить создание экземпляров класса
    private ConnectionDB() {}
    public synchronized static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("SQLite JDBC driver not found");
            }
        }
        return connection;
    }
}

package org.example.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySql {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/dots&boxes";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public ConnectionMySql() {

    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

}

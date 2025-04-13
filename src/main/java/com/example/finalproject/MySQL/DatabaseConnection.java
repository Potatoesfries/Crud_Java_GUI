package com.example.finalproject.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection databaseLink;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testJava";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    @SuppressWarnings("CallToPrintStackTrace")
    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
        return databaseLink;
    }
}

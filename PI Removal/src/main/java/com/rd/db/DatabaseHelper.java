package com.rd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private DatabaseHelper() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection(String dbUrl) throws SQLException {
        return DriverManager.getConnection(dbUrl, USER, PASSWORD);
    }
}

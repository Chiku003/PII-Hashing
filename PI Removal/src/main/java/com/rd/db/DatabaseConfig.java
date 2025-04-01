package com.rd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    
    public static final String DEFAULT_TABLE = "users";

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "admin";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}

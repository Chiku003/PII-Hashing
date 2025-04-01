package org.example;

import com.rd.db.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.rd.db.DatabaseConfig.*;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Successfully connected to DB!");
            } else {
                System.out.println("Failed to connect to DB");
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }
}

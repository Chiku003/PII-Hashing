package org.example;

import com.rd.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
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
}

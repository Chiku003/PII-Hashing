package com.rd.db;

import java.sql.*;


import static com.rd.db.DatabaseConfig.DB_URL;
import static com.rd.scanner.PiiScanner.scanAndReplacePii;

public class DataInserter {


    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    // Tables to insert data into
    private static final String TABLE_NAMES = "users";

    public static void main(String[] args) {
        // Sample data to insert
        String[][] sampleData = {
                {"Nikhil", "test@example.com", "Pune, Maharashtra", "M"},
                {"Priya Patel", "priya.patel@example.com", "Bengaluru, Karnataka", "F"},
                {"Ravi Kumar", "ravi.kumar@example.com", "New Delhi, Delhi", "M"},
                {"Anjali Gupta", "anjali.gupta@example.com", "Pune, Maharashtra", "F"}
        };

        // Insert data into each table
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

                createTableIfNotExists(conn, TABLE_NAMES);
                addSampleDataToDatabase(conn, TABLE_NAMES, sampleData);

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Perform PII hashing after inserting data
        doHashing();
    }

    public static void addSampleDataToDatabase(Connection conn, String tableName, String[][] data) {
        String insertQuery = "";

        switch (tableName) {
            case "users":
                insertQuery = "INSERT INTO users (name, email, address, gender) VALUES (?, ?, ?, ?)";
                break;
            case "employees":
                insertQuery = "INSERT INTO employees (name, email, address, position, gender) VALUES (?, ?, ?, ?, ?)";
                break;
            case "customers":
                insertQuery = "INSERT INTO customers (name, email, address, loyalty_status, gender) VALUES (?, ?, ?, ?, ?)";
                break;
            default:
                System.out.println("Unknown table: " + tableName);
                return;
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            for (String[] record : data) {
                preparedStatement.setString(1, record[0]); // name
                preparedStatement.setString(2, record[1]); // email
                preparedStatement.setString(3, record[2]); // address

                if (tableName.equals("employees")) {
                    preparedStatement.setString(4, "Software Engineer"); // Example default position
                    preparedStatement.setString(5, record[3]); // gender
                } else if (tableName.equals("customers")) {
                    preparedStatement.setString(4, "Gold"); // Example default loyalty status
                    preparedStatement.setString(5, record[3]); // gender
                } else {
                    preparedStatement.setString(4, record[3]); // gender
                }

                preparedStatement.executeUpdate();
            }

            System.out.println("Data inserted into table " + tableName);
        } catch (SQLException e) {
            System.err.println("Error inserting data into " + tableName + ": " + e.getMessage());
        }
    }

    private static void createTableIfNotExists(Connection conn, String tableName) {
        String createTableQuery = "";

        switch (tableName) {
            case "users":
                createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(100), " +
                        "email VARCHAR(100), " +
                        "address TEXT, " +
                        "gender VARCHAR(10)" +
                        ");";
                break;
            case "employees":
                createTableQuery = "CREATE TABLE IF NOT EXISTS employees (" +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(100), " +
                        "email VARCHAR(100), " +
                        "address TEXT, " +
                        "position VARCHAR(50) DEFAULT NULL, " +
                        "gender VARCHAR(10)" +
                        ");";
                break;
            case "customers":
                createTableQuery = "CREATE TABLE IF NOT EXISTS customers (" +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(100), " +
                        "email VARCHAR(100), " +
                        "address TEXT, " +
                        "loyalty_status VARCHAR(20) DEFAULT NULL, " +
                        "gender VARCHAR(10)" +
                        ");";
                break;
            default:
                System.out.println("Unknown table: " + tableName);
                return;
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableQuery);
            System.out.println("Table " + tableName + " created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating table " + tableName + ": " + e.getMessage());
        }
    }

    private static void doHashing() {
        scanAndReplacePii(DB_URL);
    }
}

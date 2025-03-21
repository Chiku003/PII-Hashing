package com.rd.scanner;

import com.rd.db.DatabaseHelper;
import com.rd.pattern.PiiPattern;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PiiScanner {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String TABLE_NAME = "users";

    public static void scanAndReplacePii(String dbUrl) {
        try (Connection conn = DatabaseHelper.getConnection(dbUrl)) {
            String selectQuery = "SELECT id, name, email, address, gender FROM " + TABLE_NAME;

            try (PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String address = resultSet.getString("address");
                    String gender = resultSet.getString("gender");

                    // Only hash email if it matches the email pattern
                    String hashedEmail = hashPiiIfMatchesPattern(email, PiiPattern.EMAIL_PATTERN);

                    updateUserPii(conn, id, name, hashedEmail, address, gender);
                }
                System.out.println("PII scanning and replacement completed successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Database error during PII scanning: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String hashPiiIfMatchesPattern(String pii, Pattern pattern) {
        if (pii != null && !pii.isEmpty()) {
            Matcher matcher = pattern.matcher(pii);
            if (matcher.matches()) {
                return hashPii(pii);
            }
        }
        return pii;
    }

    private static String hashPii(String pii) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(pii.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing PII: " + e.getMessage(), e);
        }
    }

    private static void updateUserPii(Connection conn, int id, String name, String email, String address, String gender) {
        String updateQuery = "UPDATE " + TABLE_NAME + " SET name = ?, email = ?, address = ?, gender = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, gender);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating PII for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

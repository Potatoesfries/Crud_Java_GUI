package com.example.finalproject.Model;

import com.example.finalproject.MySQL.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao { // DAO : data access object

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean saveUser(User user) {
        String query = "INSERT INTO users (user_name, user_email, user_password) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public User getUser(String username) {
        String query = "SELECT * FROM users WHERE user_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("user_password");
                return new User(username, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean validateCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE user_name = ? AND user_password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // returns true if a row is found

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
package com.example.finalproject.Controller;

import com.example.finalproject.Model.User;
import com.example.finalproject.Model.UserDao;

public class AuthController {

    private static UserDao userDAO;

    public AuthController() {
        userDAO = new UserDao();
    }

    public boolean register(String username, String email, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {
            return false;
        }

        // Create user
        User newUser = new User(username, email, password);

        // Save user to database
        return userDAO.saveUser(newUser);
    }

    public boolean login(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }

        // Check credentials
        return userDAO.validateCredentials(username, password);
    }
}

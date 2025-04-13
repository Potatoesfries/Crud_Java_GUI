package com.example.finalproject.Controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@SuppressWarnings("CallToPrintStackTrace")
public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;

    private AuthController authController;

    public void initialize() {
        // AuthController will be set from the LoginController
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {

        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        boolean registrationSuccess = authController.register(username, email, password);

        if (registrationSuccess) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Success", "Account created successfully!");
            navigateToLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists or invalid input");
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        navigateToLogin();
    }

    private void navigateToLogin() {
        try {
            // Get current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Load login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/finalproject/Login.fxml"));
            Parent loginView = loader.load();

            // Pass auth controller to login controller
            LoginController loginController = loader.getController();
            loginController.setAuthController(authController);

            // Create and show new scene
            Scene scene = new Scene(loginView, 450, 500);
            stage.setScene(scene);
            stage.show();

            // Make the Panel window draggable (you can move this to PanelController if
            // needed)
            final double[] xOffLogin = { 0 };
            final double[] yOffLogin = { 0 };
            loginView.setOnMousePressed(mouseEvent -> {
                xOffLogin[0] = mouseEvent.getSceneX();
                yOffLogin[0] = mouseEvent.getSceneY();
            });
            loginView.setOnMouseDragged(mouseEvent -> {
                stage.setX(mouseEvent.getScreenX() - xOffLogin[0]);
                stage.setY(mouseEvent.getScreenY() - yOffLogin[0]);
            });

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not open login view");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }
}

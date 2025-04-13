package com.example.finalproject.Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings({ "CallToPrintStackTrace" })
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    // @FXML private Button loginButton;
    @FXML
    private Button registerButton;

    private AuthController authController;

    public void initialize() {
        // Initialize the auth controller
        authController = new AuthController();
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean loginSuccess = authController.login(username, password);

        if (loginSuccess) {
            showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome " + username + "!");
            // Navigate to home screen or dashboard here
            try {
                // Get current stage
                Stage stage = (Stage) ((Node) usernameField).getScene().getWindow();
                // Stage stage = (Stage) usernameField.getScene().getWindow();

                // Load Home.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/finalproject/Panel.fxml"));
                Parent panelView = loader.load();

                // Create and show new scene
                Scene scene = new Scene(panelView);
                stage.setScene(scene);

                stage.setTitle("Library - Library Management System");
                stage.show();

                // Make the Panel window draggable (you can move this to PanelController if
                // needed)
                final double[] xOffsetPanel = { 0 };
                final double[] yOffsetPanel = { 0 };
                panelView.setOnMousePressed(mouseEvent -> {
                    xOffsetPanel[0] = mouseEvent.getSceneX();
                    yOffsetPanel[0] = mouseEvent.getSceneY();
                });
                panelView.setOnMouseDragged(mouseEvent -> {
                    stage.setX(mouseEvent.getScreenX() - xOffsetPanel[0]);
                    stage.setY(mouseEvent.getScreenY() - yOffsetPanel[0]);
                });

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not open Home view");
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
        }
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {
        try {
            // Get current stage
            Stage stage = (Stage) registerButton.getScene().getWindow();

            // Load register FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/finalproject/Register.fxml"));
            Parent registerView = loader.load();
            // Pass auth controller to register controller
            RegisterController registerController = loader.getController();
            registerController.setAuthController(authController);

            // Create and show new scene
            Scene scene = new Scene(registerView);
            stage.setScene(scene);

            stage.setTitle("Register");
            stage.show();

            // Make the Register window draggable
            final double[] xOffsetRegister = { 0 };
            final double[] yOffsetRegister = { 0 };
            registerView.setOnMousePressed(mouseEvent -> {
                xOffsetRegister[0] = mouseEvent.getSceneX();
                yOffsetRegister[0] = mouseEvent.getSceneY();
            });
            registerView.setOnMouseDragged(mouseEvent -> {
                stage.setX(mouseEvent.getScreenX() - xOffsetRegister[0]);
                stage.setY(mouseEvent.getScreenY() - yOffsetRegister[0]);
            });

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not open register view");
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

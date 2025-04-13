package com.example.finalproject;

import com.example.finalproject.MySQL.DatabaseConnection;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private double x, y = 0;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database connection
        initDatabase();

        // Set up the main scene
        scene = new Scene(loadFXML("Login"), 450, 500);

        // Make window draggable
        scene.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        // Set window properties
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Library - Library Management System");

        // Set a default icon or skip if not available
        try {
            // Try to load the icon from resources
            Image appIcon = new Image(getClass().getResourceAsStream("/com/example/finalproject/app_icon.png"));
            if (appIcon != null && !appIcon.isError()) {
                stage.getIcons().add(appIcon);
            } else {
                // Silent fail without error message
                System.out.println("Application icon not found, using default.");
            }
        } catch (Exception e) {
            // Silent fail without error message
            System.out.println("Could not load application icon, using default.");
        }

        stage.show();
    }

    private void initDatabase() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                System.out.println("Database connection successful!");
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();

            // Show error dialog
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Cannot connect to database");
                alert.setContentText("Please make sure MySQL is running and the database 'testJava' exists.\n\n" + e.getMessage());
                alert.showAndWait();
            });
        }
    }

    // Changed from package-private to public
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Changed from package-private to public
    public static void setRoot(Parent root) {
        scene.setRoot(root);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
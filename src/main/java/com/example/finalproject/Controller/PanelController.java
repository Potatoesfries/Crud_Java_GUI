package com.example.finalproject.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.finalproject.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PanelController implements Initializable {

    @FXML
    private StackPane contentArea;
    @FXML
    private Button HomeBtn;
    @FXML
    private Button ManageBtn;
    @FXML
    private Button logoutBtn;

    private static final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;";
    private static final String INACTIVE_BUTTON_STYLE = "-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Load Home view by default and set Home button as active
            Parent fxml = FXMLLoader.load(getClass().getResource("/com/example/finalproject/Home.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);

            // Set Home button as active by default
            HomeBtn.setStyle(ACTIVE_BUTTON_STYLE);
            ManageBtn.setStyle(INACTIVE_BUTTON_STYLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goHomePanel(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/com/example/finalproject/Home.fxml"));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);

        // Update button styles
        HomeBtn.setStyle(ACTIVE_BUTTON_STYLE);
        ManageBtn.setStyle(INACTIVE_BUTTON_STYLE);

        System.out.println("HOME");
    }

    @FXML
    private void goManagePanel(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/com/example/finalproject/Manage.fxml"));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);

        // Update button styles
        HomeBtn.setStyle(INACTIVE_BUTTON_STYLE);
        ManageBtn.setStyle(ACTIVE_BUTTON_STYLE);

        System.out.println("MANAGE");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Get the stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/finalproject/Login.fxml"));
            Parent loginView = loader.load();

            // Create a new scene with the login view
            Scene scene = new Scene(loginView);

            // Set the scene to the stage
            stage.setScene(scene);
            stage.show();

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

            System.out.println("LOGOUT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
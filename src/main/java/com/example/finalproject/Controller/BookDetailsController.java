package com.example.finalproject.Controller;

import java.io.File;
import java.io.IOException;
import com.example.finalproject.Model.Book;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BookDetailsController {

    @FXML
    private Label bookTitle;
    @FXML
    private ImageView bookImage;
    @FXML
    private Label availabilityLabel;
    @FXML
    private Label isbnLabel;
    @FXML
    private Label publisherLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Button closeButton;

    private Book book;
 
    public void setBook(Book book) throws IOException {
        this.book = book;

        // Set book details in the UI
        bookTitle.setText(book.getTitle());
        isbnLabel.setText(book.getIsbn());
        publisherLabel.setText(book.getPublisher());
        quantityLabel.setText(String.valueOf(book.getQuantity()));
        idLabel.setText(String.valueOf(book.getId()));

        // Set availability status
        if (book.getQuantity() > 0) {
            availabilityLabel.setText("Available");
            availabilityLabel.setStyle("-fx-background-color: #2ecc71; -fx-background-radius: 5;");
        } else {
            availabilityLabel.setText("Out of Stock");
            availabilityLabel.setStyle("-fx-background-color: #e74c3c; -fx-background-radius: 5;");
        }

        // Set book image
        setBookImage();

    }

    private void setBookImage() {
        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            File imageFile = new File(book.getImagePath());
            if (imageFile.exists()) {
                try {
                    Image image = new Image(imageFile.toURI().toString());
                    bookImage.setImage(image);
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                    setDefaultImage();
                }
            } else {
                setDefaultImage();
            }
        } else {
            setDefaultImage();
        }
    }

    private void setDefaultImage() {
        try {
            // Try to load a default cover image from resources
            Image defaultCover = new Image(
                    getClass().getResourceAsStream("/com/example/finalproject/default_cover.png"));
            if (defaultCover != null && !defaultCover.isError()) {
                bookImage.setImage(defaultCover);
            } else {
                // Create a simple blue rectangle as default
                bookImage.setImage(new Image(
                        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAAnElEQVR42u3RAQ0AAAgDIN8/9K3hHFSg15Lo5AQhCEEIQhCCEIQgCEEIQhCCEIQgCEEIghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIARBCEIQghCEIARBCEIQghCEIARBCEIQghCEIAQhCEIQBDnWAy+5CzCKDUZgAAAAAElFTkSuQmCC"));
            }
        } catch (Exception e) {
            System.err.println("Error loading default image: " + e.getMessage());
            // Use a simple data URI for a blue rectangle
            bookImage.setImage(new Image(
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAAnElEQVR42u3RAQ0AAAgDIN8/9K3hHFSg15Lo5AQhCEEIQhCCEIQgCEEIQhCCEIQgCEEIghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIARBCEIQghCEIARBCEIQghCEIARBCEIQghCEIAQhCEIQBDnWAy+5CzCKDUZgAAAAAElFTkSuQmCC"));
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        // Close the dialog

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
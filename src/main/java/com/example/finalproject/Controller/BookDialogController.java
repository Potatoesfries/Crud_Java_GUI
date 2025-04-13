package com.example.finalproject.Controller;

import com.example.finalproject.Model.Book;
import com.example.finalproject.Model.BookDao;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BookDialogController {

    @FXML private Label dialogTitle;
    @FXML private TextField titleField;
    @FXML private TextField isbnField;
    @FXML private TextField publisherField;
    @FXML private TextField quantityField;
    @FXML private TextField imagePathField;
    @FXML private ImageView imagePreview;
    @FXML private Button browseButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private BookDao bookDao;
    private Book book;
    private boolean isEditMode = false;
    private File selectedImageFile;
    private static final String IMAGES_DIR = "book_covers";

    public void initialize() {
        // Create the book_covers directory if it doesn't exist
        Path imagesDir = Paths.get(IMAGES_DIR);
        if (!Files.exists(imagesDir)) {
            try {
                Files.createDirectories(imagesDir);
                System.out.println("Created images directory: " + imagesDir.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Error", "Failed to create images directory");
            }
        }
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void initForAdd() {
        isEditMode = false;
        dialogTitle.setText("Add Book");
        book = null;
    }

    public void initForEdit(Book book) {
        isEditMode = true;
        dialogTitle.setText("Edit Book");
        this.book = book;

        // Fill the fields with book data
        titleField.setText(book.getTitle());
        isbnField.setText(book.getIsbn());
        publisherField.setText(book.getPublisher());
        quantityField.setText(String.valueOf(book.getQuantity()));

        // Set image if available
        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            imagePathField.setText(book.getImagePath());
            File imageFile = new File(book.getImagePath());
            if (imageFile.exists()) {
                try {
                    Image image = new Image(imageFile.toURI().toString());
                    imagePreview.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleBrowseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Book Cover Image");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) browseButton.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            imagePathField.setText(selectedImageFile.getAbsolutePath());

            // Show image preview
            try {
                Image image = new Image(selectedImageFile.toURI().toString());
                imagePreview.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Error", "Failed to load image preview");
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        String title = titleField.getText().trim();
        String isbn = isbnField.getText().trim();
        String publisher = publisherField.getText().trim();
        int quantity = Integer.parseInt(quantityField.getText().trim());

        // Process image if selected
        String imagePath = null;
        if (selectedImageFile != null) {
            try {
                // Generate a unique filename to avoid conflicts
                String uniqueFileName = System.currentTimeMillis() + "_" + selectedImageFile.getName();
                Path destination = Paths.get(IMAGES_DIR, uniqueFileName);

                // Copy the file to our app's images directory
                Files.copy(selectedImageFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                imagePath = destination.toString();
                System.out.println("Saved image to: " + imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Error", "Failed to save image file");
                return;
            }
        } else if (isEditMode && book.getImagePath() != null) {
            // Keep existing image if editing and no new image was selected
            imagePath = book.getImagePath();
        }

        boolean success;
        if (isEditMode) {
            // Update existing book
            book.setTitle(title);
            book.setIsbn(isbn);
            book.setPublisher(publisher);
            book.setQuantity(quantity);
            book.setImagePath(imagePath);
            success = bookDao.updateBook(book);
        } else {
            // Create new book
            Book newBook = new Book(title, isbn, publisher, quantity, imagePath);
            success = bookDao.addBook(newBook);
        }

        if (success) {
            closeDialog();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to save the book");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeDialog();
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorMessage += "Title cannot be empty\n";
        }

        if (isbnField.getText() == null || isbnField.getText().trim().isEmpty()) {
            errorMessage += "ISBN cannot be empty\n";
        }

        if (publisherField.getText() == null || publisherField.getText().trim().isEmpty()) {
            errorMessage += "Publisher cannot be empty\n";
        }

        String quantityStr = quantityField.getText();
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            errorMessage += "Quantity cannot be empty\n";
        } else {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    errorMessage += "Quantity must be a positive number\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Quantity must be a valid number\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(AlertType.ERROR, "Invalid Input", errorMessage);
            return false;
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
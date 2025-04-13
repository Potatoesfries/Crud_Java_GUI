package com.example.finalproject.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.finalproject.Model.Book;
import com.example.finalproject.Model.BookDao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BookViewController implements Initializable {

    @FXML private FlowPane booksContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;

    private BookDao bookDao;
    // Remove static initialization to avoid errors on startup
    private static Image DEFAULT_COVER = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the BookDao
        bookDao = new BookDao();

        // Debug
        System.out.println("BookViewController initializing...");

        // Set up Enter key event for search field
        searchField.setOnKeyPressed(this::handleSearchKeyPress);

        // Load books from database
        loadBooks();
    }

    private void handleSearchKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSearch(new ActionEvent());
        }
    }

    private void loadBooks() {
        // Clear existing content
        booksContainer.getChildren().clear();

        // Get all books from the database
        List<Book> books = bookDao.getAllBooks();

        // Debug
        System.out.println("Loaded " + books.size() + " books from database");

        // Add each book to the container
        for (Book book : books) {
            addBookToContainer(book);
            System.out.println("Added book: " + book.getId() + " - " + book.getTitle());
        }
    }

    private void addBookToContainer(Book book) {
        try {
            // Load book card layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/finalproject/BookCard.fxml"));
            VBox bookCard = loader.load();

            // Set book details - Make sure we're finding the correct elements
            ImageView coverImageView = (ImageView) bookCard.lookup("#coverImageView");
            Label titleLabel = (Label) bookCard.lookup("#titleLabel");
            Label publisherLabel = (Label) bookCard.lookup("#publisherLabel");

            // If the publisherLabel isn't found (old layout), try authorLabel as fallback
            if (publisherLabel == null) {
                publisherLabel = (Label) bookCard.lookup("#authorLabel");
            }

            // Debug output to see if elements are found correctly
            System.out.println("Book: " + book.getTitle());
            System.out.println("  titleLabel found: " + (titleLabel != null));
            System.out.println("  publisherLabel found: " + (publisherLabel != null));

            // Explicitly set title and publisher with null checks
            if (titleLabel != null) {
                titleLabel.setText(book.getTitle());
                // Make sure the title is visible and has appropriate constraints
                titleLabel.setVisible(true);
                titleLabel.setManaged(true);
                titleLabel.setWrapText(true);
            } else {
                System.err.println("ERROR: titleLabel not found in BookCard.fxml");
            }

            if (publisherLabel != null) {
                publisherLabel.setText(book.getPublisher());
                publisherLabel.setVisible(true);
                publisherLabel.setManaged(true);
            } else {
                System.err.println("ERROR: publisherLabel/authorLabel not found in BookCard.fxml");
            }

            // Set cover image
            if (coverImageView != null) {
                if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
                    File imageFile = new File(book.getImagePath());
                    if (imageFile.exists()) {
                        try {
                            Image image = new Image(imageFile.toURI().toString());
                            coverImageView.setImage(image);
                        } catch (Exception e) {
                            System.err.println("Error loading image: " + e.getMessage());
                            coverImageView.setImage(getDefaultCover());
                        }
                    } else {
                        coverImageView.setImage(getDefaultCover());
                    }
                } else {
                    coverImageView.setImage(getDefaultCover());
                }
            } else {
                System.err.println("ERROR: coverImageView not found in BookCard.fxml");
            }

            // Add click event to show book details
            bookCard.setOnMouseClicked(event -> {
                System.out.println("Book clicked: " + book.getTitle());
                showBookDetails(book);
            });

            // Add to container
            booksContainer.getChildren().add(bookCard);

        } catch (IOException e) {
            System.err.println("Error creating book card: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showBookDetails(Book book) {
        try {
            // Load the book details dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/finalproject/BookDetails.fxml"));
            Parent root = loader.load();
    
            // Get the controller and set the book
            BookDetailsController controller = loader.getController();
            controller.setBook(book);
    
            // Create and configure the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle(book.getTitle());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED); // Optional: for borderless window
    
            // Create the scene
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
    
            // Make window draggable
            final double[] xOffset = {0};
            final double[] yOffset = {0};
    
            root.setOnMousePressed(event -> {
                xOffset[0] = event.getSceneX();
                yOffset[0] = event.getSceneY();
            });
    
            root.setOnMouseDragged(event -> {
                dialogStage.setX(event.getScreenX() - xOffset[0]);
                dialogStage.setY(event.getScreenY() - yOffset[0]);
            });
    
            // Show the dialog and wait for it to close
            dialogStage.showAndWait();
    
        } catch (IOException e) {
            System.err.println("Error showing book details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Image getDefaultCover() {
        // If we already have a default cover, return it
        if (DEFAULT_COVER != null) {
            return DEFAULT_COVER;
        }

        // Otherwise create a simple colored rectangle as default cover
        try {
            // Try to load from resources one more time
            Image img = new Image(getClass().getResourceAsStream("/com/example/finalproject/default_cover.png"));
            if (!img.isError()) {
                DEFAULT_COVER = img;
                return DEFAULT_COVER;
            }
        } catch (Exception e) {
            System.err.println("Error loading default cover from resources: " + e.getMessage());
        }

        // If all else fails, create a very simple default image
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAAnElEQVR42u3RAQ0AAAgDIN8/9K3hHFSg15Lo5AQhCEEIQhCCEIQgCEEIQhCCEIQgCEEIghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIAQhCEEQghCEIARBCEIQghCEIARBCEIQghCEIARBCEIQghCEIAQhCEIQBDnWAy+5CzCKDUZgAAAAAElFTkSuQmCC", true);
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim();

        // Clear existing content
        booksContainer.getChildren().clear();

        List<Book> books;
        if (keyword.isEmpty()) {
            books = bookDao.getAllBooks();
        } else {
            books = bookDao.searchBooks(keyword);
        }

        // Add search results to container
        for (Book book : books) {
            addBookToContainer(book);
        }

        System.out.println("Search results: " + books.size() + " books found");
    }

    public void refreshBooks() {
        loadBooks();
    }
}
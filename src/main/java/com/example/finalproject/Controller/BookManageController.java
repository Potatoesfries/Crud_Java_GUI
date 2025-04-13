package com.example.finalproject.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.example.finalproject.Model.Book;
import com.example.finalproject.Model.BookDao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookManageController implements Initializable {

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, String> publisherColumn;
    @FXML private TableColumn<Book, Integer> quantityColumn;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;

    private BookDao bookDao;
    private ObservableList<Book> bookList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the BookDao
        bookDao = new BookDao();

        // Debug
        System.out.println("BookManageController initializing...");

        // Configure the table columns - IMPORTANT: names must match Book class property names exactly
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

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
        // Get all books from the database
        List<Book> books = bookDao.getAllBooks();

        // Debug
        System.out.println("Loaded " + books.size() + " books from database");

        // Create a new observable list
        bookList = FXCollections.observableArrayList(books);

        // Important: Set the items to the table
        bookTable.setItems(bookList);

        // More debug
        for (Book book : books) {
            System.out.println("Book: " + book.getId() + " - " + book.getTitle());
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadBooks();
        } else {
            List<Book> books = bookDao.searchBooks(keyword);
            bookList = FXCollections.observableArrayList(books);
            bookTable.setItems(bookList);
            System.out.println("Search found " + books.size() + " books");
        }
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/finalproject/BookDialog.fxml"));
            Parent root = loader.load();

            BookDialogController controller = loader.getController();
            controller.setBookDao(bookDao);
            controller.initForAdd();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Book");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(addButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            // Refresh the table
            loadBooks();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not open the book dialog");
        }
    }

    @FXML
    private void handleEditBook(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a book to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/finalproject/BookDialog.fxml"));
            Parent root = loader.load();

            BookDialogController controller = loader.getController();
            controller.setBookDao(bookDao);
            controller.initForEdit(selectedBook);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Book");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(editButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            // Refresh the table
            loadBooks();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not open the book dialog");
        }
    }

    @FXML
    private void handleDeleteBook(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a book to delete");
            return;
        }

        // Confirm deletion
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Book");
        alert.setContentText("Are you sure you want to delete the book: " + selectedBook.getTitle() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = bookDao.deleteBook(selectedBook.getId());
            if (success) {
                bookList.remove(selectedBook);
                showAlert(AlertType.INFORMATION, "Success", "Book deleted successfully");
                loadBooks(); // Refresh the table
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to delete the book");
            }
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadBooks();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package com.example.finalproject.Model;

import com.example.finalproject.MySQL.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    // SQL to alter table if needed
    private static final String ALTER_TABLE_SQL =
            "ALTER TABLE books ADD COLUMN IF NOT EXISTS image_path VARCHAR(255)";

    public BookDao() {
        // Check if the image_path column exists, and add it if not
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(ALTER_TABLE_SQL);
        } catch (SQLException e) {
            System.err.println("Error checking/adding image_path column: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean addBook(Book book) {
        String query = "INSERT INTO books (title, isbn, publisher, quantity, image_path) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getIsbn());
            stmt.setString(3, book.getPublisher());
            stmt.setInt(4, book.getQuantity());
            stmt.setString(5, book.getImagePath());

            int rowsInserted = stmt.executeUpdate();
            System.out.println("Added book: " + book.getTitle() + ", rows affected: " + rowsInserted);
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean updateBook(Book book) {
        String query = "UPDATE books SET title = ?, isbn = ?, publisher = ?, quantity = ?, image_path = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getIsbn());
            stmt.setString(3, book.getPublisher());
            stmt.setInt(4, book.getQuantity());
            stmt.setString(5, book.getImagePath());
            stmt.setInt(6, book.getId());

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Updated book ID " + book.getId() + ", rows affected: " + rowsUpdated);
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean deleteBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            System.out.println("Deleted book ID " + id + ", rows affected: " + rowsDeleted);
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public Book getBook(int id) {
        String query = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String title = rs.getString("title");
                String isbn = rs.getString("isbn");
                String publisher = rs.getString("publisher");
                int quantity = rs.getInt("quantity");
                String imagePath = rs.getString("image_path");

                Book book = new Book(id, title, isbn, publisher, quantity, imagePath);
                System.out.println("Retrieved book: " + book.getId() + " - " + book.getTitle());
                return book;
            }

        } catch (SQLException e) {
            System.err.println("Error getting book: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Database connection established for getAllBooks()");

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                System.out.println("Executing query: " + query);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String isbn = rs.getString("isbn");
                    String publisher = rs.getString("publisher");
                    int quantity = rs.getInt("quantity");
                    String imagePath = rs.getString("image_path");

                    Book book = new Book(id, title, isbn, publisher, quantity, imagePath);
                    books.add(book);

                    System.out.println("Found book: " + id + " - " + title);
                }

                System.out.println("Total books found: " + books.size());
            }

        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        // Improved query to search case-insensitively
        String query = "SELECT * FROM books WHERE " +
                "LOWER(title) LIKE LOWER(?) OR " +
                "LOWER(isbn) LIKE LOWER(?) OR " +
                "LOWER(publisher) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String searchTerm = "%" + keyword + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            System.out.println("Searching for: " + keyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String isbn = rs.getString("isbn");
                    String publisher = rs.getString("publisher");
                    int quantity = rs.getInt("quantity");
                    String imagePath = rs.getString("image_path");

                    Book book = new Book(id, title, isbn, publisher, quantity, imagePath);
                    books.add(book);

                    System.out.println("Found book in search: " + id + " - " + title);
                }

                System.out.println("Search returned " + books.size() + " books");
            }

        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }
}
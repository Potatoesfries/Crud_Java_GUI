package com.example.finalproject.Model;

public class Book {
    private int id;
    private String title;
    private String isbn;
    private String publisher;
    private int quantity;
    private String imagePath;

    public Book(int id, String title, String isbn, String publisher, int quantity, String imagePath) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public Book(String title, String isbn, String publisher, int quantity, String imagePath) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    // Constructor without image path for backward compatibility
    public Book(int id, String title, String isbn, String publisher, int quantity) {
        this(id, title, isbn, publisher, quantity, null);
    }

    // Constructor without image path for backward compatibility
    public Book(String title, String isbn, String publisher, int quantity) {
        this(title, isbn, publisher, quantity, null);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", isbn=" + isbn + ", publisher=" + publisher + ", quantity="
                + quantity + ", imagePath=" + imagePath + "]";
    }
}
package com.example.finalproject.Model;

public class User {

    private String Username;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.Username = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String password) {
        this.Username = name;
        this.password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String name) {
        this.Username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

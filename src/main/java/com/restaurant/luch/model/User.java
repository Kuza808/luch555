package com.restaurant.luch.model;

public class User {
    private int userId;
    private String username;
    private String phoneNumber;
    private String fullName;
    private String password;
    private String email;
    private boolean isAdmin;

    public User(int userId, String username, String phoneNumber, String fullName, String password, String email, boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return isAdmin; }
}

package com.restaurant.luch.models;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String phoneNumber;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;
    private LocalDateTime createdAt;

    public User() {}

    public User(String phoneNumber, String fullName, String username, String password) {
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

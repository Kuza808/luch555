package com.restaurant.luch.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private int bookingId;
    private int userId;
    private String userName;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private int guestsCount;
    private String specialRequests;
    private String status;

    public Booking() {}

    public Booking(int userId, LocalDate bookingDate, LocalTime bookingTime, int guestsCount) {
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.guestsCount = guestsCount;
        this.status = "pending";
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public LocalTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }

    public int getGuestsCount() { return guestsCount; }
    public void setGuestsCount(int guestsCount) { this.guestsCount = guestsCount; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

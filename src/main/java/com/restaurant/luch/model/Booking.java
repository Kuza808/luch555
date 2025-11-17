package com.restaurant.luch.model;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Модель бронирования столика
 */
public class Booking {
    private int id;
    private int userId;
    private String guestName;
    private String phone;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private int guestsCount;
    private String tableType;
    private String status;
    private String specialRequests;
    private LocalDateTime createdDate;

    public Booking() {
        this.status = "PENDING";
        this.createdDate = LocalDateTime.now();
    }

    public Booking(int userId, String guestName, String phone, LocalDate bookingDate,
                   LocalTime bookingTime, int guestsCount, String tableType) {
        this();
        this.userId = userId;
        this.guestName = guestName;
        this.phone = phone;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.guestsCount = guestsCount;
        this.tableType = tableType;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public LocalTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }

    public int getGuestsCount() { return guestsCount; }
    public void setGuestsCount(int guestsCount) { this.guestsCount = guestsCount; }

    public String getTableType() { return tableType; }
    public void setTableType(String tableType) { this.tableType = tableType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public String getStatusDisplay() {
        return switch(status) {
            case "PENDING" -> "⏳ Ожидает подтверждения";
            case "CONFIRMED" -> "✅ Подтверждено";
            case "SEATED" -> "🪑 Гости размещены";
            case "COMPLETED" -> "🏁 Завершено";
            case "CANCELLED" -> "❌ Отменено";
            default -> status;
        };
    }
}

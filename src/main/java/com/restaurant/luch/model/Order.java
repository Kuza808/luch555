package com.restaurant.luch.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель заказа в ресторане
 */
public class Order {
    private int id;
    private int userId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private String orderType;
    private String specialRequests;

    public Order() {
        this.items = new ArrayList<>();
        this.status = "PENDING";
        this.orderDate = LocalDateTime.now();
    }

    public Order(int userId, String orderType) {
        this();
        this.userId = userId;
        this.orderType = orderType;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        calculateTotal();
    }

    public void removeItem(OrderItem item) {
        this.items.remove(item);
        calculateTotal();
    }

    private void calculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getDish().getPrice() * item.getQuantity())
                .sum();
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) {
        this.items = items;
        calculateTotal();
    }

    public double getTotalAmount() { return totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public String getFormattedTotal() {
        return String.format("%.0f ₽", totalAmount);
    }

    public String getStatusDisplay() {
        return switch(status) {
            case "PENDING" -> "⏳ Ожидает подтверждения";
            case "CONFIRMED" -> "✅ Подтвержден";
            case "PREPARING" -> "👨‍🍳 Готовится";
            case "READY" -> "🎉 Готов к выдаче";
            case "COMPLETED" -> "🏁 Завершен";
            case "CANCELLED" -> "❌ Отменен";
            default -> status;
        };
    }

    public int getTotalItems() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }
}
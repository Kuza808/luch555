package com.restaurant.luch.model;

import javafx.collections.ObservableList;
import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int userId;
    private String deliveryAddress;
    private String paymentMethod;
    private double totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;
    private ObservableList<CartItem> orderItems;

    public Order() {}

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public ObservableList<CartItem> getOrderItems() { return orderItems; }
    public void setOrderItems(ObservableList<CartItem> orderItems) { this.orderItems = orderItems; }
}

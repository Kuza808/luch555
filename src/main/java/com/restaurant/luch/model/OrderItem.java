package com.restaurant.luch.model;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int dishId;
    private int quantity;
    private double price;

    public OrderItem() {}

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getDishId() { return dishId; }
    public void setDishId(int dishId) { this.dishId = dishId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

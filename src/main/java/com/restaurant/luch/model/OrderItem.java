package com.restaurant.luch.model;

/**
 * Модель элемента заказа
 */
public class OrderItem {
    private Dish dish;
    private int quantity;
    private String specialRequests;

    public OrderItem() {}

    public OrderItem(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    // Геттеры и сеттеры
    public Dish getDish() { return dish; }
    public void setDish(Dish dish) { this.dish = dish; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public double getItemTotal() {
        return dish.getPrice() * quantity;
    }

    public String getFormattedItemTotal() {
        return String.format("%.0f ₽", getItemTotal());
    }
}
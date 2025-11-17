package com.restaurant.luch.model;

public class Dish {
    private int dishId;
    private String dishName;
    private String description;
    private double price;
    private int categoryId;

    public Dish(int dishId, String dishName, String description, double price, int categoryId) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
    }

    public int getDishId() { return dishId; }
    public String getDishName() { return dishName; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getCategoryId() { return categoryId; }
}

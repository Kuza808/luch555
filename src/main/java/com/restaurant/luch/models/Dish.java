package com.restaurant.luch.models;

public class Dish {
    private int dishId;
    private String dishName;
    private String description;
    private double price;
    private int categoryId;
    private String imagePath;
    private boolean isAvailable;
    private boolean isFavorite;

    public Dish() {}

    public Dish(String dishName, String description, double price, int categoryId) {
        this.dishName = dishName;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.isAvailable = true;
    }

    public int getDishId() { return dishId; }
    public void setDishId(int dishId) { this.dishId = dishId; }

    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}

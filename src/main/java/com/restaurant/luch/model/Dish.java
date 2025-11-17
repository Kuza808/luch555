package com.restaurant.luch.model;

import java.util.Arrays;
import java.util.List;

/**
 * Модель блюда в ресторане
 */
public class Dish {
    private int id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private List<String> ingredients;
    private List<String> allergens;
    private int cookingTime;
    private boolean isSeasonal;
    private boolean isAvailable;
    private double calories;

    public Dish() {}

    public Dish(int id, String name, String description, double price, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = true;
        this.cookingTime = 20;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public List<String> getAllergens() { return allergens; }
    public void setAllergens(List<String> allergens) { this.allergens = allergens; }

    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }

    public boolean isSeasonal() { return isSeasonal; }
    public void setSeasonal(boolean seasonal) { isSeasonal = seasonal; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }

    public String getFormattedPrice() {
        return String.format("%.0f ₽", price);
    }

    public String getCookingTimeDisplay() {
        return cookingTime + " мин";
    }

    public String getCaloriesDisplay() {
        return String.format("%.0f ккал", calories);
    }
}
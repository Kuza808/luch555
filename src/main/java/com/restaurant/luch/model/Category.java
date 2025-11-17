package com.restaurant.luch.model;

public class Category {
    private int categoryId;
    private String categoryName;
    private int displayOrder;

    public Category() {}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    @Override
    public String toString() {
        return categoryName;
    }
}

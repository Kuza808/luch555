package com.restaurant.luch.models;

import java.time.LocalDateTime;

public class Favorite {
    private int favoriteId;
    private int userId;
    private int dishId;
    private LocalDateTime addedAt;

    public Favorite() {}

    public Favorite(int userId, int dishId) {
        this.userId = userId;
        this.dishId = dishId;
    }

    public int getFavoriteId() { return favoriteId; }
    public void setFavoriteId(int favoriteId) { this.favoriteId = favoriteId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getDishId() { return dishId; }
    public void setDishId(int dishId) { this.dishId = dishId; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}

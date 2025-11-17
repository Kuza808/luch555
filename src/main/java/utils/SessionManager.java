package com.restaurant.luch.utils;

import com.restaurant.luch.model.User;
import com.restaurant.luch.model.CartItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private ObservableList<CartItem> cart;

    private SessionManager() {
        cart = FXCollections.observableArrayList();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
        cart.clear();
    }

    public ObservableList<CartItem> getCart() {
        return cart;
    }

    public void addToCart(CartItem item) {
        cart.add(item);
    }

    public void removeFromCart(CartItem item) {
        cart.remove(item);
    }

    public void clearCart() {
        cart.clear();
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.getDish().getPrice() * item.getQuantity();
        }
        return total;
    }
}

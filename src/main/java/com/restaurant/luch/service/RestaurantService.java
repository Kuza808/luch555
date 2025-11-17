package com.restaurant.luch.service;

import com.restaurant.luch.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RestaurantService {
    private static RestaurantService instance;

    private RestaurantService() {}

    public static RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    public ObservableList<Dish> getMenuItems() {
        return FXCollections.observableArrayList();
    }

    public ObservableList<Booking> getBookings() {
        return FXCollections.observableArrayList();
    }
}

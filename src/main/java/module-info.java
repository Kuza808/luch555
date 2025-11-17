module com.restaurant.luch {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;
    requires java.sql;

    opens com.restaurant.luch to javafx.fxml, com.google.gson;
    opens com.restaurant.luch.controller to javafx.fxml, com.google.gson;
    opens com.restaurant.luch.model to javafx.fxml, com.google.gson;
    opens com.restaurant.luch.utils to javafx.fxml;

    exports com.restaurant.luch;
    exports com.restaurant.luch.controllers;
    exports com.restaurant.luch.model;
    exports com.restaurant.luch.database;
    exports com.restaurant.luch.utils;
    exports com.restaurant.luch.config;
}

module com.restaurant.luch {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.restaurant.luch to javafx.fxml;
    opens com.restaurant.luch.controller to javafx.fxml;
    opens com.restaurant.luch.model to javafx.fxml;
    opens com.restaurant.luch.service to javafx.fxml;

    exports com.restaurant.luch;
    exports com.restaurant.luch.controller;
    exports com.restaurant.luch.model;
    exports com.restaurant.luch.service;
}
package com.restaurant.luch.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.restaurant.luch.model.User;
import com.restaurant.luch.utils.SessionManager;
import java.io.IOException;

public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Button backButton;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            nameLabel.setText("Имя: " + user.getFullName());
            usernameLabel.setText("Логин: " + user.getUsername());
            phoneLabel.setText("Телефон: " + user.getPhoneNumber());
            emailLabel.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "Не указан"));
        }
    }

    @FXML
    private void handleBack() {
        loadScene("mainpage.fxml", "Главное меню");
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        loadScene("login.fxml", "Вход");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/" + fxmlFile));
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

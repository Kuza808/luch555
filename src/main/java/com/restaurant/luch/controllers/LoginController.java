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

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        System.out.println("LoginController инициализирован");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Заполните все поля");
            return;
        }

        // Тестовые учетные данные
        if ("admin".equals(username) && "admin".equals(password)) {
            User user = new User(1, "admin", "+1234567890", "Admin User", "admin", "admin@test.com", true);
            SessionManager.getInstance().setCurrentUser(user);
            loadScene("mainpage.fxml", "Главное меню");
        } else if ("user".equals(username) && "user".equals(password)) {
            User user = new User(2, "user", "+9876543210", "Regular User", "user", "user@test.com", false);
            SessionManager.getInstance().setCurrentUser(user);
            loadScene("mainpage.fxml", "Главное меню");
        } else {
            showError("Неверные учетные данные!");
        }
    }

    @FXML
    private void handleRegister() {
        loadScene("register.fxml", "Регистрация");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/" + fxmlFile));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

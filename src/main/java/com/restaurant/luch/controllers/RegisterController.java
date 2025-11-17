package com.restaurant.luch.controllers;

import com.restaurant.luch.database.UserDAO;
import com.restaurant.luch.model.User;
import com.restaurant.luch.utils.ValidationUtils;
import com.restaurant.luch.utils.NotificationUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {

    @FXML private TextField phoneField;
    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField;
    @FXML private Button registerButton;
    @FXML private Button backButton;

    @FXML
    private void handleRegister() {
        String phone = phoneField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText().trim();

        if (phone.isEmpty() || fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            NotificationUtils.showWarning("Предупреждение", "Пожалуйста, заполните все обязательные поля");
            return;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            NotificationUtils.showWarning("Предупреждение", "Неверный формат номера телефона");
            return;
        }

        if (!password.equals(confirmPassword)) {
            NotificationUtils.showWarning("Предупреждение", "Пароли не совпадают");
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            NotificationUtils.showWarning("Предупреждение", "Пароль должен содержать минимум 6 символов");
            return;
        }

        if (UserDAO.usernameExists(username)) {
            NotificationUtils.showWarning("Предупреждение", "Это имя пользователя уже занято");
            return;
        }

        if (UserDAO.phoneExists(phone)) {
            NotificationUtils.showWarning("Предупреждение", "Этот номер телефона уже зарегистрирован");
            return;
        }

        User newUser = new User(phone, fullName, username, password);
        newUser.setEmail(email.isEmpty() ? null : email);

        if (UserDAO.registerUser(newUser)) {
            NotificationUtils.showSuccess("Успех", "Регистрация успешна! Теперь вы можете войти.");
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(this::handleBack);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            NotificationUtils.showError("Ошибка", "Ошибка регистрации. Попробуйте еще раз.");
        }
    }

    @FXML
    private void handleBack() {
        loadScene("Login.fxml", "Вход");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/" + fxmlFile));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/green-theme.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

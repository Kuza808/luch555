package com.restaurant.luch.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Контроллер для окна авторизации
 */
public class AuthController {

    @FXML private TextField loginEmailField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Label loginErrorLabel;
    @FXML private Button loginButton;

    /**
     * Метод инициализации контроллера
     */
    @FXML
    public void initialize() {
        System.out.println("AuthController инициализирован");
        clearErrors();
    }

    /**
     * Обработчик нажатия кнопки "Войти"
     */
    @FXML
    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showLoginError("Заполните все поля");
            return;
        }

        if (isValidCredentials(email, password)) {
            openMainMenu();
        } else {
            showLoginError("Неверный email или пароль");
        }
    }

    /**
     * Открыть главное меню
     */
    private void openMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/restaurant/luch/main-view.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) loginEmailField.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 800);

            currentStage.setTitle("Ресторан 'Луч' - Главное меню");
            currentStage.setScene(scene);
            currentStage.centerOnScreen();

            System.out.println("Главное меню открыто");

        } catch (IOException e) {
            System.err.println("Ошибка загрузки главного меню: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Ошибка",
                    "Не удалось загрузить главное меню: " + e.getMessage());
        }
    }

    /**
     * Временная проверка учетных данных
     */
    private boolean isValidCredentials(String email, String password) {
        return "guest@luch.ru".equals(email) && "123456".equals(password);
    }

    /**
     * Показать ошибку входа
     */
    private void showLoginError(String message) {
        loginErrorLabel.setText(message);
        loginErrorLabel.setVisible(true);
    }

    /**
     * Очистить ошибки
     */
    private void clearErrors() {
        loginErrorLabel.setVisible(false);
    }

    /**
     * Показать всплывающее сообщение
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Обработчик наведения мыши на кнопку
     */
    @FXML
    private void handleMouseEnter() {
        if (!loginButton.isDisable()) {
            loginButton.setStyle("-fx-background-color: #A0522D; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 12 30; -fx-font-size: 14px; -fx-cursor: hand;");
        }
    }

    /**
     * Обработчик ухода мыши с кнопки
     */
    @FXML
    private void handleMouseExit() {
        if (!loginButton.isDisable()) {
            loginButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 12 30; -fx-font-size: 14px; -fx-cursor: hand;");
        }
    }
}
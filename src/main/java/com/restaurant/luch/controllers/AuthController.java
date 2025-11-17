package com.restaurant.luch.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class AuthController {

    @FXML private TextField loginEmailField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Label loginErrorLabel;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        System.out.println("AuthController инициализирован");
        clearErrors();
    }

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

    private void openMainMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/MainPage.fxml"));
            Stage currentStage = (Stage) loginEmailField.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/styles/green-theme.css").toExternalForm());
            currentStage.setTitle("Ресторан 'Луч' - Главное меню");
            currentStage.setScene(scene);
            currentStage.centerOnScreen();
            System.out.println("Главное меню открыто");
        } catch (IOException e) {
            System.err.println("Ошибка загрузки главного меню: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось загрузить главное меню: " + e.getMessage());
        }
    }

    private boolean isValidCredentials(String email, String password) {
        return "guest@luch.ru".equals(email) && "123456".equals(password);
    }

    private void showLoginError(String message) {
        loginErrorLabel.setText(message);
        loginErrorLabel.setVisible(true);
    }

    private void clearErrors() {
        loginErrorLabel.setVisible(false);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleMouseEnter() {
        if (!loginButton.isDisable()) {
            loginButton.setStyle("-fx-background-color: #A0522D; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 12 30; -fx-font-size: 14px; -fx-cursor: hand;");
        }
    }

    @FXML
    private void handleMouseExit() {
        if (!loginButton.isDisable()) {
            loginButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 12 30; -fx-font-size: 14px; -fx-cursor: hand;");
        }
    }
}

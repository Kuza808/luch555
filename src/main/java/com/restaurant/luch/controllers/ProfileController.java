package controllers;

import database.FavoriteDAO;
import models.Dish;
import utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.io.IOException;

public class ProfileController {

    @FXML private Label userNameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label usernameLabel;
    @FXML private TextField emailField;
    @FXML private Button saveEmailButton;
    @FXML private Button logoutButton;
    @FXML private Button backButton;
    @FXML private FlowPane favoritesContainer;

    @FXML
    private void initialize() {
        loadUserInfo();
        loadFavorites();
    }

    private void loadUserInfo() {
        if (SessionManager.getInstance().isLoggedIn()) {
            var user = SessionManager.getInstance().getCurrentUser();
            userNameLabel.setText(user.getFullName());
            phoneLabel.setText(user.getPhoneNumber());
            usernameLabel.setText("@" + user.getUsername());
            emailField.setText(user.getEmail() != null ? user.getEmail() : "");
        }
    }

    private void loadFavorites() {
        favoritesContainer.getChildren().clear();
        int userId = SessionManager.getInstance().getCurrentUser().getUserId();
        ObservableList<Dish> favorites = FavoriteDAO.getFavoriteDishes(userId);

        if (favorites.isEmpty()) {
            Label emptyLabel = new Label("У вас пока нет избранных блюд");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #757575; -fx-padding: 20;");
            favoritesContainer.getChildren().add(emptyLabel);
        } else {
            for (Dish dish : favorites) {
                VBox dishCard = createFavoriteDishCard(dish);
                favoritesContainer.getChildren().add(dishCard);
            }
        }
    }

    private VBox createFavoriteDishCard(Dish dish) {
        VBox card = new VBox(8);
        card.getStyleClass().add("dish-card");
        card.setPrefWidth(180);
        card.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-background-radius: 8;");

        Label nameLabel = new Label(dish.getDishName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);

        Label priceLabel = new Label(String.format("%.2f ₽", dish.getPrice()));
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32;");

        Button removeButton = new Button("Удалить из избранного");
        removeButton.setStyle("-fx-font-size: 11px; -fx-background-color: #c70000; -fx-text-fill: white;");
        removeButton.setOnAction(e -> {
            int userId = SessionManager.getInstance().getCurrentUser().getUserId();
            FavoriteDAO.removeFromFavorites(userId, dish.getDishId());
            loadFavorites();
        });

        card.getChildren().addAll(nameLabel, priceLabel, removeButton);
        return card;
    }

    @FXML
    private void handleSaveEmail() {
        String email = emailField.getText().trim();
        // Здесь добавьте код для сохранения email в базу данных
        // UserDAO.updateEmail(userId, email);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Email сохранен");
        alert.setHeaderText(null);
        alert.setContentText("Email успешно обновлен!");
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите выйти?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            SessionManager.getInstance().logout();
            loadScene("Login.fxml", "Вход");
        }
    }

    @FXML
    private void handleBack() {
        loadScene("MainPage.fxml", "Главная страница");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/" + fxmlFile));
            Stage stage = (Stage) backButton.getScene().getWindow();
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

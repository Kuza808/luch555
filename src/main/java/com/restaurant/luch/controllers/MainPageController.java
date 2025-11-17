
package com.restaurant.luch.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import com.restaurant.luch.model.Dish;
import com.restaurant.luch.model.CartItem;
import com.restaurant.luch.utils.SessionManager;
import com.restaurant.luch.utils.NotificationUtils;
import java.io.IOException;

public class MainPageController {

    @FXML private Label userNameLabel;
    @FXML private Button cartButton;
    @FXML private Button profileButton;
    @FXML private FlowPane dishesContainer;
    @FXML private Label cartCountLabel;

    @FXML
    public void initialize() {
        if (SessionManager.getInstance().isLoggedIn()) {
            userNameLabel.setText(SessionManager.getInstance().getCurrentUser().getFullName());
        }

        displaySampleDishes();
        updateCartCount();
    }

    private void displaySampleDishes() {
        dishesContainer.setHgap(20);
        dishesContainer.setVgap(20);

        String[] dishNames = {"Паста", "Пицца", "Салат", "Суп", "Напиток"};
        double[] prices = {350, 500, 250, 150, 80};

        for (int i = 0; i < dishNames.length; i++) {
            VBox dishCard = createDishCard(i + 1, dishNames[i], prices[i]);
            dishesContainer.getChildren().add(dishCard);
        }
    }

    private VBox createDishCard(int id, String name, double price) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 15; -fx-background-color: white;");
        card.setPrefWidth(200);
        card.setAlignment(Pos.TOP_CENTER);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label priceLabel = new Label(price + " ₽");
        priceLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #2e7d32;");

        Button addBtn = new Button("В корзину");
        addBtn.setPrefWidth(180);
        addBtn.setStyle("-fx-font-size: 14; -fx-background-color: #2e7d32; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            Dish dish = new Dish(id, name, "", price, 1);
            CartItem item = new CartItem(dish, 1);
            SessionManager.getInstance().addToCart(item);
            updateCartCount();
            NotificationUtils.showSuccess("Успех", name + " добавлено в корзину!");
        });

        card.getChildren().addAll(nameLabel, priceLabel, addBtn);
        return card;
    }

    private void updateCartCount() {
        int count = SessionManager.getInstance().getCart().size();
        cartCountLabel.setText(String.valueOf(count));
    }

    @FXML
    private void handleCart() {
        loadScene("cart.fxml", "Корзина");
    }

    @FXML
    private void handleProfile() {
        loadScene("profile.fxml", "Профиль");
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        loadScene("login.fxml", "Вход");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/" + fxmlFile));
            Stage stage = (Stage) cartButton.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            NotificationUtils.showError("Ошибка", "Не удалось загрузить экран: " + fxmlFile);
        }
    }
}

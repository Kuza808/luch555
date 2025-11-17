package com.restaurant.luch.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.restaurant.luch.model.CartItem;
import com.restaurant.luch.utils.SessionManager;
import com.restaurant.luch.utils.NotificationUtils;
import java.io.IOException;

public class CartController {

    @FXML private VBox cartItemsContainer;
    @FXML private Label totalLabel;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private Button placeOrderButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        paymentMethodCombo.getItems().addAll("Наличные", "Карта", "Онлайн");
        paymentMethodCombo.setValue("Карта");
        displayCartItems();
        updateTotal();
    }

    private void displayCartItems() {
        cartItemsContainer.getChildren().clear();

        for (CartItem item : SessionManager.getInstance().getCart()) {
            HBox itemRow = new HBox(10);
            itemRow.setStyle("-fx-padding: 10; -fx-border-color: #eee;");

            Label nameLabel = new Label(item.getDish().getDishName());
            nameLabel.setPrefWidth(150);

            Label priceLabel = new Label(item.getDish().getPrice() + " ₽");
            priceLabel.setPrefWidth(100);

            Spinner<Integer> quantitySpinner = new Spinner<>(1, 100, item.getQuantity());
            quantitySpinner.setPrefWidth(80);
            quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                item.setQuantity(newVal);
                updateTotal();
            });

            Button removeBtn = new Button("Удалить");
            removeBtn.setOnAction(e -> {
                SessionManager.getInstance().removeFromCart(item);
                displayCartItems();
                updateTotal();
            });

            itemRow.getChildren().addAll(nameLabel, priceLabel, quantitySpinner, removeBtn);
            cartItemsContainer.getChildren().add(itemRow);
        }
    }

    private void updateTotal() {
        double total = SessionManager.getInstance().getCartTotal();
        totalLabel.setText("Итого: " + total + " ₽");
    }

    @FXML
    private void handlePlaceOrder() {
        String address = addressField.getText().trim();
        String paymentMethod = paymentMethodCombo.getValue();

        if (address.isEmpty()) {
            NotificationUtils.showWarning("Предупреждение", "Введите адрес доставки");
            return;
        }

        if (SessionManager.getInstance().getCart().isEmpty()) {
            NotificationUtils.showWarning("Предупреждение", "Корзина пуста");
            return;
        }

        NotificationUtils.showSuccess("Успех", "Спасибо за заказ! Мы с вами скоро свяжемся.");
        SessionManager.getInstance().clearCart();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        handleBack();
    }

    @FXML
    private void handleBack() {
        loadScene("mainpage.fxml", "Главное меню");
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

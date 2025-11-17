package controllers;

import database.OrderDAO;
import models.CartItem;
import models.Order;
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

public class CartController {

    @FXML private VBox cartItemsContainer;
    @FXML private Label totalLabel;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private Button placeOrderButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        // Заполнить способы оплаты
        paymentMethodCombo.getItems().addAll("Наличными", "Картой", "Онлайн оплата");
        paymentMethodCombo.setValue("Наличными");

        loadCartItems();
        updateTotal();
    }

    private void loadCartItems() {
        cartItemsContainer.getChildren().clear();
        ObservableList<CartItem> cart = SessionManager.getInstance().getCart();

        if (cart.isEmpty()) {
            Label emptyLabel = new Label("Корзина пуста");
            emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #757575;");
            cartItemsContainer.getChildren().add(emptyLabel);
            placeOrderButton.setDisable(true);
        } else {
            for (CartItem item : cart) {
                HBox itemBox = createCartItemBox(item);
                cartItemsContainer.getChildren().add(itemBox);
            }
            placeOrderButton.setDisable(false);
        }
    }

    private HBox createCartItemBox(CartItem item) {
        HBox box = new HBox(20);
        box.getStyleClass().add("cart-item");
        box.setPrefHeight(80);
        box.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");

        // Название блюда
        Label nameLabel = new Label(item.getDish().getDishName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        nameLabel.setPrefWidth(200);

        // Цена за единицу
        Label priceLabel = new Label(String.format("%.2f ₽", item.getDish().getPrice()));
        priceLabel.setStyle("-fx-font-size: 14px;");
        priceLabel.setPrefWidth(80);

        // Количество
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 50, item.getQuantity());
        quantitySpinner.setPrefWidth(80);
        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            item.setQuantity(newVal);
            updateTotal();
        });

        // Промежуточная сумма
        Label subtotalLabel = new Label(String.format("%.2f ₽", item.getSubtotal()));
        subtotalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        subtotalLabel.setPrefWidth(100);

        // Обновление промежуточной суммы при изменении количества
        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            subtotalLabel.setText(String.format("%.2f ₽", item.getSubtotal()));
        });

        // Кнопка удаления
        Button removeButton = new Button("✖");
        removeButton.setStyle("-fx-background-color: #c70000; -fx-text-fill: white; -fx-font-weight: bold;");
        removeButton.setOnAction(e -> {
            SessionManager.getInstance().removeFromCart(item);
            loadCartItems();
            updateTotal();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        box.getChildren().addAll(nameLabel, priceLabel, quantitySpinner, subtotalLabel, spacer, removeButton);

        return box;
    }

    private void updateTotal() {
        double total = SessionManager.getInstance().getCartTotal();
        totalLabel.setText(String.format("Итого: %.2f ₽", total));
    }

    @FXML
    private void handlePlaceOrder() {
        if (SessionManager.getInstance().getCart().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Корзина пуста", "Добавьте блюда перед оформлением заказа");
            return;
        }

        String address = addressField.getText().trim();
        if (address.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Адрес не указан", "Пожалуйста, укажите адрес доставки");
            return;
        }

        String paymentMethod = convertPaymentMethod(paymentMethodCombo.getValue());

        // Создание заказа
        Order order = new Order();
        order.setUserId(SessionManager.getInstance().getCurrentUser().getUserId());
        order.setDeliveryAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(SessionManager.getInstance().getCartTotal());
        order.setOrderItems(SessionManager.getInstance().getCart());

        if (OrderDAO.createOrder(order)) {
            SessionManager.getInstance().clearCart();
            showSuccessMessage();
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось оформить заказ. Попробуйте снова.");
        }
    }

    private String convertPaymentMethod(String method) {
        switch (method) {
            case "Наличными": return "cash";
            case "Картой": return "card";
            case "Онлайн оплата": return "online";
            default: return "cash";
        }
    }

    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Заказ оформлен");
        alert.setHeaderText(null);
        alert.setContentText("Спасибо за заказ! Мы с вами скоро свяжемся.");
        alert.showAndWait();
        handleBack();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

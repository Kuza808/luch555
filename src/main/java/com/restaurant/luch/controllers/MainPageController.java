package com.restaurant.luch.controllers;

import com.restaurant.luch.database.CategoryDAO;
import com.restaurant.luch.database.DishDAO;
import com.restaurant.luch.database.FavoriteDAO;
import com.restaurant.luch.model.Category;
import com.restaurant.luch.model.Dish;
import com.restaurant.luch.model.CartItem;
import com.restaurant.luch.utils.SessionManager;
import com.restaurant.luch.utils.NotificationUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;

public class MainPageController {

    @FXML private HBox categoryBar;
    @FXML private FlowPane dishesContainer;
    @FXML private Label userNameLabel;
    @FXML private Button cartButton;
    @FXML private Button bookingButton;
    @FXML private Button profileButton;
    @FXML private Label cartCountLabel;

    private int selectedCategoryId = 0;

    @FXML
    private void initialize() {
        if (SessionManager.getInstance().isLoggedIn()) {
            userNameLabel.setText(SessionManager.getInstance().getCurrentUser().getFullName());
        }

        loadCategories();
        loadAllDishes();
        updateCartCount();
    }

    private void loadCategories() {
        categoryBar.getChildren().clear();

        Button allButton = createCategoryButton("Все", 0);
        allButton.getStyleClass().add("category-button-selected");
        categoryBar.getChildren().add(allButton);

        ObservableList<Category> categories = CategoryDAO.getAllCategories();
        for (Category category : categories) {
            Button categoryButton = createCategoryButton(category.getCategoryName(), category.getCategoryId());
            categoryBar.getChildren().add(categoryButton);
        }
    }

    private Button createCategoryButton(String name, int categoryId) {
        Button button = new Button(name);
        button.getStyleClass().add("category-button");
        button.setOnAction(e -> {
            selectedCategoryId = categoryId;
            updateCategorySelection(button);
            if (categoryId == 0) {
                loadAllDishes();
            } else {
                loadDishesByCategory(categoryId);
            }
        });
        return button;
    }

    private void updateCategorySelection(Button selectedButton) {
        for (javafx.scene.Node node : categoryBar.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.getStyleClass().remove("category-button-selected");
            }
        }
        selectedButton.getStyleClass().add("category-button-selected");
    }

    private void loadAllDishes() {
        dishesContainer.getChildren().clear();
        ObservableList<Dish> dishes = DishDAO.getAllDishes();
        displayDishes(dishes);
    }

    private void loadDishesByCategory(int categoryId) {
        dishesContainer.getChildren().clear();
        ObservableList<Dish> dishes = DishDAO.getDishesByCategory(categoryId);
        displayDishes(dishes);
    }

    private void displayDishes(ObservableList<Dish> dishes) {
        int userId = SessionManager.getInstance().getCurrentUser().getUserId();

        for (Dish dish : dishes) {
            VBox dishCard = createDishCard(dish, userId);
            dishesContainer.getChildren().add(dishCard);
        }
    }

    private VBox createDishCard(Dish dish, int userId) {
        VBox card = new VBox(10);
        card.getStyleClass().add("dish-card");
        card.setPrefWidth(250);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));

        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(false);

        if (dish.getImagePath() != null && !dish.getImagePath().isEmpty()) {
            File imageFile = new File(dish.getImagePath());
            if (imageFile.exists()) {
                imageView.setImage(new Image(imageFile.toURI().toString()));
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default-dish.png")));
            }
        } else {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default-dish.png")));
        }

        Label nameLabel = new Label(dish.getDishName());
        nameLabel.getStyleClass().add("dish-name");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(220);

        Label descLabel = new Label(dish.getDescription());
        descLabel.getStyleClass().add("dish-description");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(220);

        Label priceLabel = new Label(String.format("%.2f ₽", dish.getPrice()));
        priceLabel.getStyleClass().add("dish-price");

        Button favoriteButton = new Button();
        favoriteButton.getStyleClass().add("favorite-button");
        boolean isFav = FavoriteDAO.isFavorite(userId, dish.getDishId());
        favoriteButton.setText(isFav ? "❤" : "♡");
        favoriteButton.setOnAction(e -> toggleFavorite(favoriteButton, userId, dish.getDishId()));

        Button addToCartButton = new Button("В корзину");
        addToCartButton.getStyleClass().add("add-to-cart-button");
        addToCartButton.setOnAction(e -> addToCart(dish));

        HBox topBar = new HBox(favoriteButton);
        topBar.setAlignment(Pos.TOP_RIGHT);

        card.getChildren().addAll(topBar, imageView, nameLabel, descLabel, priceLabel, addToCartButton);

        return card;
    }

    private void toggleFavorite(Button button, int userId, int dishId) {
        if (button.getText().equals("♡")) {
            if (FavoriteDAO.addToFavorites(userId, dishId)) {
                button.setText("❤");
            }
        } else {
            if (FavoriteDAO.removeFromFavorites(userId, dishId)) {
                button.setText("♡");
            }
        }
    }

    private void addToCart(Dish dish) {
        CartItem item = new CartItem(dish, 1);
        SessionManager.getInstance().addToCart(item);
        updateCartCount();
        NotificationUtils.showSuccess("Добавлено", dish.getDishName() + " добавлено в корзину!");
    }

    private void updateCartCount() {
        int count = SessionManager.getInstance().getCart().size();
        cartCountLabel.setText(String.valueOf(count));
    }

    @FXML
    private void handleCart() {
        loadScene("Cart.fxml", "Корзина");
    }

    @FXML
    private void handleBooking() {
        loadScene("Booking.fxml", "Бронирование");
    }

    @FXML
    private void handleProfile() {
        loadScene("Profile.fxml", "Профиль");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/" + fxmlFile));
            Stage stage = (Stage) cartButton.getScene().getWindow();
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

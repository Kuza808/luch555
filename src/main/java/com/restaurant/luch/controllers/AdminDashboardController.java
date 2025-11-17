package com.restaurant.luch.controllers;

import com.restaurant.luch.database.*;
import com.restaurant.luch.model.*;
import com.restaurant.luch.utils.SessionManager;
import com.restaurant.luch.utils.FileUploadManager;
import com.restaurant.luch.utils.NotificationUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;

public class AdminDashboardController {

    @FXML private TabPane tabPane;
    @FXML private TableView<Dish> dishesTable;
    @FXML private TableColumn<Dish, String> dishNameColumn;
    @FXML private TableColumn<Dish, Double> dishPriceColumn;
    @FXML private TableColumn<Dish, String> dishCategoryColumn;

    @FXML private TextField dishNameField;
    @FXML private TextArea dishDescField;
    @FXML private TextField dishPriceField;
    @FXML private ComboBox<Category> categoryCombo;
    @FXML private TextField imagePathField;
    @FXML private Button selectImageButton;
    @FXML private Button addDishButton;
    @FXML private Button updateDishButton;
    @FXML private Button deleteDishButton;

    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingUserColumn;
    @FXML private TableColumn<Booking, String> bookingDateColumn;
    @FXML private TableColumn<Booking, Integer> bookingGuestsColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    @FXML private Button confirmBookingButton;
    @FXML private Button rejectBookingButton;

    @FXML private Button logoutButton;

    @FXML
    private void initialize() {
        loadCategories();
        loadDishes();
        loadBookings();
        setupTables();
    }

    private void loadCategories() {
        ObservableList<Category> categories = CategoryDAO.getAllCategories();
        categoryCombo.setItems(categories);
        if (!categories.isEmpty()) {
            categoryCombo.setValue(categories.get(0));
        }
    }

    private void loadDishes() {
        ObservableList<Dish> dishes = DishDAO.getAllDishes();
        dishesTable.setItems(dishes);
    }

    private void loadBookings() {
        ObservableList<Booking> bookings = BookingDAO.getAllBookings();
        bookingsTable.setItems(bookings);
    }

    private void setupTables() {
        dishNameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDishName()));
        dishPriceColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));
        dishCategoryColumn.setCellValueFactory(data -> {
            Category cat = CategoryDAO.getCategoryById(data.getValue().getCategoryId());
            return new javafx.beans.property.SimpleStringProperty(cat != null ? cat.getCategoryName() : "");
        });

        dishesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateDishFields(newVal);
            }
        });

        bookingUserColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getUserName()));
        bookingDateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getBookingDate().toString()));
        bookingGuestsColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getGuestsCount()));
        bookingStatusColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(translateStatus(data.getValue().getStatus())));
    }

    private void populateDishFields(Dish dish) {
        dishNameField.setText(dish.getDishName());
        dishDescField.setText(dish.getDescription());
        dishPriceField.setText(String.valueOf(dish.getPrice()));
        imagePathField.setText(dish.getImagePath());

        for (Category cat : categoryCombo.getItems()) {
            if (cat.getCategoryId() == dish.getCategoryId()) {
                categoryCombo.setValue(cat);
                break;
            }
        }
    }

    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение блюда");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            try {
                String imageUrl = FileUploadManager.uploadDishImage(selectedFile);
                if (imageUrl != null) {
                    imagePathField.setText(imageUrl);
                    NotificationUtils.showSuccess("Успех", "Изображение загружено!");
                } else {
                    NotificationUtils.showError("Ошибка", "Не удалось загрузить изображение");
                }
            } catch (IOException e) {
                e.printStackTrace();
                NotificationUtils.showError("Ошибка", "Ошибка при загрузке: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAddDish() {
        if (!validateDishFields()) return;

        Dish dish = new Dish();
        dish.setDishName(dishNameField.getText().trim());
        dish.setDescription(dishDescField.getText().trim());
        dish.setPrice(Double.parseDouble(dishPriceField.getText().trim()));
        dish.setCategoryId(categoryCombo.getValue().getCategoryId());
        dish.setImagePath(imagePathField.getText().trim());
        dish.setAvailable(true);

        if (DishDAO.addDish(dish)) {
            NotificationUtils.showSuccess("Успех", "Блюдо добавлено");
            clearDishFields();
            loadDishes();
        } else {
            NotificationUtils.showError("Ошибка", "Не удалось добавить блюдо");
        }
    }

    @FXML
    private void handleUpdateDish() {
        Dish selectedDish = dishesTable.getSelectionModel().getSelectedItem();
        if (selectedDish == null) {
            NotificationUtils.showWarning("Предупреждение", "Выберите блюдо для обновления");
            return;
        }

        if (!validateDishFields()) return;

        selectedDish.setDishName(dishNameField.getText().trim());
        selectedDish.setDescription(dishDescField.getText().trim());
        selectedDish.setPrice(Double.parseDouble(dishPriceField.getText().trim()));
        selectedDish.setCategoryId(categoryCombo.getValue().getCategoryId());
        selectedDish.setImagePath(imagePathField.getText().trim());

        if (DishDAO.updateDish(selectedDish)) {
            NotificationUtils.showSuccess("Успех", "Блюдо обновлено");
            clearDishFields();
            loadDishes();
        } else {
            NotificationUtils.showError("Ошибка", "Не удалось обновить блюдо");
        }
    }

    @FXML
    private void handleDeleteDish() {
        Dish selectedDish = dishesTable.getSelectionModel().getSelectedItem();
        if (selectedDish == null) {
            NotificationUtils.showWarning("Предупреждение", "Выберите блюдо для удаления");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText(null);
        confirm.setContentText("Вы уверены, что хотите удалить это блюдо?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (DishDAO.deleteDish(selectedDish.getDishId())) {
                NotificationUtils.showSuccess("Успех", "Блюдо удалено");
                clearDishFields();
                loadDishes();
            } else {
                NotificationUtils.showError("Ошибка", "Не удалось удалить блюдо");
            }
        }
    }

    @FXML
    private void handleConfirmBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            NotificationUtils.showWarning("Предупреждение", "Выберите бронирование");
            return;
        }

        int adminId = SessionManager.getInstance().getCurrentUser().getUserId();
        if (BookingDAO.confirmBooking(selectedBooking.getBookingId(), adminId)) {
            NotificationUtils.showSuccess("Успех", "Бронирование подтверждено");
            loadBookings();
        } else {
            NotificationUtils.showError("Ошибка", "Не удалось подтвердить бронирование");
        }
    }

    @FXML
    private void handleRejectBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            NotificationUtils.showWarning("Предупреждение", "Выберите бронирование");
            return;
        }

        int adminId = SessionManager.getInstance().getCurrentUser().getUserId();
        if (BookingDAO.rejectBooking(selectedBooking.getBookingId(), adminId)) {
            NotificationUtils.showSuccess("Успех", "Бронирование отклонено");
            loadBookings();
        } else {
            NotificationUtils.showError("Ошибка", "Не удалось отклонить бронирование");
        }
    }

    private boolean validateDishFields() {
        if (dishNameField.getText().trim().isEmpty() ||
                dishPriceField.getText().trim().isEmpty() ||
                categoryCombo.getValue() == null) {
            NotificationUtils.showWarning("Предупреждение", "Заполните все обязательные поля");
            return false;
        }

        try {
            Double.parseDouble(dishPriceField.getText().trim());
        } catch (NumberFormatException e) {
            NotificationUtils.showWarning("Предупреждение", "Введите корректную цену");
            return false;
        }

        return true;
    }

    private void clearDishFields() {
        dishNameField.clear();
        dishDescField.clear();
        dishPriceField.clear();
        imagePathField.clear();
        dishesTable.getSelectionModel().clearSelection();
    }

    private String translateStatus(String status) {
        switch (status) {
            case "pending": return "Ожидает";
            case "confirmed": return "Подтверждено";
            case "rejected": return "Отклонено";
            default: return status;
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        loadScene("Login.fxml", "Вход");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/" + fxmlFile));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
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

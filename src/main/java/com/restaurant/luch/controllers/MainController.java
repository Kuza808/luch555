package com.restaurant.luch.controllers;

import com.restaurant.luch.models.*;
import com.restaurant.luch.service.RestaurantService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Контроллер главного меню ресторана
 */
public class MainController implements Initializable {

    @FXML private TabPane mainTabPane;
    @FXML private Tab menuTab;
    @FXML private Tab orderTab;
    @FXML private Tab bookingTab;
    @FXML private Tab profileTab;

    // Компоненты меню - ИСПРАВЛЕНО: TilePane вместо VBox
    @FXML private TilePane menuContainer;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private TextField searchField;

    // Компоненты заказа
    @FXML private VBox orderItemsContainer;
    @FXML private Label orderTotalLabel;
    @FXML private Label itemsCountLabel;

    // Компоненты бронирования
    @FXML private DatePicker bookingDatePicker;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private ComboBox<Integer> guestsComboBox;
    @FXML private ComboBox<String> tableTypeComboBox;
    @FXML private TextArea specialRequestsArea;
    @FXML private VBox bookingsContainer;

    // Данные
    private ObservableList<Dish> menuItems;
    private Order currentOrder;
    private ObservableList<Booking> userBookings;
    private RestaurantService restaurantService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeData();
        initializeMenu();
        initializeOrder();
        initializeBooking();
    }

    /**
     * Инициализация данных
     */
    private void initializeData() {
        // Инициализация сервиса
        restaurantService = RestaurantService.getInstance();

        // Инициализация меню
        menuItems = restaurantService.getMenuItems();

        // Инициализация заказа
        currentOrder = new Order(1, "DINE_IN");

        // Инициализация бронирований
        userBookings = restaurantService.getBookings();
    }

    /**
     * Инициализация меню
     */
    private void initializeMenu() {
        // Проверяем что menuContainer инициализирован
        if (menuContainer == null) {
            System.err.println("menuContainer не инициализирован!");
            return;
        }

        // Настройка TilePane
        menuContainer.setHgap(20);
        menuContainer.setVgap(20);
        menuContainer.setPrefTileWidth(300);
        menuContainer.setPrefTileHeight(220);
        menuContainer.setStyle("-fx-padding: 10;");

        // Категории для фильтра
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Все", "Закуски", "Основные блюда", "Салаты", "Супы", "Десерты", "Напитки"
        );
        categoryFilter.setItems(categories);
        categoryFilter.setValue("Все");

        // Обработчики фильтров
        categoryFilter.setOnAction(e -> filterMenu());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterMenu());

        // Заполнение меню
        refreshMenu();
    }

    /**
     * Инициализация заказа
     */
    private void initializeOrder() {
        if (orderItemsContainer == null) {
            System.err.println("orderItemsContainer не инициализирован!");
            return;
        }
        updateOrderSummary();
    }

    /**
     * Инициализация бронирования
     */
    private void initializeBooking() {
        // Проверяем инициализацию компонентов
        if (bookingDatePicker == null || timeComboBox == null) {
            System.err.println("Компоненты бронирования не инициализированы!");
            return;
        }

        // Настройка DatePicker
        bookingDatePicker.setValue(LocalDate.now());
        bookingDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Временные слоты
        ObservableList<String> times = FXCollections.observableArrayList(
                "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"
        );
        timeComboBox.setItems(times);
        timeComboBox.setValue("19:00");

        // Количество гостей
        ObservableList<Integer> guests = FXCollections.observableArrayList();
        for (int i = 1; i <= 10; i++) guests.add(i);
        guestsComboBox.setItems(guests);
        guestsComboBox.setValue(2);

        // Типы столов
        ObservableList<String> tableTypes = FXCollections.observableArrayList(
                "Стандарт (2 персоны)", "Угловой (4 персоны)", "Банкетный (6 персон)", "VIP (8 персон)"
        );
        tableTypeComboBox.setItems(tableTypes);
        tableTypeComboBox.setValue("Стандарт (2 персоны)");

        refreshBookings();
    }

    /**
     * Обновление отображения меню
     */
    private void refreshMenu() {
        if (menuContainer == null) {
            System.err.println("menuContainer is null в refreshMenu()");
            return;
        }

        menuContainer.getChildren().clear();

        for (Dish dish : menuItems) {
            VBox dishCard = createDishCard(dish);
            menuContainer.getChildren().add(dishCard);
        }
    }

    /**
     * Создание карточки блюда
     */
    private VBox createDishCard(Dish dish) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; " +
                "-fx-border-color: #E8D8C8; -fx-border-width: 1; -fx-padding: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(139, 69, 19, 0.1), 8, 0, 0, 0);");
        card.setSpacing(10);
        card.setPrefWidth(280);
        card.setPrefHeight(200);

        // Заголовок с названием и ценой
        HBox headerBox = new HBox();
        headerBox.setSpacing(10);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label nameLabel = new Label(dish.getName());
        nameLabel.setStyle("-fx-text-fill: #8B4513; -fx-font-size: 16px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);

        Label priceLabel = new Label(dish.getFormattedPrice());
        priceLabel.setStyle("-fx-text-fill: #A0522D; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        headerBox.getChildren().addAll(nameLabel, priceLabel);

        // Описание
        Label descLabel = new Label(dish.getDescription());
        descLabel.setStyle("-fx-text-fill: #654321; -fx-font-size: 12px;");
        descLabel.setWrapText(true);
        descLabel.setPrefHeight(40);

        // Информация о блюде
        HBox infoBox = new HBox();
        infoBox.setSpacing(15);
        infoBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label timeLabel = new Label("⏱️ " + dish.getCookingTimeDisplay());
        timeLabel.setStyle("-fx-text-fill: #A0522D; -fx-font-size: 11px;");

        Label caloriesLabel = new Label("🔥 " + dish.getCaloriesDisplay());
        caloriesLabel.setStyle("-fx-text-fill: #A0522D; -fx-font-size: 11px;");

        infoBox.getChildren().addAll(timeLabel, caloriesLabel);

        // Кнопка добавления в заказ
        Button addButton = new Button("Добавить в заказ");
        addButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8 16; -fx-cursor: hand;");
        addButton.setOnAction(e -> addToOrder(dish));

        // Обработчики hover для кнопки
        addButton.setOnMouseEntered(e ->
                addButton.setStyle("-fx-background-color: #A0522D; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8 16; -fx-cursor: hand;")
        );
        addButton.setOnMouseExited(e ->
                addButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8 16; -fx-cursor: hand;")
        );

        card.getChildren().addAll(headerBox, descLabel, infoBox, addButton);
        return card;
    }

    /**
     * Фильтрация меню
     */
    private void filterMenu() {
        if (menuContainer == null) return;

        String category = categoryFilter.getValue();
        String searchText = searchField.getText().toLowerCase();

        menuContainer.getChildren().clear();

        for (Dish dish : menuItems) {
            boolean categoryMatch = "Все".equals(category) || dish.getCategory().equals(category);
            boolean searchMatch = dish.getName().toLowerCase().contains(searchText) ||
                    dish.getDescription().toLowerCase().contains(searchText);

            if (categoryMatch && searchMatch) {
                VBox dishCard = createDishCard(dish);
                menuContainer.getChildren().add(dishCard);
            }
        }
    }

    /**
     * Добавление блюда в заказ
     */
    private void addToOrder(Dish dish) {
        // Проверяем, есть ли уже такое блюдо в заказе
        Optional<OrderItem> existingItem = currentOrder.getItems().stream()
                .filter(item -> item.getDish().getId() == dish.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            // Увеличиваем количество
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
        } else {
            // Добавляем новое блюдо
            OrderItem newItem = new OrderItem(dish, 1);
            currentOrder.addItem(newItem);
        }

        updateOrderSummary();
        showAlert(Alert.AlertType.INFORMATION, "Успешно", "Блюдо добавлено в заказ!");
    }

    /**
     * Обновление summary заказа
     */
    private void updateOrderSummary() {
        if (orderItemsContainer == null) return;

        orderItemsContainer.getChildren().clear();

        for (OrderItem item : currentOrder.getItems()) {
            HBox itemBox = createOrderItemBox(item);
            orderItemsContainer.getChildren().add(itemBox);
        }

        if (orderTotalLabel != null) {
            orderTotalLabel.setText(currentOrder.getFormattedTotal());
        }
        if (itemsCountLabel != null) {
            itemsCountLabel.setText(currentOrder.getTotalItems() + " шт.");
        }
    }

    /**
     * Создание элемента заказа
     */
    private HBox createOrderItemBox(OrderItem item) {
        HBox itemBox = new HBox();
        itemBox.setSpacing(10);
        itemBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        itemBox.setStyle("-fx-padding: 10; -fx-background-color: #F9F5F0; -fx-background-radius: 5;");
        itemBox.setPrefWidth(350);

        // Название и количество
        VBox infoBox = new VBox();
        infoBox.setSpacing(2);

        Label nameLabel = new Label(item.getDish().getName());
        nameLabel.setStyle("-fx-text-fill: #8B4513; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label quantityLabel = new Label("Количество: " + item.getQuantity());
        quantityLabel.setStyle("-fx-text-fill: #A0522D; -fx-font-size: 12px;");

        infoBox.getChildren().addAll(nameLabel, quantityLabel);

        // Цена и кнопки
        VBox controlBox = new VBox();
        controlBox.setSpacing(5);
        controlBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        Label priceLabel = new Label(item.getFormattedItemTotal());
        priceLabel.setStyle("-fx-text-fill: #A0522D; -fx-font-weight: bold; -fx-font-size: 14px;");

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);

        Button minusButton = new Button("-");
        minusButton.setStyle("-fx-background-color: #A0522D; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 3; -fx-min-width: 25; -fx-min-height: 25; -fx-cursor: hand;");
        minusButton.setOnAction(e -> updateItemQuantity(item, -1));

        Button plusButton = new Button("+");
        plusButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 3; -fx-min-width: 25; -fx-min-height: 25; -fx-cursor: hand;");
        plusButton.setOnAction(e -> updateItemQuantity(item, 1));

        Button removeButton = new Button("✕");
        removeButton.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 3; -fx-min-width: 25; -fx-min-height: 25; -fx-cursor: hand;");
        removeButton.setOnAction(e -> removeItemFromOrder(item));

        buttonBox.getChildren().addAll(minusButton, plusButton, removeButton);
        controlBox.getChildren().addAll(priceLabel, buttonBox);

        HBox.setHgrow(infoBox, Priority.ALWAYS);
        itemBox.getChildren().addAll(infoBox, controlBox);

        return itemBox;
    }

    /**
     * Изменение количества блюда в заказе
     */
    private void updateItemQuantity(OrderItem item, int change) {
        int newQuantity = item.getQuantity() + change;
        if (newQuantity <= 0) {
            removeItemFromOrder(item);
        } else {
            item.setQuantity(newQuantity);
            updateOrderSummary();
        }
    }

    /**
     * Удаление блюда из заказа
     */
    private void removeItemFromOrder(OrderItem item) {
        currentOrder.removeItem(item);
        updateOrderSummary();
    }

    /**
     * Оформление заказа
     */
    @FXML
    private void handlePlaceOrder() {
        if (currentOrder.getItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Внимание", "Добавьте блюда в заказ!");
            return;
        }

        currentOrder.setStatus("CONFIRMED");
        showAlert(Alert.AlertType.INFORMATION, "Успешно",
                "Заказ оформлен! Номер заказа: #" + (new Random().nextInt(1000) + 1000));

        // Очищаем заказ
        currentOrder.getItems().clear();
        updateOrderSummary();
    }

    /**
     * Создание бронирования
     */
    @FXML
    private void handleCreateBooking() {
        LocalDate date = bookingDatePicker.getValue();
        String time = timeComboBox.getValue();
        Integer guests = guestsComboBox.getValue();
        String tableType = tableTypeComboBox.getValue();
        String requests = specialRequestsArea.getText();

        if (date == null || time == null || guests == null || tableType == null) {
            showAlert(Alert.AlertType.WARNING, "Внимание", "Заполните все обязательные поля!");
            return;
        }

        Booking newBooking = new Booking(1, "Иван Иванов", "+7 (999) 123-45-67",
                date, LocalTime.parse(time), guests, tableType);
        newBooking.setSpecialRequests(requests);
        newBooking.setStatus("PENDING");

        userBookings.add(newBooking);
        refreshBookings();

        showAlert(Alert.AlertType.INFORMATION, "Успешно",
                "Бронирование создано! Ожидайте подтверждения.");

        // Очищаем форму
        specialRequestsArea.clear();
    }

    /**
     * Обновление списка бронирований
     */
    private void refreshBookings() {
        if (bookingsContainer == null) return;

        bookingsContainer.getChildren().clear();

        for (Booking booking : userBookings) {
            VBox bookingCard = createBookingCard(booking);
            bookingsContainer.getChildren().add(bookingCard);
        }
    }

    /**
     * Создание карточки бронирования
     */
    private VBox createBookingCard(Booking booking) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-radius: 8; " +
                "-fx-border-color: #E8D8C8; -fx-border-width: 1; -fx-padding: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(139, 69, 19, 0.05), 5, 0, 0, 0);");
        card.setSpacing(8);

        // Заголовок
        HBox headerBox = new HBox();
        headerBox.setSpacing(10);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label dateLabel = new Label(booking.getBookingDate().toString() + " в " + booking.getBookingTime().toString());
        dateLabel.setStyle("-fx-text-fill: #8B4513; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label statusLabel = new Label(booking.getStatusDisplay());
        statusLabel.setStyle("-fx-text-fill: #A0522D; -fx-font-size: 12px; -fx-font-weight: bold;");

        HBox.setHgrow(dateLabel, Priority.ALWAYS);
        headerBox.getChildren().addAll(dateLabel, statusLabel);

        // Детали
        Label detailsLabel = new Label(String.format("Гостей: %d | Стол: %s",
                booking.getGuestsCount(), booking.getTableType()));
        detailsLabel.setStyle("-fx-text-fill: #654321; -fx-font-size: 12px;");

        // Особые пожелания
        if (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty()) {
            Label requestsLabel = new Label("Пожелания: " + booking.getSpecialRequests());
            requestsLabel.setStyle("-fx-text-fill: #A0522D; -fx-font-size: 11px; -fx-font-style: italic;");
            requestsLabel.setWrapText(true);
            card.getChildren().addAll(headerBox, detailsLabel, requestsLabel);
        } else {
            card.getChildren().addAll(headerBox, detailsLabel);
        }

        return card;
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

    // Обработчики событий мыши для кнопок
    @FXML
    public void handleMouseEnter(javafx.scene.input.MouseEvent event) {
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            if (!button.isDisable()) {
                button.setStyle(button.getStyle().replace("-fx-background-color: #8B4513;", "-fx-background-color: #A0522D;"));
            }
        }
    }

    @FXML
    public void handleMouseExit(javafx.scene.input.MouseEvent event) {
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            if (!button.isDisable()) {
                button.setStyle(button.getStyle().replace("-fx-background-color: #A0522D;", "-fx-background-color: #8B4513;"));
            }
        }
    }
}
package com.restaurant.luch.service;


import com.restaurant.luch.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * Сервис для работы с данными ресторана
 */
public class RestaurantService {

    private static RestaurantService instance;
    private ObservableList<Dish> menuItems;
    private ObservableList<Booking> bookings;
    private ObservableList<Order> orders;

    private RestaurantService() {
        initializeData();
    }

    public static RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    /**
     * Инициализация тестовых данных
     */
    private void initializeData() {
        initializeMenu();
        initializeBookings();
        initializeOrders();
    }

    /**
     * Инициализация меню
     */
    private void initializeMenu() {
        menuItems = FXCollections.observableArrayList();

        // Закуски
        Dish bruschetta = createDish(1, "Брускетта с томатами",
                "Итальянские гренки с свежими томатами и базиликом", 450, "Закуски", 280);
        bruschetta.setIngredients(Arrays.asList("Хлеб чиабатта", "Помидоры", "Базилик", "Чеснок", "Оливковое масло"));
        menuItems.add(bruschetta);

        Dish tartar = createDish(2, "Татар из говядины",
                "Нежный татар из мраморной говядины с каперсами", 890, "Закуски", 320);
        tartar.setIngredients(Arrays.asList("Говядина", "Каперсы", "Лук красный", "Яичный желток", "Соус Ворчестер"));
        menuItems.add(tartar);

        // Основные блюда
        Dish steak = createDish(3, "Стейк Рибай",
                "Мраморный стейк с розмарином и овощами гриль", 1850, "Основные блюда", 650);
        steak.setCookingTime(25);
        steak.setIngredients(Arrays.asList("Говядина Рибай", "Розмарин", "Чеснок", "Овощи сезонные", "Соус песто"));
        menuItems.add(steak);

        Dish salmon = createDish(4, "Лосось в цитрусовом соусе",
                "Филе лосося с апельсиновым соусом и булгуром", 1250, "Основные блюда", 480);
        salmon.setCookingTime(20);
        salmon.setIngredients(Arrays.asList("Лосось", "Апельсин", "Лимон", "Булгур", "Зелень"));
        menuItems.add(salmon);

        // Салаты
        Dish caesar = createDish(5, "Цезарь с креветками",
                "Классический салат Цезарь с тигровыми креветками", 950, "Салаты", 380);
        caesar.setIngredients(Arrays.asList("Салат романо", "Креветки тигровые", "Пармезан", "Сухарики", "Соус Цезарь"));
        menuItems.add(caesar);

        // Супы
        Dish mushroomSoup = createDish(6, "Крем-суп из белых грибов",
                "Ароматный крем-суп с трюфельным маслом", 650, "Супы", 290);
        mushroomSoup.setIngredients(Arrays.asList("Белые грибы", "Сливки", "Лук", "Чеснок", "Трюфельное масло"));
        menuItems.add(mushroomSoup);

        // Десерты
        Dish tiramisu = createDish(7, "Тирамису классический",
                "Итальянский десерт с маскарпоне и кофе", 550, "Десерты", 420);
        tiramisu.setIngredients(Arrays.asList("Маскарпоне", "Печенье савоярди", "Кофе эспрессо", "Какао", "Яйца"));
        menuItems.add(tiramisu);

        // Напитки
        Dish lemonade = createDish(8, "Домашний лимонад",
                "Освежающий лимонад с мятой и имбирем", 350, "Напитки", 120);
        lemonade.setIngredients(Arrays.asList("Лимоны", "Мята", "Имбирь", "Мед", "Содовая"));
        menuItems.add(lemonade);
    }

    /**
     * Создание блюда
     */
    private Dish createDish(int id, String name, String description, double price, String category, double calories) {
        Dish dish = new Dish(id, name, description, price, category);
        dish.setCalories(calories);
        return dish;
    }

    /**
     * Инициализация бронирований
     */
    private void initializeBookings() {
        bookings = FXCollections.observableArrayList();

        Booking booking1 = new Booking(1, "Иван Иванов", "+7 (999) 123-45-67",
                LocalDate.now().plusDays(1), LocalTime.of(19, 0), 4, "Угловой (4 персоны)");
        booking1.setStatus("CONFIRMED");
        bookings.add(booking1);

        Booking booking2 = new Booking(1, "Иван Иванов", "+7 (999) 123-45-67",
                LocalDate.now().plusDays(3), LocalTime.of(20, 0), 2, "Стандарт (2 персоны)");
        booking2.setStatus("PENDING");
        bookings.add(booking2);
    }

    /**
     * Инициализация заказов
     */
    private void initializeOrders() {
        orders = FXCollections.observableArrayList();
    }

    // === ГЕТТЕРЫ ===

    public ObservableList<Dish> getMenuItems() {
        return menuItems;
    }

    public ObservableList<Booking> getBookings() {
        return bookings;
    }

    public ObservableList<Order> getOrders() {
        return orders;
    }

    /**
     * Получить блюда по категории
     */
    public ObservableList<Dish> getDishesByCategory(String category) {
        if ("Все".equals(category)) {
            return menuItems;
        }
        return menuItems.filtered(dish -> dish.getCategory().equals(category));
    }

    /**
     * Поиск блюд
     */
    public ObservableList<Dish> searchDishes(String query) {
        if (query == null || query.trim().isEmpty()) {
            return menuItems;
        }

        String lowerQuery = query.toLowerCase();
        return menuItems.filtered(dish ->
                dish.getName().toLowerCase().contains(lowerQuery) ||
                        dish.getDescription().toLowerCase().contains(lowerQuery) ||
                        (dish.getIngredients() != null && dish.getIngredients().stream()
                                .anyMatch(ingredient -> ingredient.toLowerCase().contains(lowerQuery)))
        );
    }

    /**
     * Добавить бронирование
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Добавить заказ
     */
    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Получить категории меню
     */
    public List<String> getMenuCategories() {
        return Arrays.asList("Все", "Закуски", "Основные блюда", "Салаты", "Супы", "Десерты", "Напитки");
    }

    /**
     * Получить доступные времена для бронирования
     */
    public List<String> getAvailableTimes() {
        return Arrays.asList("12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00");
    }

    /**
     * Получить типы столов
     */
    public List<String> getTableTypes() {
        return Arrays.asList("Стандарт (2 персоны)", "Угловой (4 персоны)", "Банкетный (6 персон)", "VIP (8 персон)");
    }
}
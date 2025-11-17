package database;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Dish;
import java.io.IOException;

public class DishDAO {

    // Получить все блюда
    public static ObservableList<Dish> getAllDishes() {
        ObservableList<Dish> dishes = FXCollections.observableArrayList();
        try {
            String response = SupabaseClient.get("dishes?is_available=eq.true&select=*");
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            for (JsonElement element : array) {
                Dish dish = parseJsonToDish(element.getAsJsonObject());
                dishes.add(dish);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    // Получить блюда по категории
    public static ObservableList<Dish> getDishesByCategory(int categoryId) {
        ObservableList<Dish> dishes = FXCollections.observableArrayList();
        try {
            String endpoint = "dishes?category_id=eq." + categoryId + "&is_available=eq.true&select=*";
            String response = SupabaseClient.get(endpoint);
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            for (JsonElement element : array) {
                Dish dish = parseJsonToDish(element.getAsJsonObject());
                dishes.add(dish);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    // Добавить новое блюдо
    public static boolean addDish(Dish dish) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("dish_name", dish.getDishName());
            json.addProperty("description", dish.getDescription());
            json.addProperty("price", dish.getPrice());
            json.addProperty("category_id", dish.getCategoryId());
            json.addProperty("image_path", dish.getImagePath());
            json.addProperty("is_available", dish.isAvailable());

            String response = SupabaseClient.post("dishes", json.toString());
            return !response.isEmpty() && !response.equals("[]");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Обновить блюдо
    public static boolean updateDish(Dish dish) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("dish_name", dish.getDishName());
            json.addProperty("description", dish.getDescription());
            json.addProperty("price", dish.getPrice());
            json.addProperty("category_id", dish.getCategoryId());
            json.addProperty("image_path", dish.getImagePath());
            json.addProperty("is_available", dish.isAvailable());

            String endpoint = "dishes?dish_id=eq." + dish.getDishId();
            String response = SupabaseClient.patch(endpoint, json.toString());
            return !response.isEmpty();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Удалить блюдо
    public static boolean deleteDish(int dishId) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("is_available", false);

            String endpoint = "dishes?dish_id=eq." + dishId;
            String response = SupabaseClient.patch(endpoint, json.toString());
            return !response.isEmpty();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Вспомогательный метод
    private static Dish parseJsonToDish(JsonObject obj) {
        Dish dish = new Dish();
        dish.setDishId(obj.get("dish_id").getAsInt());
        dish.setDishName(obj.get("dish_name").getAsString());
        dish.setDescription(obj.get("description").getAsString());
        dish.setPrice(obj.get("price").getAsDouble());
        dish.setCategoryId(obj.get("category_id").getAsInt());
        dish.setImagePath(obj.has("image_path") ? obj.get("image_path").getAsString() : null);
        dish.setAvailable(obj.get("is_available").getAsBoolean());
        return dish;
    }
}

package database;

import com.google.gson.*;
import models.Dish;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

public class FavoriteDAO {

    // Добавить блюдо в избранное
    public static boolean addToFavorites(int userId, int dishId) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("user_id", userId);
            json.addProperty("dish_id", dishId);

            String response = SupabaseClient.post("favorites", json.toString());
            return !response.isEmpty();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Удалить блюдо из избранного
    public static boolean removeFromFavorites(int userId, int dishId) {
        try {
            String endpoint = "favorites?user_id=eq." + userId + "&dish_id=eq." + dishId;
            return SupabaseClient.delete(endpoint);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Проверить, находится ли блюдо в избранном
    public static boolean isFavorite(int userId, int dishId) {
        try {
            String endpoint = "favorites?user_id=eq." + userId + "&dish_id=eq." + dishId + "&select=*";
            String response = SupabaseClient.get(endpoint);
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();
            return array.size() > 0;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Получить избранные блюда пользователя
    public static ObservableList<Dish> getFavoriteDishes(int userId) {
        ObservableList<Dish> favorites = FXCollections.observableArrayList();
        try {
            String endpoint = "favorites?user_id=eq." + userId + "&select=dishes(*)";
            String response = SupabaseClient.get(endpoint);
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            for (JsonElement element : array) {
                JsonObject favObj = element.getAsJsonObject();
                if (favObj.has("dishes")) {
                    JsonObject dishObj = favObj.getAsJsonObject("dishes");
                    Dish dish = new Dish();
                    dish.setDishId(dishObj.get("dish_id").getAsInt());
                    dish.setDishName(dishObj.get("dish_name").getAsString());
                    dish.setDescription(dishObj.get("description").getAsString());
                    dish.setPrice(dishObj.get("price").getAsDouble());
                    dish.setFavorite(true);
                    favorites.add(dish);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return favorites;
    }
}

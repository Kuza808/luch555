package database;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Category;
import java.io.IOException;

public class CategoryDAO {

    public static ObservableList<Category> getAllCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        try {
            String response = SupabaseClient.get("categories?select=*&order=display_order");
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                Category category = new Category();
                category.setCategoryId(obj.get("category_id").getAsInt());
                category.setCategoryName(obj.get("category_name").getAsString());
                category.setDisplayOrder(obj.get("display_order").getAsInt());
                categories.add(category);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static Category getCategoryById(int categoryId) {
        try {
            String endpoint = "categories?category_id=eq." + categoryId + "&select=*";
            String response = SupabaseClient.get(endpoint);
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            if (array.size() > 0) {
                JsonObject obj = array.get(0).getAsJsonObject();
                Category category = new Category();
                category.setCategoryId(obj.get("category_id").getAsInt());
                category.setCategoryName(obj.get("category_name").getAsString());
                category.setDisplayOrder(obj.get("display_order").getAsInt());
                return category;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

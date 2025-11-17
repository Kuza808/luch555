package database;

import com.google.gson.*;
import models.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Регистрация нового пользователя
    public static boolean registerUser(User user) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("phone_number", user.getPhoneNumber());
            json.addProperty("full_name", user.getFullName());
            json.addProperty("username", user.getUsername());
            json.addProperty("password", user.getPassword());
            json.addProperty("email", user.getEmail());
            json.addProperty("is_admin", false);

            String response = SupabaseClient.post("users", json.toString());
            return !response.isEmpty() && !response.equals("[]");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Вход пользователя
    public static User loginUser(String username, String password) {
        try {
            String endpoint = "users?username=eq." + username + "&password=eq." + password + "&select=*";
            String response = SupabaseClient.get(endpoint);

            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            if (array.size() > 0) {
                JsonObject userObj = array.get(0).getAsJsonObject();
                User user = new User();
                user.setUserId(userObj.get("user_id").getAsInt());
                user.setPhoneNumber(userObj.get("phone_number").getAsString());
                user.setFullName(userObj.get("full_name").getAsString());
                user.setUsername(userObj.get("username").getAsString());
                user.setEmail(userObj.has("email") ? userObj.get("email").getAsString() : null);
                user.setAdmin(userObj.get("is_admin").getAsBoolean());
                return user;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Проверка существования username
    public static boolean usernameExists(String username) {
        try {
            String endpoint = "users?username=eq." + username + "&select=username";
            String response = SupabaseClient.get(endpoint);

            JsonArray array = JsonParser.parseString(response).getAsJsonArray();
            return array.size() > 0;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Проверка существования номера телефона
    public static boolean phoneExists(String phoneNumber) {
        try {
            String endpoint = "users?phone_number=eq." + phoneNumber + "&select=phone_number";
            String response = SupabaseClient.get(endpoint);

            JsonArray array = JsonParser.parseString(response).getAsJsonArray();
            return array.size() > 0;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

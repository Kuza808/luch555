package database;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Booking;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDAO {

    // Создать новое бронирование
    public static boolean createBooking(Booking booking) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("user_id", booking.getUserId());
            json.addProperty("booking_date", booking.getBookingDate().toString());
            json.addProperty("booking_time", booking.getBookingTime().toString());
            json.addProperty("guests_count", booking.getGuestsCount());
            json.addProperty("special_requests", booking.getSpecialRequests());
            json.addProperty("status", "pending");

            String response = SupabaseClient.post("bookings", json.toString());
            return !response.isEmpty();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Получить все бронирования (для администратора)
    public static ObservableList<Booking> getAllBookings() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        try {
            String endpoint = "bookings?select=*,users(full_name)&order=booking_date.desc,booking_time.desc";
            String response = SupabaseClient.get(endpoint);
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            for (JsonElement element : array) {
                Booking booking = parseJsonToBooking(element.getAsJsonObject());
                bookings.add(booking);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Получить бронирования пользователя
    public static ObservableList<Booking> getUserBookings(int userId) {
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        try {
            String endpoint = "bookings?user_id=eq." + userId + "&select=*&order=booking_date.desc,booking_time.desc";
            String response = SupabaseClient.get(endpoint);
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            for (JsonElement element : array) {
                Booking booking = parseJsonToBooking(element.getAsJsonObject());
                bookings.add(booking);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Подтвердить бронирование
    public static boolean confirmBooking(int bookingId, int adminId) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("status", "confirmed");
            json.addProperty("confirmed_by", adminId);

            String endpoint = "bookings?booking_id=eq." + bookingId;
            String response = SupabaseClient.patch(endpoint, json.toString());
            return !response.isEmpty();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Отклонить бронирование
    public static boolean rejectBooking(int bookingId, int adminId) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("status", "rejected");
            json.addProperty("confirmed_by", adminId);

            String endpoint = "bookings?booking_id=eq." + bookingId;
            String response = SupabaseClient.patch(endpoint, json.toString());
            return !response.isEmpty();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Booking parseJsonToBooking(JsonObject obj) {
        Booking booking = new Booking();
        booking.setBookingId(obj.get("booking_id").getAsInt());
        booking.setUserId(obj.get("user_id").getAsInt());
        booking.setBookingDate(LocalDate.parse(obj.get("booking_date").getAsString()));
        booking.setBookingTime(LocalTime.parse(obj.get("booking_time").getAsString()));
        booking.setGuestsCount(obj.get("guests_count").getAsInt());
        booking.setSpecialRequests(obj.has("special_requests") ? obj.get("special_requests").getAsString() : null);
        booking.setStatus(obj.get("status").getAsString());

        if (obj.has("users")) {
            JsonObject user = obj.getAsJsonObject("users");
            booking.setUserName(user.get("full_name").getAsString());
        }

        return booking;
    }
}

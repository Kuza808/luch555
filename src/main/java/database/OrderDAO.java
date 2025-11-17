package database;

import com.google.gson.*;
import models.CartItem;
import models.Order;
import java.io.IOException;

public class OrderDAO {

    public static boolean createOrder(Order order) {
        try {
            // Сначала создаем заказ
            JsonObject orderJson = new JsonObject();
            orderJson.addProperty("user_id", order.getUserId());
            orderJson.addProperty("delivery_address", order.getDeliveryAddress());
            orderJson.addProperty("payment_method", order.getPaymentMethod());
            orderJson.addProperty("total_amount", order.getTotalAmount());
            orderJson.addProperty("order_status", "pending");

            String orderResponse = SupabaseClient.post("orders", orderJson.toString());
            JsonArray orderArray = JsonParser.parseString(orderResponse).getAsJsonArray();

            if (orderArray.size() == 0) {
                return false;
            }

            int orderId = orderArray.get(0).getAsJsonObject().get("order_id").getAsInt();

            // Затем добавляем элементы заказа
            for (CartItem item : order.getOrderItems()) {
                JsonObject itemJson = new JsonObject();
                itemJson.addProperty("order_id", orderId);
                itemJson.addProperty("dish_id", item.getDish().getDishId());
                itemJson.addProperty("quantity", item.getQuantity());
                itemJson.addProperty("price", item.getDish().getPrice());

                SupabaseClient.post("order_items", itemJson.toString());
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

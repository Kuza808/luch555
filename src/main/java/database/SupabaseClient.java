package database;

import config.SupabaseConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import java.io.IOException;

public class SupabaseClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = SupabaseConfig.SUPABASE_URL + "/rest/v1";
    private static final String HEADERS_CONTENT_TYPE = "application/json";
    private static final String HEADERS_AUTHORIZATION = "Bearer " + SupabaseConfig.SUPABASE_ANON_KEY;

    // GET запрос
    public static String get(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + endpoint)
                .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Authorization", HEADERS_AUTHORIZATION)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "[]";
        }
    }

    // POST запрос
    public static String post(String endpoint, String json) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse(HEADERS_CONTENT_TYPE));
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + endpoint)
                .post(body)
                .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Authorization", HEADERS_AUTHORIZATION)
                .addHeader("Content-Type", HEADERS_CONTENT_TYPE)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "{}";
        }
    }

    // PATCH запрос (обновление)
    public static String patch(String endpoint, String json) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse(HEADERS_CONTENT_TYPE));
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + endpoint)
                .patch(body)
                .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Authorization", HEADERS_AUTHORIZATION)
                .addHeader("Content-Type", HEADERS_CONTENT_TYPE)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "{}";
        }
    }

    // DELETE запрос
    public static boolean delete(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + endpoint)
                .delete()
                .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Authorization", HEADERS_AUTHORIZATION)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
}

package com.restaurant.luch.database;

import com.restaurant.luch.config.SupabaseConfig;
import okhttp3.*;
import java.io.IOException;

public class SupabaseClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = SupabaseConfig.SUPABASE_URL + "/rest/v1";

    public static String get(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + endpoint)
                .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.SUPABASE_ANON_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "[]";
        }
    }

    public static String post(String endpoint, String json) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + endpoint)
                .post(body)
                .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "{}";
        }
    }

    public static String patch(String endpoint, String json) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + endpoint)
                .patch(body)
                .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "{}";
        }
    }

    public static boolean delete(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + endpoint)
                .delete()
                .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.SUPABASE_ANON_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
}

package utils;

import config.SupabaseConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUploadManager {

    public static String uploadDishImage(File imageFile) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getName();

        // URL для загрузки
        String uploadUrl = SupabaseConfig.SUPABASE_URL + "/storage/v1/object/dish-images/" + fileName;

        // Загрузить файл
        FileInputStream fis = new FileInputStream(imageFile);
        byte[] fileData = fis.readAllBytes();
        fis.close();

        // Используйте OkHttp для загрузки
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(fileData, null);

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(uploadUrl)
                .header("Authorization", "Bearer " + SupabaseConfig.SUPABASE_ANON_KEY)
                .header("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .post(body)
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return SupabaseConfig.SUPABASE_URL + "/storage/v1/object/public/dish-images/" + fileName;
            }
        }

        return null;
    }
}

package com.midtrans.snapbi;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Map;

public class SnapBiApiRequestor {

    private static final OkHttpClient client;

    static {
        // Create a logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (SnapBiConfig.isEnableLogging()) {
            clientBuilder.addInterceptor(loggingInterceptor);
        }
        client = clientBuilder.build();
    }

    public static JSONObject remoteCall(String url, Map<String, String> headers, Map<String, ?> body) throws IOException {
        
        JSONObject jsonBody = new JSONObject(body);
        String payloadJson = jsonBody.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, payloadJson);

        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            return new JSONObject(responseBody);
        }
    }
}




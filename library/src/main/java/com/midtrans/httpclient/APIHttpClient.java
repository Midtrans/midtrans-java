package com.midtrans.httpclient;

import com.midtrans.Config;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * Retrofit HttpClient configuration
 */
public class APIHttpClient {
    private static final Logger LOGGER = Logger.getLogger(APIHttpClient.class.getName());

    private Config config;

    /**
     * APIHttpClient constructor
     *
     * @param config Midtrans {@link Config configuration}
     */
    public APIHttpClient(Config config) {
        this.config = config;
    }

    /**
     * get client Retrofit configuration
     *
     * @return Retrofit
     */
    public Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(config.getBASE_URL())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(defaultHttpClient)
                .build();
    }

    private OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
            .addInterceptor((Interceptor.Chain chain) -> {
                if (config.getSERVER_KEY().isEmpty() || config.getSERVER_KEY() == null) {
                    if (!config.isProduction()) {
                        LOGGER.info("Server key is empty....");
                    }
                    return chain.proceed(chain.request());
                }

                Request header = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", encodeServerKey())
                        .addHeader("Content-Type", "application/json")
                        .addHeader("User-Agent","Midtrans-Java-Library")
                        .build();
                return chain.proceed(header);
            }).build();

    private String encodeServerKey(){
        return "Basic " + Base64.getEncoder().encodeToString((config.getSERVER_KEY() + ":").getBytes(StandardCharsets.UTF_8));
    }
}



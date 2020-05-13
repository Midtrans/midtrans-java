package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.proxy.ProxyConfig;
import okhttp3.*;
import okhttp3.Authenticator;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Retrofit HttpClient configuration
 */
public class APIHttpClient {
    private static final Logger LOGGER = Logger.getLogger(APIHttpClient.class.getName());

    private Config config;
    private OkHttpClient defaultHttpClient;

    /**
     * APIHttpClient constructor
     *
     * @param config Midtrans {@link Config configuration}
     */
    public APIHttpClient(Config config) {
        this.config = config;
        defaultHttpClient = configHttpClient();
    }

    /**
     * HttpClient configuration
     *
     * @return  OkHttpClient {@link OkHttpClient}
     */
    private OkHttpClient configHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();

        ConnectionPool connectionPool = new ConnectionPool(
                config.getMaxConnectionPool(),
                config.getKeepAliveDuration(),
                TimeUnit.SECONDS);

        if (config.getProxyConfig() == null) {
            return httpClient.newBuilder()
                    .addInterceptor(loggingInterceptor())
                    .connectTimeout(config.getConnectionTimeout(), TimeUnit.SECONDS)
                    .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
                    .connectionPool(connectionPool)
                    .addInterceptor(headers())
                    .build();
        }

        final ProxyConfig proxyConfig = config.getProxyConfig();
        Authenticator proxyAuthenticator = (route, response) -> {
            String credential = Credentials.basic(proxyConfig.getUsername(), proxyConfig.getPassword());
            return response.request().newBuilder().header("Proxy-Authorization", credential).build();
        };

        return httpClient.newBuilder()
                .connectTimeout(config.getConnectionTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
                .connectionPool(connectionPool)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort())))
                .proxyAuthenticator(proxyAuthenticator)
                .addInterceptor(headers())
                .build();
    }

    /**
     * Headers set configuration
     *
     * @return  Interceptor {@link Interceptor}
     */
    private Interceptor headers() {
        return chain -> {
            if (config.getSERVER_KEY().isEmpty() || config.getSERVER_KEY() == null) {
                if (config.isEnabledLog()) {
                    LOGGER.info("Server key is empty....");
                }
                return chain.proceed(chain.request());
            }
            Map<String, String> headersMap = new HashMap<>();
            headersMap.put("Accept", "application/json");
            headersMap.put("Content-Type", "application/json");
            headersMap.put("Authorization", encodeServerKey());
            headersMap.put("User-Agent", "Midtrans-Java-Library-"+getLibraryVersion());
            if (!config.getIrisIdempotencyKey().isEmpty()) {
                headersMap.put("X-Idempotency-Key", config.getIrisIdempotencyKey());
            }
            if (!config.getPaymentIdempotencyKey().isEmpty()) {
                headersMap.put("Idempotency-Key", config.getPaymentIdempotencyKey());
            }
            if (!config.getPaymentAppendNotification().isEmpty()) {
                headersMap.put("X-Append-Notification", config.getPaymentAppendNotification());
            }
            if (!config.getPaymentOverrideNotification().isEmpty()) {
                headersMap.put("X-Override-Notification", config.getPaymentOverrideNotification());
            }

            Headers headers = Headers.of(headersMap);
            Request request = chain.request().newBuilder().headers(headers).build();

            return chain.proceed(request);
        };
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

    /**
     * OkHttpClient Logging Interceptor Configuration. Auto disable when production mode
     *
     * @return HttpLoggingInterceptor
     */
    private HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (config.isEnabledLog()) {
            return logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            return logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

    private String encodeServerKey(){
        return "Basic " + Base64.getEncoder().encodeToString((config.getSERVER_KEY() + ":").getBytes(StandardCharsets.UTF_8));
    }

    private String getLibraryVersion() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/version.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        return (properties.getProperty("version") == null) ? "unable to reach" : properties.getProperty("version");
    }
}
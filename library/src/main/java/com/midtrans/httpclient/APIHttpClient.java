package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.proxy.ProxyConfig;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Retrofit HttpClient configuration
 */
public class APIHttpClient {
    private static final Logger LOGGER = Logger.getLogger(APIHttpClient.class.getName());

    private Config config;

    public final static String POST = "POST";
    public final static String GET = "GET";
    public final static String PATCH = "PATCH";

    /**
     * APIHttpClient constructor
     *
     * @param config Midtrans {@link Config configuration}
     */
    public APIHttpClient(Config config) {
        this.config = config;
    }

    /**
     * HTTP request method for API Request
     */
    public <T> T request(String method, String url, Map<String, ?> requestBody) throws MidtransError {
        JSONObject jsonParam = new JSONObject(requestBody);
        OkHttpClient client = this.configHttpClient();

        RequestBody body = null;
        if (!method.equals(GET)) {
            MediaType mediaType = MediaType.parse("application/json");
            body = RequestBody.create(mediaType, jsonParam.toString());
        }

        Request request = new Request.Builder()
                .url(url)
                .method(method, body)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();

            assert response.body() != null;
            String responseBody = response.body().string();

            if (response.code() >= 400 && response.code() != 407) {
                throw new MidtransError(
                        "Midtrans API is returning API error. HTTP status code: " + response.code() + " API response: " + responseBody,
                        response.code(),
                        responseBody,
                        response
                );
            } else if (response.code() >= 400) {
                throw new MidtransError(
                        "Midtrans API is returning API error. HTTP status code: " + response.code() + " API response: " + responseBody,
                        response.code(),
                        responseBody,
                        response
                );
            } else {
                return (T) responseBody;
            }
        } catch (IOException e) {
            throw new MidtransError(
                    "IOException during API request to Midtrans:" + url + " with message:" + e.getMessage() + ". Likely connection failure, please check your internet connection and try again.",
                    0,
                    null,
                    null,
                    e
            );
        }
    }

    /**
     * HttpClient configuration
     *
     * @return OkHttpClient {@link OkHttpClient}
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
     * @return Interceptor {@link Interceptor}
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
            if (config.getCustomHeaders() != null) {
                for (Map.Entry<String, String> headerMap : config.getCustomHeaders().entrySet()) {
                    headersMap.put(headerMap.getKey(), headerMap.getValue());
                }
            }

            headersMap.put("Accept", "application/json");
            headersMap.put("Content-Type", "application/json");
            headersMap.put("Authorization", encodeServerKey());
            headersMap.put("User-Agent", "Midtrans-Java-Library-" + getLibraryVersion());
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

    private String encodeServerKey() {
        return "Basic " + Base64.getEncoder().encodeToString((config.getSERVER_KEY() + ":").getBytes(StandardCharsets.UTF_8));
    }

    private String getLibraryVersion() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/version.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        return (properties.getProperty("version") == null) ? "unable to reach" : properties.getProperty("version");
    }
}
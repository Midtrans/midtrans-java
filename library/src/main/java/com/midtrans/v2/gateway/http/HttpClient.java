package com.midtrans.v2.gateway.http;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.proxy.ProxyConfig;
import com.midtrans.v2.Midtrans;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient class Midtrans Java Library V2
 */
public class HttpClient {

    public final static String POST = "POST";
    public final static String GET = "GET";
    public final static String PATCH = "PATCH";

    public static <T> T request(String method, String url, ApiConfig apiConfig, Map<String, ?> requestBody) throws MidtransError {
        JSONObject jsonParam = new JSONObject(requestBody);
        OkHttpClient client = buildHttpClient(apiConfig);

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


    private static OkHttpClient buildHttpClient(ApiConfig apiConfig) {
        OkHttpClient httpClient = new OkHttpClient();
        ApiConfig defaultConfig = Optional.ofNullable(apiConfig).orElse(ApiConfig.getDefaultConfig());
        Interceptor logging = loggingInterceptor(defaultConfig.isEnableLog());

        Interceptor interceptor = chain -> {
            Request request = chain.request().newBuilder().headers(headers(defaultConfig)).build();
            return chain.proceed(request);
        };

        ConnectionPool connectionPool = new ConnectionPool(
                defaultConfig.getMaxConnectionPool(),
                defaultConfig.getKeepAliveDuration(),
                TimeUnit.SECONDS);

        if (defaultConfig.getProxyConfig() == null) {
            return httpClient.newBuilder()
                    .addInterceptor(logging)
                    .connectTimeout(defaultConfig.getConnectTimeout(), TimeUnit.SECONDS)
                    .readTimeout(defaultConfig.getReadTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(defaultConfig.getWriteTimeout(), TimeUnit.SECONDS)
                    .connectionPool(connectionPool)
                    .addInterceptor(interceptor)
                    .build();
        }

        final ProxyConfig proxyConfig = defaultConfig.getProxyConfig();
        Authenticator proxyAuthenticator = (route, response) -> {
            String credential = Credentials.basic(proxyConfig.getUsername(), proxyConfig.getPassword());
            return response.request().newBuilder().header("Proxy-Authorization", credential).build();
        };

        return httpClient.newBuilder()
                .connectTimeout(defaultConfig.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(defaultConfig.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(defaultConfig.getWriteTimeout(), TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort())))
                .proxyAuthenticator(proxyAuthenticator)
                .connectionPool(connectionPool)
                .addInterceptor(interceptor)
                .addInterceptor(logging)
                .build();
    }

    private static Headers headers(ApiConfig apiConfig) {
        Map<String, String> headersMap = new HashMap<>();
        ApiConfig defaultConfig = Optional.ofNullable(apiConfig).orElse(ApiConfig.getDefaultConfig());

        if (defaultConfig.getCustomHeaders() != null) {
            for (Map.Entry<String, String> headerMap : defaultConfig.getCustomHeaders().entrySet()) {
                headersMap.put(headerMap.getKey(), headerMap.getValue());
            }
        }

        headersMap.put("Accept", "application/json");
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", encodeKey(defaultConfig.getServerKey()));
        headersMap.put("User-Agent", "Midtrans-Java/v2-Library-" + getLibraryVersion());
        if (defaultConfig.getIrisIdempotencykey() != null) {
            headersMap.put("X-Idempotency-Key", defaultConfig.getIrisIdempotencykey());
        }
        if (defaultConfig.getPaymentIdempotencyKey() != null) {
            headersMap.put("Idempotency-Key", defaultConfig.getPaymentIdempotencyKey());
        }
        if (defaultConfig.getPaymentAppendNotification() != null) {
            headersMap.put("X-Append-Notification", defaultConfig.getPaymentAppendNotification());
        }
        if (defaultConfig.getPaymentOverrideNotification() != null) {
            headersMap.put("X-Override-Notification", defaultConfig.getPaymentOverrideNotification());
        }

        return Headers.of(headersMap);
    }

    private static HttpLoggingInterceptor loggingInterceptor(boolean isEnableLog) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (isEnableLog) {
            return logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            return logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

    private static String encodeKey(String apiKey) {
        return "Basic " + Base64.getEncoder().encodeToString((apiKey + ":").getBytes(StandardCharsets.UTF_8));
    }

    private static String getLibraryVersion()  {
        InputStream resourceAsStream = ApiConfig.class.getResourceAsStream("/version.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            return "Unable to reach version";
        }
        return (properties.getProperty("version") == null) ? "unable to reach" : properties.getProperty("version");
    }

}

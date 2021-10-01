package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.proxy.ProxyConfig;
import com.midtrans.utils.Utility;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient class
 */
public class APIHttpClient {
    public Config config;

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
    public static <T> T request(String method, String url, Config config, Map<String, ?> requestBody) throws MidtransError {
        OkHttpClient client = buildHttpClient(config);
        JSONObject jsonParam = new JSONObject(requestBody);

        RequestBody body = null;
        if (!method.equals(GET)) {
            //MediaType mediaType = MediaType.parse("application/json");
            body = RequestBody.create(null, jsonParam.toString());
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
            if (Utility.hasOwnProperty(responseBody, "status_code")) {
                JSONObject jsonObject = new JSONObject(responseBody);
                int statusCode = Integer.parseInt(jsonObject.getString("status_code"));
                if (statusCode >= 400 && statusCode != 407) {
                    throw new MidtransError(
                            "Midtrans API is returning API error. HTTP status code: " + statusCode + " API response: " + responseBody,
                            statusCode,
                            responseBody,
                            response
                    );
                }
            }

            if (response.code() >= 400) {
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
                    "IOException during API request to Midtrans: " + url + " with message: " + e.getMessage() + ". Likely connection failure, please check your internet connection and try again.",
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
    private static OkHttpClient buildHttpClient(Config config) {
        OkHttpClient httpClient = new OkHttpClient();

        Headers headers = null;
        try {
            headers = getHeadersConfig(config);
        } catch (MidtransError e) {
            e.printStackTrace();
        }

        // initialize http headers
        Headers finalHeaders = headers;
        Interceptor headersInterceptor = chain -> {
            Request request = chain.request().newBuilder().headers(finalHeaders).build();
            return chain.proceed(request);
        };

        // setup connection pool
        ConnectionPool connectionPool = new ConnectionPool(
                config.getMaxConnectionPool(),
                config.getKeepAliveDuration(),
                config.getHttpClientTimeUnit());

        // setup default http client
        if (config.getProxyConfig() == null) {
            return httpClient.newBuilder()
                    .connectTimeout(config.getConnectionTimeout(), config.getHttpClientTimeUnit())
                    .readTimeout(config.getReadTimeout(), config.getHttpClientTimeUnit())
                    .writeTimeout(config.getWriteTimeout(), config.getHttpClientTimeUnit())
                    .connectionPool(connectionPool)
                    .addInterceptor(headersInterceptor)
                    .addInterceptor(loggingInterceptor(config))
                    .build();
        }

        // setup http client with proxy
        final ProxyConfig proxyConfig = config.getProxyConfig();
        Authenticator proxyAuthenticator = (route, response) -> {
            String credential = Credentials.basic(proxyConfig.getUsername(), proxyConfig.getPassword());
            return response.request().newBuilder().header("Proxy-Authorization", credential).build();
        };

        return httpClient.newBuilder()
                .connectTimeout(config.getConnectionTimeout(), config.getHttpClientTimeUnit())
                .readTimeout(config.getReadTimeout(), config.getHttpClientTimeUnit())
                .writeTimeout(config.getWriteTimeout(), config.getHttpClientTimeUnit())
                .connectionPool(connectionPool)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort())))
                .proxyAuthenticator(proxyAuthenticator)
                .addInterceptor(headersInterceptor)
                .addInterceptor(loggingInterceptor(config))
                .build();
    }

    /**
     * Headers set configuration
     *
     * @return Interceptor {@link Interceptor}
     */
    private static Headers getHeadersConfig(Config config) throws MidtransError {

        Map<String, String> headersMap = new HashMap<>();
        if (config.getCustomHeaders() != null) {
            for (Map.Entry<String, String> headerMap : config.getCustomHeaders().entrySet()) {
                headersMap.put(headerMap.getKey(), headerMap.getValue());
            }
        }

        String serverKey = config.getServerKey();
        if (serverKey == null) {
            throw new MidtransError(
                    "No ServerKey provided. Please set your serverKey key. "
                            + "You can retrieve the ServerKey from the Midtrans Dashboard. "
                            + "See https://docs.midtrans.com/en/midtrans-account/overview?id=retrieving-api-access-keys for the details "
                            + "or contact support at support@midtrans.com if you have any questions.");
        } else if (serverKey.isEmpty()) {
            throw new MidtransError(
                    "The ServerKey is invalid, as it is an empty string. Please double-check your API key. "
                            + "You can check the ServerKey from the Midtrans Dashboard. "
                            + "See https://docs.midtrans.com/en/midtrans-account/overview?id=retrieving-api-access-keys for the details "
                            + "or contact support at support@midtrans.com if you have any questions."
            );
        } else if (serverKey.contains(" ")) {
            throw new MidtransError(
                    "The ServerKey is contains white-space. Please double-check your API key. "
                            + "You can check the ServerKey from the Midtrans Dashboard. "
                            + "See https://docs.midtrans.com/en/midtrans-account/overview?id=retrieving-api-access-keys for the details "
                            + "or contact support at support@midtrans.com if you have any questions."
            );
        }

        headersMap.put("Accept", "application/json");
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", Utility.encodeBase64(serverKey));
        headersMap.put("User-Agent", "Midtrans-Java-Library-" + Utility.getLibraryVersion());
        if (config.getIrisIdempotencyKey() != null) {
            headersMap.put("X-Idempotency-Key", config.getIrisIdempotencyKey());
        }
        if (config.getPaymentIdempotencyKey() != null) {
            headersMap.put("Idempotency-Key", config.getPaymentIdempotencyKey());
        }
        if (config.getPaymentAppendNotification() != null) {
            headersMap.put("X-Append-Notification", config.getPaymentAppendNotification());
        }
        if (config.getPaymentOverrideNotification() != null) {
            headersMap.put("X-Override-Notification", config.getPaymentOverrideNotification());
        }

        return Headers.of(headersMap);
    }

    /**
     * OkHttpClient Logging Interceptor Configuration. Auto disable when production mode
     *
     * @return HttpLoggingInterceptor
     */
    private static HttpLoggingInterceptor loggingInterceptor(Config config) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (config.isEnabledLog()) {
            return logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            return logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

}
package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.proxy.ProxyConfig;
import okhttp3.*;
import okhttp3.Authenticator;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
        if (config.getProxyConfig() == null) {
            return httpClient.newBuilder()
                    .connectTimeout(config.getConnectionTimeout(), TimeUnit.SECONDS)
                    .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
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

            Request header = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", this.encodeServerKey())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "Midtrans-Java-Library")
                    .build();
                return chain.proceed(header);
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

    private String encodeServerKey(){
        return "Basic " + Base64.getEncoder().encodeToString((config.getSERVER_KEY() + ":").getBytes(StandardCharsets.UTF_8));
    }
}



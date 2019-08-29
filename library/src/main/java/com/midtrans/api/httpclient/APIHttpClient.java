package com.midtrans.api.httpclient;

import com.midtrans.api.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class APIHttpClient {
    private static final Logger LOGGER = Logger.getLogger(APIHttpClient.class.getName());

    @Autowired
    private Config config;

    public APIHttpClient() {
    }

    public Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(config.getBASE_URL())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(defaultHttpClient)
                .build();
    }

    private OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    if (config.getSERVER_KEY().isEmpty() || config.getSERVER_KEY() == null) {
                        LOGGER.warning("Server key is empty....");
                        return chain.proceed(chain.request());
                    }
                    Request header = chain.request().newBuilder()
                            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", encodeServerKey())
                            .build();
                    return chain.proceed(header);
                }
            }).build();

    private String encodeServerKey(){
        return "Basic " + Base64.getEncoder().encodeToString((config.getSERVER_KEY() + ":").getBytes(StandardCharsets.UTF_8));
    }

    public void httpErrorHandle(int code) {
        switch (code) {
            case 400:
                LOGGER.warning("400 Bad Request: There was a problem in the JSON you submitted");
                break;
            case 401:
                LOGGER.warning("401 Unauthorized: Access denied due to unauthorized transaction, please check client or server key");
                break;
            case 404:
                LOGGER.warning("404 Not Found");
                break;
            case 500:
                LOGGER.warning("HTTP ERROR 500: Internal Server ERROR!");
                break;
            default:
                LOGGER.warning("Unknown ERROR");
                break;
        }
    }

}



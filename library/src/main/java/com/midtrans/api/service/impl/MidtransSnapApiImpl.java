package com.midtrans.api.service.impl;

import com.midtrans.api.Config;
import com.midtrans.api.httpclient.APIHttpClient;
import com.midtrans.api.httpclient.SnapApi;
import com.midtrans.api.service.MidtransSnapApi;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class MidtransSnapApiImpl implements MidtransSnapApi {
    private static final Logger LOGGER = Logger.getLogger(MidtransSnapApi.class.getName());


    @Autowired
    private APIHttpClient httpClient;

    @Autowired
    private Config config;


    @Override
    public String generateSnapToken(Map<String, Object> objectMap) {
        //TODO getConfig SNAP API TO BASE_URL
        config.getSnapApi();
        String snapToken = "";
        SnapApi snapApi = httpClient.getClient().create(SnapApi.class);
        Call<ResponseBody> call = snapApi.generateToken(objectMap);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    if (response.body() != null) {
                        JSONObject object = new JSONObject(response.body().string());
                        snapToken = object.getString("token");
                        LOGGER.info("Midtrans snap token: " + snapToken);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                httpClient.httpErrorHandle(response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snapToken;
    }

    @Override
    public String snapRedirect(Map<String, Object> objectMap) {
        config.getSnapApi();
        String snapRedirectURL = "";
        SnapApi snapApi = httpClient.getClient().create(SnapApi.class);
        Call<ResponseBody> call = snapApi.generateToken(objectMap);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    if (response.body() != null) {
                        JSONObject object = new JSONObject(response.body().string());
                        snapRedirectURL = object.getString("redirect_url");
                        LOGGER.info("Midtrans redirect URL: " + snapRedirectURL);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                httpClient.httpErrorHandle(response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snapRedirectURL;
    }

}

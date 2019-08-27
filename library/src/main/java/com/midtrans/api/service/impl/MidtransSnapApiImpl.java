package com.midtrans.api.service.impl;

import com.midtrans.api.httpclient.APIHttpClient;
import com.midtrans.api.service.MidtransSnapApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MidtransSnapApiImpl implements MidtransSnapApi {

    @Autowired
    private APIHttpClient httpClient;

    @Override
    public String generateToken(Map<String, Object> objectMap) {
        JSONObject response = httpClient.postMethod(objectMap, "/transactions");
        String token = response.getString("token");
        return token;
    }

    @Override
    public JSONObject tokenTransaction(Map<String, Object> objectMap) {
        return httpClient.postMethod(objectMap,"/transactions");
    }
}

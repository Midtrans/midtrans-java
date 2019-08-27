package com.midtrans.api.service.impl;

import com.midtrans.api.httpclient.APIHttpClient;
import com.midtrans.api.service.MidtransCoreApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
public class MidtransCoreApiImpl implements MidtransCoreApi {

    @Autowired
    private APIHttpClient httpClient;

    @Override
    public JSONObject getToken(MultiValueMap<String, String> params) {
        return httpClient.getMethod(params, "/token");
    }

    @Override
    public JSONObject cargeCC(Map<String, Object> objectMap) {
        return httpClient.postMethod(objectMap, "/charge");
    }


}

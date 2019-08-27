package com.midtrans.api.service;

import org.json.JSONObject;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface MidtransCoreApi {
    JSONObject getToken(MultiValueMap<String, String> params);
    JSONObject cargeCC(Map<String, Object> objectMap);
}

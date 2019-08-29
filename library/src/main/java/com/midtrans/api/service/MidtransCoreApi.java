package com.midtrans.api.service;

import org.json.JSONObject;
import java.util.Map;

public interface MidtransCoreApi {
    JSONObject getToken(Map<String, String> params);
    JSONObject cargeCC(Map<String, Object> objectMap);
}

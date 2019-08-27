package com.midtrans.api.service;

import org.json.JSONObject;

import java.util.Map;

public interface MidtransSnapApi {
    String generateToken(Map<String, Object> objectMap);
    JSONObject tokenTransaction(Map<String, Object> objectMap);
}

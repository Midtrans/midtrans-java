package com.midtrans.api.service;

import org.json.JSONObject;

import java.util.Map;

public interface MidtransCoreApi {
    JSONObject chargeTransaction(Map<String, Object> body);

    JSONObject checkTransaction(String orderId);

    JSONObject approveTransaction(String orderId);

    JSONObject cancelTransaction(String orderId);

    JSONObject expireTransaction(String orderId);

    JSONObject refundTransaction(String orderId, Map<String, String> body);

}

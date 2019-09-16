package com.midtrans.api.service;

import com.midtrans.api.Config;
import org.json.JSONObject;
import java.util.Map;

public interface MidtransSnapApi {

    Config apiConfig();

    // To get snap transaction with return json raw object
    JSONObject createTransaction(Map<String, Object> params);

    // To get snap token with return String token
    String createTransactionToken(Map<String, Object> params);

    // To get snap redirect url with return String redirect url
    String createTransactionRedirectUrl(Map<String, Object> params);
}
package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.utils.Utility;
import org.json.JSONObject;

import java.util.Map;

public class SnapApi {

    public static JSONObject createTransaction(Map<String, Object> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getSnapApiURL(),
                configOptions,
                requestBody
        ));
    }

    public static JSONObject createTransaction(Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                Config.getGlobalConfig().getSnapApiURL(),
                Config.getGlobalConfig(),
                requestBody
        ));
    }

    public static String createTransactionToken(Map<String, Object> requestBody) throws MidtransError {
        return createTransaction(requestBody).getString("token");
    }

    public static String createTransactionToken(Map<String, Object> requestBody, Config configOptions) throws MidtransError {
        return createTransaction(requestBody, configOptions).getString("token");
    }

    public static String createTransactionRedirectUrl(Map<String, Object> requestBody) throws MidtransError {
        return createTransaction(requestBody).getString("redirect_url");
    }

    public static String createTransactionRedirectUrl(Map<String, Object> requestBody, Config configOptions) throws MidtransError {
        return createTransaction(requestBody, configOptions).getString("redirect_url");
    }

}
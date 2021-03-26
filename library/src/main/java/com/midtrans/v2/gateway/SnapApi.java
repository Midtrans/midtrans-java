package com.midtrans.v2.gateway;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.http.ApiConfig;
import com.midtrans.v2.gateway.http.HttpClient;
import org.json.JSONObject;

import java.util.Map;


public class SnapApi {

    private static final String SNAP_API_VERSION = "v1";

    public static String snapBaseUrl(boolean isProduction) {
        String pathUrl = "snap/" + SNAP_API_VERSION + "/transactions";
        if (isProduction) {
            Midtrans.enableLog = false;
            return Midtrans.getSnapProductionBaseUrl() + pathUrl;
        } else {
            Midtrans.enableLog = true;
            return Midtrans.getSnapSandboxBaseUrl() + pathUrl;
        }
    }

    public static JSONObject createTransaction(Map<String, Object> param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, snapBaseUrl(Midtrans.isProduction), ApiConfig.getDefaultConfig(), param));
    }

    public static JSONObject createTransaction(ApiConfig apiConfig, Map<String, Object> param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, snapBaseUrl(apiConfig.isProduction()), apiConfig, param));
    }

    public static String createTransactionToken(Map<String, Object> param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, snapBaseUrl(Midtrans.isProduction()), ApiConfig.getDefaultConfig(), param)).getString("token");
    }

    public static String createTransactionToken(ApiConfig apiConfig, Map<String, Object> param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, snapBaseUrl(Midtrans.isProduction()), apiConfig, param)).getString("token");
    }

    public static String createTransactionRedirectUrl(Map<String, Object> param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, snapBaseUrl(Midtrans.isProduction()), ApiConfig.getDefaultConfig(), param)).getString("redirect_url");
    }

    public static String createTransactionRedirectUrl(ApiConfig apiConfig, Map<String, Object> param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, snapBaseUrl(Midtrans.isProduction()), apiConfig, param)).getString("redirect_url");
    }


}
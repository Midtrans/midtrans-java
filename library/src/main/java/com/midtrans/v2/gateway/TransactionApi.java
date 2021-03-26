package com.midtrans.v2.gateway;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.http.ApiConfig;
import com.midtrans.v2.gateway.http.HttpClient;
import org.json.JSONObject;

import java.util.Map;

import static com.midtrans.v2.gateway.CoreApi.coreApiBaseUrl;

public class TransactionApi {

    public static JSONObject checkTransaction(String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/status", ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject checkTransaction(ApiConfig apiConfig, String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/status", apiConfig, null));
    }

    public static JSONObject approveTransaction(String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/approve", ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject approveTransaction(ApiConfig apiConfig, String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/approve", apiConfig, null));
    }

    public static JSONObject denyTransaction(String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/deny", ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject denyTransaction(ApiConfig apiConfig, String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/deny", apiConfig, null));
    }

    public static JSONObject cancelTransaction(String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/cancel", ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject cancelTransaction(ApiConfig apiConfig, String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/cancel", apiConfig, null));
    }

    public static JSONObject expireTransaction(String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/expire", ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject expireTransaction(ApiConfig apiConfig, String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/expire", apiConfig, null));
    }

    public static JSONObject refundTransaction(String param, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/refund", ApiConfig.getDefaultConfig(), requestBody));
    }

    public static JSONObject refundTransaction(ApiConfig apiConfig, String param, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/refund", apiConfig, requestBody));
    }

    public static JSONObject directRefundTransaction(String param, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/refund/online/direct", ApiConfig.getDefaultConfig(), requestBody));
    }

    public static JSONObject directRefundTransaction(ApiConfig apiConfig, String param, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/refund/online/direct", apiConfig, requestBody));
    }

    public static JSONObject captureTransaction(Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + "capture", ApiConfig.getDefaultConfig(), requestBody));
    }

    public static JSONObject captureTransaction(ApiConfig apiConfig, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + "capture", apiConfig, requestBody));
    }

    public static JSONObject getStatusB2b(String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/status/b2b", ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject getStatusB2b(ApiConfig apiConfig, String param) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, coreApiBaseUrl(Midtrans.isProduction(), CoreApi.API_VERSION) + param + "/status/b2b", apiConfig, null));
    }
}

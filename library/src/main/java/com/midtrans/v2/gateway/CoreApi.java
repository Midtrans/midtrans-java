package com.midtrans.v2.gateway;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.http.ApiConfig;
import com.midtrans.v2.gateway.http.HttpClient;
import org.json.JSONObject;

import java.util.Map;

public class CoreApi {

    public static final String API_VERSION = "v2";

    public static String coreApiBaseUrl(boolean isProduction, String apiVersion) {
        if (isProduction) {
            Midtrans.enableLog = false;
            return Midtrans.getProductionBaseUrl() + apiVersion + "/";
        } else {
            Midtrans.enableLog = true;
            return Midtrans.getSandboxBaseUrl() + apiVersion + "/";
        }
    }

    public static JSONObject cardToken(Map<String, String> param) throws MidtransError {
        String url = coreApiBaseUrl(Midtrans.isProduction, API_VERSION) +
                "token?client_key=" + param.get("client_key") +
                "&card_number=" + param.get("card_number") +
                "&card_exp_month=" + param.get("card_exp_month") +
                "&card_exp_year=" + param.get("card_exp_year") +
                "&card_cvv=" + param.get("card_cvv");
        return new JSONObject((String) HttpClient.request(HttpClient.GET, url, null, null));
    }

    public static JSONObject registerCard(Map<String, String> param) throws MidtransError {
        String url = coreApiBaseUrl(Midtrans.isProduction, API_VERSION) +
                "card/register?client_key=" + param.get("client_key") +
                "&card_number=" + param.get("card_number") +
                "&card_exp_month=" + param.get("card_exp_month") +
                "&card_exp_year=" + param.get("card_exp_year") +
                "&card_cvv=" + param.get("card_cvv");
        return new JSONObject((String) HttpClient.request(HttpClient.GET, url, null, null));
    }

    public static JSONObject cardPointInquiry(String tokenId) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, coreApiBaseUrl(Midtrans.isProduction(), API_VERSION) + "point_inquiry/" + tokenId, ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject cardPointInquiry(ApiConfig apiConfig, String tokenId) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, coreApiBaseUrl(apiConfig.isProduction(), API_VERSION) + "point_inquiry/" + tokenId, apiConfig, null));
    }

    public static JSONObject chargeTransaction(Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(Midtrans.isProduction(), API_VERSION) + "charge", ApiConfig.getDefaultConfig(), requestBody));
    }

    public static JSONObject chargeTransaction(ApiConfig apiConfig, Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, coreApiBaseUrl(apiConfig.isProduction(), API_VERSION) + "charge", apiConfig, requestBody));
    }

    public static JSONObject getBin(String binNumber) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, coreApiBaseUrl(Midtrans.isProduction(), "v1") + "bins/" + binNumber, ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject getBin(ApiConfig apiConfig, String binNumber) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, coreApiBaseUrl(apiConfig.isProduction(), "v1") + "bins/" + binNumber, apiConfig, null));
    }
}

package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;

import java.util.Map;


public class CoreApi {

    public static final String API_VERSION = "v2";

    public static JSONObject cardToken(Map<String, String> param, Config configOptions) throws MidtransError {
        String url = Config.getGlobalConfig().getCoreApiURL() + API_VERSION +
                "/token?client_key=" + param.get("client_key") +
                "&card_number=" + param.get("card_number") +
                "&card_exp_month=" + param.get("card_exp_month") +
                "&card_exp_year=" + param.get("card_exp_year") +
                "&card_cvv=" + param.get("card_cvv");
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET, url, configOptions, null));
    }

    public static JSONObject cardToken(Map<String, String> param) throws MidtransError {
        return cardToken(param, Config.getGlobalConfig());
    }

    public static JSONObject registerCard(Map<String, String> param, Config configOptions) throws MidtransError {
        String url = Config.getGlobalConfig().getCoreApiURL() + API_VERSION +
                "/card/register?client_key=" + param.get("client_key") +
                "&card_number=" + param.get("card_number") +
                "&card_exp_month=" + param.get("card_exp_month") +
                "&card_exp_year=" + param.get("card_exp_year") +
                "&card_cvv=" + param.get("card_cvv");
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET, url, configOptions, null));
    }

    public static JSONObject registerCard(Map<String, String> param) throws MidtransError {
        return registerCard(param, Config.getGlobalConfig());
    }

    public static JSONObject cardPointInquiry(String tokenId, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET, configOptions.getCoreApiURL() + API_VERSION + "/point_inquiry/" + tokenId, configOptions, null));
    }

    public static JSONObject cardPointInquiry(String tokenId) throws MidtransError {
        return cardPointInquiry(tokenId, Config.getGlobalConfig());
    }

    public static JSONObject chargeTransaction(Map<String, Object> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.POST, configOptions.getCoreApiURL() + API_VERSION + "/charge", configOptions, requestBody));
    }

    public static JSONObject chargeTransaction(Map<String, Object> requestBody) throws MidtransError {
        return chargeTransaction(requestBody, Config.getGlobalConfig());
    }

    public static JSONObject getBin(String binNumber, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET, configOptions.getCoreApiURL() + "v1" + "/bins/" + binNumber, configOptions, null));
    }

    public static JSONObject getBin(String binNumber) throws MidtransError {
        return getBin(binNumber, Config.getGlobalConfig());
    }
}

package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;

import java.util.Map;

import static com.midtrans.httpclient.CoreApi.API_VERSION2;


public class TransactionApi {

    public static JSONObject checkTransaction(String param, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.GET, configOptions.getCoreApiURL() + API_VERSION2 + "/" + param + "/status",
                configOptions,
                null
        ));
    }

    public static JSONObject checkTransaction(String param) throws MidtransError {
        return checkTransaction(param, Config.getGlobalConfig());
    }

    public static JSONObject approveTransaction(String param, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST, configOptions.getCoreApiURL() + API_VERSION2 + "/" + param + "/approve",
                configOptions,
                null
        ));
    }

    public static JSONObject approveTransaction(String param) throws MidtransError {
        return approveTransaction(param, Config.getGlobalConfig());
    }

    public static JSONObject denyTransaction(String param, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION2 + "/" + param + "/deny",
                configOptions,
                null
        ));
    }

    public static JSONObject denyTransaction(String param) throws MidtransError {
        return denyTransaction(param, Config.getGlobalConfig());
    }

    public static JSONObject cancelTransaction(String param) throws MidtransError {
        return cancelTransaction(param, Config.getGlobalConfig());
    }

    public static JSONObject cancelTransaction(String param, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION2 + "/" + param + "/cancel",
                configOptions,
                null
        ));
    }

    public static JSONObject expireTransaction(String param, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION2 + "/" + param + "/expire",
                configOptions,
                null
        ));
    }

    public static JSONObject expireTransaction(String param) throws MidtransError {
        return expireTransaction(param, Config.getGlobalConfig());
    }

    public static JSONObject refundTransaction(String param, Map<String, String> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST, configOptions.getCoreApiURL() + API_VERSION2 + "/" + param + "/refund",
                configOptions,
                requestBody
        ));
    }

    public static JSONObject refundTransaction(String param, Map<String, String> requestBody) throws MidtransError {
        return refundTransaction(param, requestBody, Config.getGlobalConfig());
    }

    public static JSONObject directRefundTransaction(String param, Map<String, String> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION2 + "/" + param + "/refund/online/direct",
                configOptions,
                requestBody
        ));
    }

    public static JSONObject directRefundTransaction(String param, Map<String, String> requestBody) throws MidtransError {
        return directRefundTransaction(param, requestBody, Config.getGlobalConfig());
    }

    public static JSONObject captureTransaction(Map<String, String> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION2 + "/" + "capture",
                configOptions,
                requestBody
        ));
    }

    public static JSONObject captureTransaction(Map<String, String> requestBody) throws MidtransError {
        return captureTransaction(requestBody, Config.getGlobalConfig());
    }

    public static JSONObject getStatusB2b(String param, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.GET,
                configOptions.getCoreApiURL() + API_VERSION2 + "/" + param + "/status/b2b",
                configOptions,
                null
        ));
    }

    public static JSONObject getStatusB2b(String param) throws MidtransError {
        return getStatusB2b(param, Config.getGlobalConfig());
    }
}

package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;

import java.util.Map;


public class CoreApi {

    public static final String API_VERSION2 = "v2";
    public static final String API_VERSION1 = "v1";

    public static JSONObject cardToken(String cardNumber, String expMonth, String expYear, String cvv, Config configOptions) throws MidtransError {
        String url = Config.getGlobalConfig().getCoreApiURL() + API_VERSION2 +
                "/token?client_key=" + configOptions.getClientKey() +
                "&card_number=" + cardNumber +
                "&card_exp_month=" + expMonth +
                "&card_exp_year=" + expYear +
                "&card_cvv=" + cvv;
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET, url, configOptions, null));
    }

    public static JSONObject cardToken(String cardNumber, String expMonth, String expYear, String cvv) throws MidtransError {
        return cardToken(cardNumber, expMonth, expYear, cvv, Config.getGlobalConfig());
    }

    public static JSONObject cardToken(Map<String, String> param, Config configOptions) throws MidtransError {
        String url = Config.getGlobalConfig().getCoreApiURL() + API_VERSION2 +
                "/token?client_key=" + configOptions.getClientKey() +
                "&card_number=" + param.get("card_number") +
                "&card_exp_month=" + param.get("card_exp_month") +
                "&card_exp_year=" + param.get("card_exp_year") +
                "&card_cvv=" + param.get("card_cvv");
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET, url, configOptions, null));
    }

    public static JSONObject cardToken(Map<String, String> param) throws MidtransError {
        return cardToken(param, Config.getGlobalConfig());
    }

    public static JSONObject registerCard(String cardNumber, String expMonth, String expYear, Config configOptions) throws MidtransError {
        String url = Config.getGlobalConfig().getCoreApiURL() + API_VERSION2 +
                "/card/register?client_key=" + configOptions.getClientKey() +
                "&card_number=" + cardNumber +
                "&card_exp_month=" + expMonth +
                "&card_exp_year=" + expYear;
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET, url, configOptions, null));
    }

    public static JSONObject registerCard(String cardNumber, String expMonth, String expYear) throws MidtransError {
        return registerCard(cardNumber, expMonth, expYear, Config.getGlobalConfig());
    }

    public static JSONObject registerCard(Map<String, String> param, Config configOptions) throws MidtransError {
        String url = Config.getGlobalConfig().getCoreApiURL() + API_VERSION2 +
                "/card/register?client_key=" + configOptions.getClientKey() +
                "&card_number=" + param.get("card_number") +
                "&card_exp_month=" + param.get("card_exp_month") +
                "&card_exp_year=" + param.get("card_exp_year");
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET, url, configOptions, null));
    }

    public static JSONObject registerCard(Map<String, String> param) throws MidtransError {
        return registerCard(param, Config.getGlobalConfig());
    }

    public static JSONObject cardPointInquiry(String tokenId, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.GET,
                configOptions.getCoreApiURL() + API_VERSION2 + "/point_inquiry/" + tokenId,
                configOptions,
                null
        ));
    }

    public static JSONObject cardPointInquiry(String tokenId) throws MidtransError {
        return cardPointInquiry(tokenId, Config.getGlobalConfig());
    }

    public static JSONObject chargeTransaction(Map<String, Object> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION2 + "/charge",
                configOptions,
                requestBody
        ));
    }

    public static JSONObject chargeTransaction(Map<String, Object> requestBody) throws MidtransError {
        return chargeTransaction(requestBody, Config.getGlobalConfig());
    }

    public static JSONObject getBin(String binNumber, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.GET,
                configOptions.getCoreApiURL() + "v1" + "/bins/" + binNumber,
                configOptions,
                null
        ));
    }

    public static JSONObject getBin(String binNumber) throws MidtransError {
        return getBin(binNumber, Config.getGlobalConfig());
    }

    public static JSONObject createSubscription(Map<String, Object> subscriptionReq, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION1 + "/subscriptions",
                configOptions,
                subscriptionReq
        ));
    }

    public static JSONObject createSubscription(Map<String, Object> subscriptionReq) throws MidtransError {
        return createSubscription(subscriptionReq, Config.getGlobalConfig());
    }

    public static JSONObject getSubscription(String subscriptionId, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.GET,
                configOptions.getCoreApiURL() + API_VERSION1 + "/subscriptions/" + subscriptionId,
                configOptions,
                null
        ));
    }

    public static JSONObject getSubscription(String subscriptionId) throws MidtransError {
        return getSubscription(subscriptionId, Config.getGlobalConfig());
    }

    public static JSONObject disableSubscription(String subscriptionId, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION1 + "/subscriptions/" + subscriptionId + "/disable",
                configOptions,
                null));
    }

    public static JSONObject disableSubscription(String subscriptionId) throws MidtransError {
        return disableSubscription(subscriptionId, Config.getGlobalConfig());
    }

    public static JSONObject enableSubscription(String subscriptionId, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION1 + "/subscriptions/" + subscriptionId + "/enable",
                configOptions,
                null
        ));
    }

    public static JSONObject enableSubscription(String subscriptionId) throws MidtransError {
        return enableSubscription(subscriptionId, Config.getGlobalConfig());
    }

    public static JSONObject updateSubscription(String subscriptionId, Map<String, Object> subscriptionReq, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.PATCH,
                configOptions.getCoreApiURL() + API_VERSION1 + "/subscriptions/" + subscriptionId,
                configOptions,
                subscriptionReq
        ));
    }

    public static JSONObject updateSubscription(String subscriptionId, Map<String, Object> subscriptionReq) throws MidtransError {
        return updateSubscription(subscriptionId, subscriptionReq, Config.getGlobalConfig());
    }

    public static JSONObject linkPaymentAccount(Map<String, Object> accountRequest, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION2 + "/pay/account",
                configOptions,
                accountRequest
        ));
    }

    public static JSONObject linkPaymentAccount(Map<String, Object> accountRequest) throws MidtransError {
        return linkPaymentAccount(accountRequest, Config.getGlobalConfig());
    }

    public static JSONObject getPaymentAccount(String accountId, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.GET,
                configOptions.getCoreApiURL() + API_VERSION2 + "/pay/account/" + accountId,
                configOptions,
                null
        ));
    }

    public static JSONObject getPaymentAccount(String accountId) throws MidtransError {
        return getPaymentAccount(accountId, Config.getGlobalConfig());
    }

    public static JSONObject unlinkPaymentAccount(String accountId, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getCoreApiURL() + API_VERSION2 + "/pay/account/" + accountId + "/unbind",
                configOptions,
                null
        ));
    }

    public static JSONObject unlinkPaymentAccount(String accountId) throws MidtransError {
        return unlinkPaymentAccount(accountId, Config.getGlobalConfig());
    }
}

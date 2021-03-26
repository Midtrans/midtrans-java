package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.APIHttpClient;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.midtrans.v2.gateway.http.HttpClient;
import org.json.JSONObject;

import java.util.Map;

/**
 * Implements from {@link MidtransCoreApi MidtransCoreApi}
 */
public class MidtransCoreApiImpl implements MidtransCoreApi {
    private final Config config;
    private final APIHttpClient httpClient;
    private final String API_VERSION = "v2";

    /**
     * CoreAPI constructor
     *
     * @param config Midtrans {@link Config configuration}
     */
    public MidtransCoreApiImpl(Config config) {
        this.config = config;
        this.httpClient = new APIHttpClient(config);
    }

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public JSONObject chargeTransaction(Map<String, Object> body) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "/charge",
                body));
    }

    @Override
    public JSONObject checkTransaction(String param) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                HttpClient.GET,
                apiConfig().getBASE_URL() + API_VERSION + "/" + param + "/status",
                null));
    }

    @Override
    public JSONObject approveTransaction(String param) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "/" + param + "/approve",
                null));
    }

    @Override
    public JSONObject cancelTransaction(String param) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "/" + param + "/cancel",
                null));
    }

    @Override
    public JSONObject expireTransaction(String param) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "/" + param + "/expire",
                null));
    }

    @Override
    public JSONObject refundTransaction(String param, Map<String, String> body) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "/" + param + "/refund",
                body));
    }

    @Override
    public JSONObject cardToken(Map<String, String> params) throws MidtransError {
        String url = apiConfig().getBASE_URL() + API_VERSION +
                "/token?client_key=" + apiConfig().getCLIENT_KEY() +
                "&card_number=" + params.get("card_number") +
                "&card_exp_month=" + params.get("card_exp_month") +
                "&card_exp_year=" + params.get("card_exp_year") +
                "&card_cvv=" + params.get("card_cvv");
        return new JSONObject((String) httpClient.request(APIHttpClient.GET, url, null));
    }

    @Override
    public JSONObject registerCard(Map<String, String> params) throws MidtransError {
        String url = apiConfig().getBASE_URL() + API_VERSION +
                "/card/register?client_key=" + apiConfig().getCLIENT_KEY() +
                "&card_number=" + params.get("card_number") +
                "&card_exp_month=" + params.get("card_exp_month") +
                "&card_exp_year=" + params.get("card_exp_year") +
                "&card_cvv=" + params.get("card_cvv");
        return new JSONObject((String) httpClient.request(APIHttpClient.GET, url, null));
    }

    @Override
    public JSONObject cardPointInquiry(String tokenId) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.GET,
                apiConfig().getBASE_URL() + API_VERSION + "/point_inquiry/" + tokenId,
                null)
        );
    }

    @Override
    public JSONObject captureTransaction(Map<String, String> params) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "/capture",
                params));
    }

    @Override
    public JSONObject getTransactionStatusB2B(String param) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.GET,
                apiConfig().getBASE_URL() + API_VERSION + "/" + param + "/status/b2b",
                null));
    }

    @Override
    public JSONObject directRefundTransaction(String param, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "/" + param + "/refund/online/direct",
                requestBody));
    }

    @Override
    public JSONObject denyTransaction(String param) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "/" + param + "/deny",
                null));
    }

    @Override
    public JSONObject getBIN(String binNumber) throws MidtransError {
        return new JSONObject((String) httpClient.request(
                APIHttpClient.GET,
                apiConfig().getBASE_URL() + "v1/" + "bins/" + binNumber,
                null));
    }
}

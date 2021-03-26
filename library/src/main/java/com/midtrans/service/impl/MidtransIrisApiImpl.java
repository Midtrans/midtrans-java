package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.APIHttpClient;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransIrisApi;
import com.midtrans.v2.gateway.http.HttpClient;
import okhttp3.HttpUrl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MidtransIrisApiImpl implements MidtransIrisApi {
    private final Config config;
    private final APIHttpClient httpClient;
    private final String API_VERSION = "api/v1/";


    /**
     * IrisAPI constructor
     *
     * @param config Api Config class
     */
    public MidtransIrisApiImpl(Config config) {
        this.config = config;
        this.httpClient = new APIHttpClient(config);
    }

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public String ping() throws MidtransError {
        return httpClient.request(APIHttpClient.GET, apiConfig().getBASE_URL() + API_VERSION + "ping", null);
    }

    @Override
    public JSONObject getBalance() throws MidtransError {
        return new JSONObject((String) httpClient.request(APIHttpClient.GET, apiConfig().getBASE_URL() + API_VERSION + "balance", null));
    }

    @Override
    public JSONObject createBeneficiaries(Map<String, String> params) throws MidtransError {
        return new JSONObject((String) httpClient.request(HttpClient.POST, apiConfig().getBASE_URL() + API_VERSION + "beneficiaries", params));
    }

    @Override
    public JSONObject updateBeneficiaries(String aliasName, Map<String, String> params) throws MidtransError {
        return new JSONObject((String) httpClient.request(APIHttpClient.PATCH,
                apiConfig().getBASE_URL() + API_VERSION + "beneficiaries/" + aliasName,
                params));
    }

    @Override
    public JSONArray getBeneficiaries() throws MidtransError {
        return new JSONArray((String) httpClient.request(
                APIHttpClient.GET, apiConfig().getBASE_URL() + API_VERSION + "beneficiaries", null));
    }

    @Override
    public JSONObject createPayouts(Map<String, Object> params) throws MidtransError {
        return new JSONObject((String) httpClient.request(APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "payouts",
                Optional.ofNullable(params).orElse(new HashMap<>())));
    }

    @Override
    public JSONObject approvePayouts(Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) httpClient.request(APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "payouts/approve",
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    @Override
    public JSONObject rejectPayouts(Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) httpClient.request(APIHttpClient.POST,
                apiConfig().getBASE_URL() + API_VERSION + "payouts/reject",
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    @Override
    public JSONObject getPayoutDetails(String referenceNo) throws MidtransError {
        return new JSONObject((String) httpClient.request(APIHttpClient.GET,
                apiConfig().getBASE_URL() + API_VERSION + "payouts/" + Optional.ofNullable(referenceNo).orElse("null"),
                null));
    }

    @Override
    public JSONArray getTransactionHistory(String fromDate, String toDate) throws MidtransError {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(apiConfig().getBASE_URL() + API_VERSION + "statements")).newBuilder()
                .addQueryParameter("from_date", fromDate)
                .addQueryParameter("to_date", toDate)
                .build();

        return new JSONArray((String) httpClient.request(APIHttpClient.GET,
                url.toString(),
                null));
    }

    @Override
    public JSONArray getTopUpChannels() throws MidtransError {
        return new JSONArray((String) httpClient.request(APIHttpClient.GET,
                apiConfig().getBASE_URL() + API_VERSION + "channels",
                null));
    }

    @Override
    public JSONArray getBankAccounts() throws MidtransError {
        return new JSONArray((String) httpClient.request(APIHttpClient.GET,
                apiConfig().getBASE_URL() + API_VERSION + "bank_accounts",
                null));
    }

    @Override
    public JSONObject getFacilitatorBalance(String bankAccountId) throws MidtransError {
        return new JSONObject((String) httpClient.request(APIHttpClient.GET,
                apiConfig().getBASE_URL() + API_VERSION + "bank_account/" + Optional.ofNullable(bankAccountId).orElse("null") + "/balance" + Optional.ofNullable(bankAccountId).orElse("null"),
                null));
    }

    @Override
    public JSONObject getBeneficiaryBanks() throws MidtransError {
        return new JSONObject((String) httpClient.request(APIHttpClient.GET,
                apiConfig().getBASE_URL() + API_VERSION + "beneficiary_banks",
                null));
    }

    @Override
    public JSONObject validateBankAccount(String bank, String account) throws MidtransError {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(apiConfig().getBASE_URL() + API_VERSION + "account_validation")).newBuilder()
                .addQueryParameter("bank", bank)
                .addQueryParameter("account", account)
                .build();

        return new JSONObject((String) httpClient.request(APIHttpClient.GET,
                url.toString(),
                null));
    }

}

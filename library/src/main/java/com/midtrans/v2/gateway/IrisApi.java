package com.midtrans.v2.gateway;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.http.ApiConfig;
import com.midtrans.v2.gateway.http.HttpClient;
import okhttp3.HttpUrl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IrisApi {

    public static String irisApiBaseUrl(boolean isProduction) {
        if (isProduction) {
            Midtrans.enableLog = false;
            return Midtrans.getIrisProductionBaseUrl();
        } else {
            Midtrans.enableLog = true;
            return Midtrans.getIrisSandboxBaseUrl();
        }
    }

    public static String ping() throws MidtransError {
        return HttpClient.request(HttpClient.GET, irisApiBaseUrl(Midtrans.isProduction()) + "ping", ApiConfig.getDefaultConfig(), null);
    }

    public static JSONObject getBalance() throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, irisApiBaseUrl(Midtrans.isProduction()) + "balance", ApiConfig.getDefaultConfig(), null));
    }

    public static JSONObject getBalance(ApiConfig apiConfig) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET, irisApiBaseUrl(Midtrans.isProduction()) + "balance", apiConfig, null));
    }

    public static JSONObject createBeneficiaries(Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, irisApiBaseUrl(Midtrans.isProduction()) + "beneficiaries", ApiConfig.getDefaultConfig(), requestBody));
    }

    public static JSONObject createBeneficiaries(ApiConfig apiConfig, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST, irisApiBaseUrl(Midtrans.isProduction()) + "beneficiaries", apiConfig, requestBody));
    }

    public static JSONObject updateBeneficiaries(String aliasName, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.PATCH,
                irisApiBaseUrl(Midtrans.isProduction()) + "beneficiaries/" + aliasName,
                ApiConfig.getDefaultConfig(),
                requestBody));
    }

    public static JSONObject updateBeneficiaries(ApiConfig apiConfig, String aliasName, Map<String, String> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.PATCH,
                irisApiBaseUrl(Midtrans.isProduction()) + "beneficiaries/" + Optional.ofNullable(aliasName).orElse("null"),
                apiConfig,
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONArray getBeneficiaries() throws MidtransError {
        return new JSONArray((String) HttpClient.request(HttpClient.GET, irisApiBaseUrl(Midtrans.isProduction()) + "beneficiaries", ApiConfig.getDefaultConfig(), null));
    }

    public static JSONArray getBeneficiaries(ApiConfig apiConfig) throws MidtransError {
        return new JSONArray((String) HttpClient.request(HttpClient.GET, irisApiBaseUrl(Midtrans.isProduction()) + "beneficiaries", apiConfig, null));
    }

    public static JSONObject createPayouts(Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST,
                irisApiBaseUrl(Midtrans.isProduction()) + "payouts",
                ApiConfig.getDefaultConfig(),
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject createPayouts(ApiConfig apiConfig, Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST,
                irisApiBaseUrl(Midtrans.isProduction()) + "payouts",
                apiConfig,
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject approvePayouts(Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST,
                irisApiBaseUrl(Midtrans.isProduction()) + "payouts/approve",
                ApiConfig.getDefaultConfig(),
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject approvePayouts(ApiConfig apiConfig, Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST,
                irisApiBaseUrl(Midtrans.isProduction()) + "payouts/approve",
                apiConfig,
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject rejectPayouts(Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST,
                irisApiBaseUrl(Midtrans.isProduction()) + "payouts/reject",
                ApiConfig.getDefaultConfig(),
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject rejectPayouts(ApiConfig apiConfig, Map<String, Object> requestBody) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.POST,
                irisApiBaseUrl(Midtrans.isProduction()) + "payouts/reject",
                apiConfig,
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject getPayoutDetails(String referenceNo) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "payouts/" + Optional.ofNullable(referenceNo).orElse("null"),
                ApiConfig.getDefaultConfig(),
                null));
    }

    public static JSONObject getPayoutDetails(ApiConfig apiConfig, String referenceNo) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "payouts/" + Optional.ofNullable(referenceNo).orElse("null"),
                apiConfig,
                null));
    }

    public static JSONArray getTransactionHistory(String fromDate, String toDate) throws MidtransError {
        HttpUrl url = HttpUrl.parse(irisApiBaseUrl(Midtrans.isProduction()) + "statements").newBuilder()
                .addQueryParameter("from_date", fromDate)
                .addQueryParameter("to_date", toDate)
                .build();

        return new JSONArray((String) HttpClient.request(HttpClient.GET,
                url.toString(),
                ApiConfig.getDefaultConfig(),
                null));
    }

    public static JSONArray getTransactionHistory(ApiConfig apiConfig, String fromDate, String toDate) throws MidtransError {
        HttpUrl url = HttpUrl.parse(irisApiBaseUrl(Midtrans.isProduction()) + "statements").newBuilder()
                .addQueryParameter("from_date", fromDate)
                .addQueryParameter("to_date", toDate)
                .build();

        return new JSONArray((String) HttpClient.request(HttpClient.GET,
                url.toString(),
                apiConfig,
                null));
    }

    public static JSONArray getTopUpChannels() throws MidtransError {
        return new JSONArray((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "channels",
                ApiConfig.getDefaultConfig(),
                null));
    }

    public static JSONArray getTopUpChannels(ApiConfig apiConfig) throws MidtransError {
        return new JSONArray((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "channels",
                apiConfig,
                null));
    }

    public static JSONArray getBankAccounts() throws MidtransError {
        return new JSONArray((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "bank_accounts",
                ApiConfig.getDefaultConfig(),
                null));
    }

    public static JSONArray getBankAccounts(ApiConfig apiConfig) throws MidtransError {
        return new JSONArray((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "bank_accounts",
                apiConfig,
                null));
    }

    public static JSONObject getFacilitatorBalance(String bankAccountId) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "bank_account/" + Optional.ofNullable(bankAccountId).orElse("null") + "/balance" + Optional.ofNullable(bankAccountId).orElse("null"),
                ApiConfig.getDefaultConfig(),
                null));
    }

    public static JSONObject getFacilitatorBalance(ApiConfig apiConfig, String bankAccountId) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "bank_account/" + Optional.ofNullable(bankAccountId).orElse("null") + "/balance" + Optional.ofNullable(bankAccountId).orElse("null"),
                apiConfig,
                null));
    }

    public static JSONObject getBeneficiaryBanks() throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "beneficiary_banks",
                ApiConfig.getDefaultConfig(),
                null));
    }

    public static JSONObject getBeneficiaryBanks(ApiConfig apiConfig) throws MidtransError {
        return new JSONObject((String) HttpClient.request(HttpClient.GET,
                irisApiBaseUrl(Midtrans.isProduction()) + "beneficiary_banks",
                apiConfig,
                null));
    }

    public static JSONObject validateBankAccount(String bank, String account) throws MidtransError {
        HttpUrl url = HttpUrl.parse(irisApiBaseUrl(Midtrans.isProduction()) + "account_validation").newBuilder()
                .addQueryParameter("bank", bank)
                .addQueryParameter("account", account)
                .build();

        return new JSONObject((String) HttpClient.request(HttpClient.GET,
                url.toString(),
                ApiConfig.getDefaultConfig(),
                null));
    }

    public static JSONObject validateBankAccount(ApiConfig apiConfig, String bank, String account) throws MidtransError {
        HttpUrl url = HttpUrl.parse(irisApiBaseUrl(Midtrans.isProduction()) + "account_validation").newBuilder()
                .addQueryParameter("bank", bank)
                .addQueryParameter("account", account)
                .build();

        return new JSONObject((String) HttpClient.request(HttpClient.GET,
                url.toString(),
                apiConfig,
                null));
    }

}

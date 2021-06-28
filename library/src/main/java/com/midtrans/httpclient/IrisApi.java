package com.midtrans.httpclient;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import okhttp3.HttpUrl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IrisApi {

    public static String ping() throws MidtransError {
        Config config = Config.builder().setServerKey("NoNeedKey").build();
        return APIHttpClient.request(
                APIHttpClient.GET,
                Config.getGlobalConfig().getIrisApiURL() + "ping",
                config,
                null
        );
    }

    public static JSONObject getBalance(Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.GET,
                configOptions.getIrisApiURL() + "balance",
                configOptions,
                null
        ));
    }

    public static JSONObject createBeneficiaries(Map<String, String> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.POST,
                configOptions.getIrisApiURL() + "beneficiaries",
                configOptions,
                requestBody
        ));
    }

    public static JSONObject updateBeneficiaries(String aliasName, Map<String, String> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(
                APIHttpClient.PATCH,
                configOptions.getIrisApiURL() + "beneficiaries/" + Optional.ofNullable(aliasName).orElse("null"),
                configOptions,
                Optional.ofNullable(requestBody).orElse(new HashMap<>())
        ));
    }

    public static JSONArray getBeneficiaries(Config configOptions) throws MidtransError {
        return new JSONArray((String) APIHttpClient.request(
                APIHttpClient.GET,
                configOptions.getIrisApiURL() + "beneficiaries", configOptions,
                null
        ));
    }

    public static JSONObject createPayouts(Map<String, Object> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.POST,
                configOptions.getIrisApiURL() + "payouts",
                configOptions,
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject approvePayouts(Map<String, Object> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.POST,
                configOptions.getIrisApiURL() + "payouts/approve",
                configOptions,
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject rejectPayouts(Map<String, Object> requestBody, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.POST,
                configOptions.getIrisApiURL() + "payouts/reject",
                configOptions,
                Optional.ofNullable(requestBody).orElse(new HashMap<>())));
    }

    public static JSONObject getPayoutDetails(String referenceNo, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET,
                configOptions.getIrisApiURL() + "payouts/" + Optional.ofNullable(referenceNo).orElse("null"),
                configOptions,
                null));
    }

    public static JSONArray getTransactionHistory(String fromDate, String toDate, Config configOptions) throws MidtransError {
        HttpUrl url = HttpUrl.parse(configOptions.getIrisApiURL() + "statements").newBuilder()
                .addQueryParameter("from_date", fromDate)
                .addQueryParameter("to_date", toDate)
                .build();

        return new JSONArray((String) APIHttpClient.request(APIHttpClient.GET,
                url.toString(),
                configOptions,
                null));
    }

    public static JSONArray getTopUpChannels(Config configOptions) throws MidtransError {
        return new JSONArray((String) APIHttpClient.request(APIHttpClient.GET,
                configOptions.getIrisApiURL() + "channels",
                configOptions,
                null));
    }

    public static JSONArray getBankAccounts(Config configOptions) throws MidtransError {
        return new JSONArray((String) APIHttpClient.request(APIHttpClient.GET,
                configOptions.getIrisApiURL() + "bank_accounts",
                configOptions,
                null));
    }

    public static JSONObject getFacilitatorBalance(String bankAccountId, Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET,
                configOptions.getIrisApiURL() + "bank_account/" + Optional.ofNullable(bankAccountId).orElse("null") + "/balance" + Optional.ofNullable(bankAccountId).orElse("null"),
                configOptions,
                null));
    }

    public static JSONObject getBeneficiaryBanks(Config configOptions) throws MidtransError {
        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET,
                configOptions.getIrisApiURL() + "beneficiary_banks",
                configOptions,
                null));
    }

    public static JSONObject validateBankAccount(String bank, String account, Config configOptions) throws MidtransError {
        HttpUrl url = HttpUrl.parse(configOptions.getIrisApiURL() + "account_validation").newBuilder()
                .addQueryParameter("bank", bank)
                .addQueryParameter("account", account)
                .build();

        return new JSONObject((String) APIHttpClient.request(APIHttpClient.GET,
                url.toString(),
                configOptions,
                null));
    }
}

package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.APIHttpClient;
import com.midtrans.httpclient.IrisApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransIrisApi;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MidtransIrisApiImpl implements MidtransIrisApi {
    private IrisApi irisApi;
    private Config config;

    public MidtransIrisApiImpl(Config config) {
        this.config = config;
        APIHttpClient httpClient = new APIHttpClient(config);
        this.irisApi = httpClient.getClient().create(IrisApi.class);
    }

    private JSONObject httpHandle(Call<ResponseBody> call) throws MidtransError {
        JSONObject object = new JSONObject();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return new JSONObject(response.body().string());
            }
            if (response.errorBody() != null) {
                if (response.code() == 401) {
                    return new JSONObject("{"+response.errorBody().string()+"}");
                }
                object = new JSONObject(response.errorBody().string());
            }
        } catch (Exception e) {
            throw new MidtransError(e);
        }
        return object;
    }

    private JSONArray jsonArrayHttpHandle(Call<ResponseBody> call) throws MidtransError {
        JSONArray result = new JSONArray();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    if (response.body() != null) {
                        result = new JSONArray(response.body().string());
                    }
                } catch (JSONException je) {
                    throw new MidtransError(je);
                }
            }
            if (response.errorBody() != null) {
                if (response.code() == 401) {
                    return new JSONArray("["+"{"+response.errorBody().string()+"}"+"]");
                }
                return new JSONArray("["+"{"+response.errorBody().string()+"}"+"]");
            }
        } catch (Exception e) {
            throw new MidtransError(e);
        }
        return result;
    }

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public String ping() {
        Call<ResponseBody> call = irisApi.ping();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject getBalance() throws MidtransError {
        Call<ResponseBody> call = irisApi.getBalance();
        return httpHandle(call);
    }

    @Override
    public JSONObject createBeneficiaries(Map<String, String> params) throws MidtransError {
        Call<ResponseBody> call = irisApi.createBeneficiaries(
                Optional.ofNullable(params)
                        .orElse(new HashMap<>())
        );
        return httpHandle(call);
    }

    @Override
    public JSONObject updateBeneficiaries(String aliasName, Map<String, String> params) throws MidtransError {
        Call<ResponseBody> call = irisApi.updateBeneficiaries(
                Optional.ofNullable(aliasName)
                        .orElse("null"),
                Optional.ofNullable(params)
                        .orElse(new HashMap<>())
        );
        return httpHandle(call);
    }

    @Override
    public JSONArray getBeneficiaries() throws MidtransError {
        Call<ResponseBody> call = irisApi.getBeneficiaries();
        return jsonArrayHttpHandle(call);
    }

    @Override
    public JSONObject createPayouts(Map<String, Object> params) throws MidtransError {
        Call<ResponseBody> call = irisApi.createPayouts(
                Optional.ofNullable(params)
                        .orElse(new HashMap<>())
        );
        return httpHandle(call);
    }

    @Override
    public JSONObject approvePayouts(Map<String, Object> params) throws MidtransError {
        Call<ResponseBody> call = irisApi.approvePayouts(
                Optional.ofNullable(params)
                        .orElse(new HashMap<>())
        );
        return httpHandle(call);
    }

    @Override
    public JSONObject rejectPayouts(Map<String, Object> params) throws MidtransError {
        Call<ResponseBody> call = irisApi.rejectPayouts(Optional.ofNullable(params)
                .orElse(new HashMap<>())
        );
        return httpHandle(call);
    }

    @Override
    public JSONObject getPayoutDetails(String referenceNo) throws MidtransError {
        Call<ResponseBody> call = irisApi.getPayoutDetails(Optional.ofNullable(referenceNo)
                        .orElse("null")
        );
        return httpHandle(call);
    }

    @Override
    public JSONArray getTransactionHistory(String fromDate, String toDate) throws MidtransError {
        Call<ResponseBody> call = irisApi.getTransactionHistory(fromDate, toDate);
        return jsonArrayHttpHandle(call);
    }

    @Override
    public JSONArray getTopUpChannels() throws MidtransError {
        Call<ResponseBody> call = irisApi.getTopUpChannels();
        return jsonArrayHttpHandle(call);
    }

    @Override
    public JSONArray getBankAccounts() throws MidtransError {
        Call<ResponseBody> call = irisApi.getBankAccount();
        return jsonArrayHttpHandle(call);
    }

    @Override
    public JSONObject getFacilitatorBalance(String bankAccountId) throws MidtransError {
        Call<ResponseBody> call = irisApi.getFacilitatorBalance(bankAccountId);
        return httpHandle(call);
    }

    @Override
    public JSONObject getBeneficiaryBanks() throws MidtransError {
        Call<ResponseBody> call = irisApi.getBeneficiaryBanks();
        return httpHandle(call);    }

    @Override
    public JSONObject validateBankAccount(String bank, String account) throws MidtransError {
        Call<ResponseBody> call = irisApi.validateBankAccount(Optional.ofNullable(bank)
                        .orElse("null"),
                Optional.ofNullable(account)
                        .orElse("null")
        );
        return httpHandle(call);    }

}

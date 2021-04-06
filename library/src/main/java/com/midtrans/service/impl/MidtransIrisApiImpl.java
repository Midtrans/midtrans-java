package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.IrisApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransIrisApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class MidtransIrisApiImpl implements MidtransIrisApi {
    private Config config;

    /**
     * IrisAPI constructor
     *
     * @param config Api Config class
     */
    public MidtransIrisApiImpl(Config config) {
        this.config = config;
    }

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public String ping() throws MidtransError {
        return IrisApi.ping();
    }

    @Override
    public JSONObject getBalance() throws MidtransError {
        return IrisApi.getBalance(config);
    }

    @Override
    public JSONObject createBeneficiaries(Map<String, String> params) throws MidtransError {

        return IrisApi.createBeneficiaries(params, config);
    }

    @Override
    public JSONObject updateBeneficiaries(String aliasName, Map<String, String> params) throws MidtransError {
        return IrisApi.updateBeneficiaries(aliasName, params, config);
    }

    @Override
    public JSONArray getBeneficiaries() throws MidtransError {
        return IrisApi.getBeneficiaries(config);
    }

    @Override
    public JSONObject createPayouts(Map<String, Object> params) throws MidtransError {
        return IrisApi.createPayouts(params, config);
    }

    @Override
    public JSONObject approvePayouts(Map<String, Object> requestBody) throws MidtransError {
        return IrisApi.approvePayouts(requestBody, config);

    }

    @Override
    public JSONObject rejectPayouts(Map<String, Object> requestBody) throws MidtransError {
        return IrisApi.rejectPayouts(requestBody, config);
    }

    @Override
    public JSONObject getPayoutDetails(String referenceNo) throws MidtransError {
        return IrisApi.getPayoutDetails(referenceNo, config);
    }

    @Override
    public JSONArray getTransactionHistory(String fromDate, String toDate) throws MidtransError {
        return IrisApi.getTransactionHistory(fromDate, toDate, config);
    }

    @Override
    public JSONArray getTopUpChannels() throws MidtransError {
        return IrisApi.getTopUpChannels(config);
    }

    @Override
    public JSONArray getBankAccounts() throws MidtransError {
        return IrisApi.getBankAccounts(config);
    }

    @Override
    public JSONObject getFacilitatorBalance(String bankAccountId) throws MidtransError {
        return IrisApi.getFacilitatorBalance(bankAccountId, config);
    }

    @Override
    public JSONObject getBeneficiaryBanks() throws MidtransError {
        return IrisApi.getBeneficiaryBanks(config);
    }

    @Override
    public JSONObject validateBankAccount(String bank, String account) throws MidtransError {
        return IrisApi.validateBankAccount(bank, account, config);
    }

}

package com.midtrans.java.v2;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.IrisApi;
import com.midtrans.v2.gateway.http.ApiConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.approverKey;
import static com.midtrans.java.mockupdata.Constant.creatorKey;
import static com.midtrans.java.mockupdata.DataMockup.refNumber;


public class IrisApiV2Test {
    private DataMockup dataMockup;
    private Map<String, String> beneficiaries = new HashMap<>();
    private ApiConfig apiConfig;


    @Before
    public void setUp() {
        dataMockup = new DataMockup();
        apiConfig = ApiConfig.builder()
                .setServerKey(creatorKey)
                .build();
        Midtrans.isProduction = false;
        Midtrans.enableLog = true;
    }

    @Test
    public void baseUrl() {
        String sandboxBaseUrl = IrisApi.irisApiBaseUrl(false);
        assert sandboxBaseUrl.equals(Midtrans.getIrisSandboxBaseUrl());

        String productionBaseUrl = IrisApi.irisApiBaseUrl(true);
        assert productionBaseUrl.equals(Midtrans.getIrisProductionBaseUrl());
    }

    @Test
    public void ping() throws MidtransError {
        String result = IrisApi.ping();
        assert result.equals("pong");
    }

    @Test
    public void getAggregatorBalance() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        JSONObject result = IrisApi.getBalance();
        assert result.has("balance");

        JSONObject result2 = IrisApi.getBalance(apiConfig);
        assert result2.has("balance");
    }

    @Test
    public void createUpdateGetBeneficiaries() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        beneficiaries = dataMockup.initDataBeneficiaries();
        JSONObject result = IrisApi.createBeneficiaries(beneficiaries);
        assert result.has("status");
        assert result.getString("status").equals("created");


        beneficiaries = dataMockup.initDataBeneficiaries();
        JSONObject result2 = IrisApi.createBeneficiaries(apiConfig, beneficiaries);
        assert result2.has("status");
        assert result2.getString("status").equals("created");


        getListBeneficiaries();
        updateBeneficiaries();
    }

    private void getListBeneficiaries() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        JSONArray result = IrisApi.getBeneficiaries();
        JSONObject jsonResult = new JSONObject(result.get(result.length() - 1).toString());
        assert jsonResult.getString("alias_name").equals(beneficiaries.get("alias_name"));


        JSONArray result2 = IrisApi.getBeneficiaries(apiConfig);
        JSONObject jsonResult2 = new JSONObject(result2.get(result2.length() - 1).toString());
        assert jsonResult2.getString("alias_name").equals(beneficiaries.get("alias_name"));
    }

    private void updateBeneficiaries() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        String oldAliasName = beneficiaries.get("alias_name");
        beneficiaries.replace("alias_name", oldAliasName, oldAliasName + "edt");
        JSONObject result = IrisApi.updateBeneficiaries(oldAliasName, beneficiaries);
        assert result.getString("status").equals("updated");
    }


    @Test
    public void createAndApprovePayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries();

        /*
        Request payout to Iris API
         */
        Midtrans.serverKey = creatorKey;
        JSONObject response = IrisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries));
        assert response.has("payouts");

        JSONArray responsePayouts = response.getJSONArray("payouts");
        responsePayouts.get(0);

        ArrayList<String> referenceNos = new ArrayList<>();
        referenceNos.add(responsePayouts.getJSONObject(0).getString("reference_no"));

        /*
        Approve payouts to Iris API
         */
        Map<String, Object> approvePayout = new HashMap<>();
        approvePayout.put("reference_nos", referenceNos);
        approvePayout.put("otp", "335163");
        approvePayouts(approvePayout);
    }

    private void approvePayouts(Map<String, Object> params) throws MidtransError {
        Midtrans.serverKey = approverKey;
        JSONObject result = IrisApi.approvePayouts(params);
        assert result.getString("status").equals("ok");
    }

    @Test
    public void createAndRejectPayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries();

        Midtrans.serverKey = creatorKey;

        /*
        Request payout to Iris API
         */
        JSONObject response = IrisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries));
        assert response.has("payouts");

        JSONArray responsePayouts = response.getJSONArray("payouts");
        responsePayouts.get(0);

        ArrayList<String> referenceNos = new ArrayList<>();
        referenceNos.add(responsePayouts.getJSONObject(0).getString("reference_no"));

        /*
        Reject payouts to Iris API
         */
        Map<String, Object> rejectPassword = new HashMap<>();
        rejectPassword.put("reference_nos", referenceNos);
        rejectPassword.put("reject_reason", "Unit test reject MidJavaLib");
        rejectPayouts(rejectPassword);
    }

    private void rejectPayouts(Map<String, Object> params) throws MidtransError {
        Midtrans.serverKey = approverKey;
        JSONObject result = IrisApi.rejectPayouts(params);
        assert result.getString("status").equals("ok");
    }

    @Test
    public void getTransactionHistory() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        LocalDate localDate = LocalDate.now();
        String fromDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
        String toDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
        JSONArray arrayResult = IrisApi.getTransactionHistory(fromDate, toDate);
        assert !arrayResult.isEmpty();
    }

    @Test
    public void getTopUpChannels() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        JSONArray result = IrisApi.getTopUpChannels();
        JSONObject jsonResult = new JSONObject(result.get(0).toString());
        assert jsonResult.getString("virtual_account_type").equals("mandiri_bill_key");
    }

    @Test
    public void getBeneficiaryBanks() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        JSONObject result = IrisApi.getBeneficiaryBanks();
        JSONArray beneficiaryBanks = result.getJSONArray("beneficiary_banks");
        JSONObject arrayList = null;
        for (int i = 0; i < beneficiaryBanks.length(); i++) {
            arrayList = (JSONObject) beneficiaryBanks.get(i);
            if (arrayList.getString("code").equals("bca")) {
                break;
            }
        }
        assert arrayList != null;
        assert arrayList.getString("code").equals("bca");
    }

    @Test
    public void validateBankAccount() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        JSONObject result = IrisApi.validateBankAccount("danamon", "000001137298");
        assert result.getString("account_no").equals("000001137298");
    }

    @Test
    public void createBeneficiaryWithIdempotencyKey() throws MidtransError {
        Map beneficiary = dataMockup.initDataBeneficiaries();

        // Request create beneficiary to Iris API
        ApiConfig apiConfig = ApiConfig.builder()
                .setServerKey(creatorKey)
                .setIrisIdempotencykey(String.valueOf(UUID.randomUUID()))
                .build();
        JSONObject result1 = IrisApi.createBeneficiaries(apiConfig, beneficiary);
        assert result1.getString("status").equals("created");

        JSONObject result2 = IrisApi.createBeneficiaries(apiConfig, beneficiary);
        assert result2.getString("status").equals("created");
    }

    @Test
    public void failureCredentials() {
        Midtrans.serverKey = "1";
        try {
            IrisApi.getBalance();
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getResponseBody().equals("HTTP Basic: Access denied.\n");
        }


        try {
            IrisApi.getBalance(null);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getResponseBody().equals("HTTP Basic: Access denied.\n");
        }
    }

    @Test
    public void failureGetPayoutsDetails() {
        Midtrans.serverKey = creatorKey;
        JSONObject result = null;
        try {
            result = IrisApi.getPayoutDetails(refNumber);
            assert result.getString("error_message").equals("Specified payout not found");
        } catch (MidtransError MidtransError) {
            MidtransError.printStackTrace();
        }
    }

    private JSONObject getRandomBeneficiaries() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        JSONArray result = IrisApi.getBeneficiaries();
        return new JSONObject(result.get(result.length() - 2).toString());
    }

}
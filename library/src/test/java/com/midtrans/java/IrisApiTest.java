package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.IrisApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
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


public class IrisApiTest {
    private DataMockup dataMockup;
    private Map<String, String> beneficiaries = new HashMap<>();
    private Config configOptions;


    @Before
    public void setUp() {
        dataMockup = new DataMockup();

        configOptions = Config.builder()
                .setSERVER_KEY(creatorKey)
                .enableLog(false)
                .build();
    }

    @Test
    public void ping() throws MidtransError {
        String result = IrisApi.ping();
        assert result.equals("pong");
    }

    @Test
    public void getAggregatorBalance() throws MidtransError {
        JSONObject result = IrisApi.getBalance(configOptions);
        assert result.has("balance");
    }

    @Test
    public void createUpdateGetBeneficiaries() throws MidtransError {
        beneficiaries = dataMockup.initDataBeneficiaries();

        /* Using method with Global Config */
        JSONObject result = IrisApi.createBeneficiaries(beneficiaries, configOptions);
        assert result.has("status");
        assert result.getString("status").equals("created");
        getListAndUpdateBeneficiaries(configOptions);

    }

    private void getListAndUpdateBeneficiaries(Config config) throws MidtransError {
        JSONArray result2 = IrisApi.getBeneficiaries(config);
        JSONObject jsonResult2 = new JSONObject(result2.get(result2.length() - 1).toString());
        assert jsonResult2.getString("alias_name").equals(beneficiaries.get("alias_name"));
        String oldAliasName = beneficiaries.get("alias_name");
        updateBeneficiaries(oldAliasName, config);
    }

    private void updateBeneficiaries(String oldAliasName, Config config) throws MidtransError {
        beneficiaries.replace("alias_name", oldAliasName, oldAliasName + "edt");
        JSONObject result = IrisApi.updateBeneficiaries(oldAliasName, beneficiaries, config);
        assert result.getString("status").equals("updated");
    }


    @Test
    public void createAndApprovePayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries(configOptions);


        JSONObject response = IrisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries), configOptions);
        assert response.has("payouts");

        /*
        Approve payouts to Iris API
         */
        Map<String, Object> approvePayout = new HashMap<>();
        approvePayout.put("reference_nos", getPayoutsFromResult(response));
        approvePayout.put("otp", "335163");

        configOptions.setSERVER_KEY(approverKey);
        JSONObject result = IrisApi.approvePayouts(approvePayout, configOptions);
        assert result.getString("status").equals("ok");
    }

    private ArrayList<String> getPayoutsFromResult(JSONObject response) {
        JSONArray responsePayouts = response.getJSONArray("payouts");
        responsePayouts.get(0);

        ArrayList<String> referenceNos = new ArrayList<>();
        referenceNos.add(responsePayouts.getJSONObject(0).getString("reference_no"));
        return referenceNos;
    }

    @Test
    public void createAndRejectPayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries(configOptions);

        Midtrans.serverKey = creatorKey;

        /*
        Request payout to Iris API
         */
        JSONObject response = IrisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries), configOptions);
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
        configOptions.setSERVER_KEY(approverKey);
        JSONObject result = IrisApi.rejectPayouts(params, configOptions);
        assert result.getString("status").equals("ok");
    }

    @Test
    public void getTransactionHistory() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        LocalDate localDate = LocalDate.now();
        String fromDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate.minusMonths(1));
        String toDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
        JSONArray arrayResult = IrisApi.getTransactionHistory(fromDate, toDate, configOptions);
        assert !arrayResult.isEmpty();
    }

    @Test
    public void getTopUpChannels() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        JSONArray result = IrisApi.getTopUpChannels(configOptions);
        JSONObject jsonResult = new JSONObject(result.get(0).toString());
        assert jsonResult.getString("virtual_account_type").equals("mandiri_bill_key");
    }

    @Test
    public void getBeneficiaryBanks() throws MidtransError {
        JSONObject result = IrisApi.getBeneficiaryBanks(configOptions);
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
        JSONObject result = IrisApi.validateBankAccount("danamon", "000001137298", configOptions);
        assert result.getString("account_no").equals("000001137298");
    }

    @Test
    public void createBeneficiaryWithIdempotencyKey() throws MidtransError {
        Map beneficiary = dataMockup.initDataBeneficiaries();

        // Request create beneficiary to Iris API
        Config configOptions = Config.builder()
                .setSERVER_KEY(creatorKey)
                .setIrisIdempotencyKey(String.valueOf(UUID.randomUUID()))
                .build();
        JSONObject result1 = IrisApi.createBeneficiaries(beneficiary, configOptions);
        assert result1.getString("status").equals("created");

        JSONObject result2 = IrisApi.createBeneficiaries(beneficiary, configOptions);
        assert result2.getString("status").equals("created");
    }

    @Test
    public void failureCredentials() {
        configOptions.setSERVER_KEY("dummy");
        try {
            IrisApi.getBalance(configOptions);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getResponseBody().equals("HTTP Basic: Access denied.\n");
        }
    }

    @Test
    public void failureGetPayoutsDetails() {
        JSONObject result;
        try {
            result = IrisApi.getPayoutDetails(refNumber, configOptions);
            assert result.getString("error_message").equals("Specified payout not found");
        } catch (MidtransError MidtransError) {
            MidtransError.printStackTrace();
        }
    }

    private JSONObject getRandomBeneficiaries(Config config) throws MidtransError {
        JSONArray result = IrisApi.getBeneficiaries(config);
        return new JSONObject(result.get(result.length() - 2).toString());
    }

}
package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.service.MidtransIrisApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.midtrans.java.mockupdata.Constant.approverKey;
import static com.midtrans.java.mockupdata.Constant.creatorKey;
import static com.midtrans.java.mockupdata.DataMockup.refNumber;


public class MidtransIrisApiTest {
    MidtransIrisApi irisApi;
    DataMockup dataMockup;
    Map<String, String> beneficiaries = new HashMap<>();


    @Before
    public void setUp() {
        dataMockup = new DataMockup();
        irisApi = new ConfigFactory(new Config(creatorKey, null, false)).getIrisApi();
    }

    @Test
    public void ping() throws MidtransError {
        String result = irisApi.ping();
        assert result.equals("pong");
    }

    @Test
    public void getAggregatorBalance() throws MidtransError {
        JSONObject result = irisApi.getBalance();
        assert result.has("balance");
    }

    @Test
    public void createUpdateGetBeneficiaries() throws MidtransError {
        beneficiaries = dataMockup.initDataBeneficiaries();
        JSONObject result = irisApi.createBeneficiaries(beneficiaries);
        assert result.has("status");
        assert result.getString("status").equals("created");

        getListBeneficiaries();
        updateBeneficiaries();
    }

    private void updateBeneficiaries() throws MidtransError {
        String oldAliasName = beneficiaries.get("alias_name");
        beneficiaries.replace("alias_name", oldAliasName, oldAliasName + "edt");
        JSONObject result = irisApi.updateBeneficiaries(oldAliasName, beneficiaries);
        assert result.getString("status").equals("updated");
    }

    private void getListBeneficiaries() throws MidtransError {
        JSONArray result = irisApi.getBeneficiaries();
        JSONObject jsonResult = new JSONObject(result.get(result.length() - 1).toString());
        assert jsonResult.getString("alias_name").equals(beneficiaries.get("alias_name"));
    }


    @Test
    public void createAndApprovePayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries();

        /*
        Request payout to Iris API
         */
        JSONObject response = irisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries));
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
        irisApi.apiConfig().setServerKey(approverKey);
        JSONObject result = irisApi.approvePayouts(params);
        assert result.getString("status").equals("ok");
    }

    @Test
    public void createAndRejectPayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries();

        /*
        Request payout to Iris API
         */
        JSONObject response = irisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries));
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
        irisApi.apiConfig().setServerKey(approverKey);
        JSONObject result = irisApi.rejectPayouts(params);
        assert result.getString("status").equals("ok");
    }

    @Test
    public void getTransactionHistory() throws MidtransError {
        LocalDate localDate = LocalDate.now();
        String fromDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate.minusMonths(1));
        String toDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
        JSONArray arrayResult = irisApi.getTransactionHistory(fromDate, toDate);
        assert !arrayResult.isEmpty();
    }

    @Test
    public void getTopUpChannels() throws MidtransError {
        JSONArray result = irisApi.getTopUpChannels();
        JSONObject jsonResult = new JSONObject(result.get(0).toString());
        assert jsonResult.getString("virtual_account_type").equals("mandiri_bill_key");
    }

    @Test
    public void getBeneficiaryBanks() throws MidtransError {
        JSONObject result = irisApi.getBeneficiaryBanks();
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
        JSONObject result = irisApi.validateBankAccount("danamon", "000001137298");
        assert result.getString("account_no").equals("000001137298");
    }

    @Test
    public void createBeneficiaryWithIdempotencyKey() throws MidtransError {
        Map beneficiary = dataMockup.initDataBeneficiaries();

        // Use idempotency key
        irisApi.apiConfig().setIrisIdempotencyKey("Iris-Idempotency-Key" + dataMockup.random());

        // Request create beneficiary to Iris API
        JSONObject result1 = irisApi.createBeneficiaries(beneficiary);
        assert result1.getString("status").equals("created");

        JSONObject result2 = irisApi.createBeneficiaries(beneficiary);
        assert result2.getString("status").equals("created");
    }

    @Test
    public void failureCredentials()  {
        irisApi.apiConfig().setServerKey("dummy");
        try {
            JSONObject result = irisApi.getBalance();
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getResponseBody().contains("Access denied");
        }
    }

    @Test
    public void failureGetPayoutsDetails() {
        irisApi.apiConfig().setServerKey(approverKey);
        try {
            irisApi.getPayoutDetails(refNumber);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("404");
        }
    }

    private JSONObject getRandomBeneficiaries() throws MidtransError {
        JSONArray result = irisApi.getBeneficiaries();
        return new JSONObject(result.get(result.length() - 2).toString());
    }

}
package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.IrisApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.approverKey;
import static com.midtrans.java.mockupdata.Constant.creatorKey;
import static com.midtrans.java.mockupdata.DataMockup.refNumber;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IrisApiTest {
    private static DataMockup dataMockup;
    private static Map<String, String> beneficiaries = new HashMap<>();
    private static Config configOptions;


    @BeforeAll
    public static void setUp() {
        dataMockup = new DataMockup();

        configOptions = Config.builder()
                .setServerKey(creatorKey)
                .enableLog(false)
                .build();
    }

    @Test
    @Order(2)
    public void ping() throws MidtransError {
        String result = IrisApi.ping();
        assertEquals("pong", result);
    }

    @Test
    @Order(3)
    public void getAggregatorBalance() throws MidtransError {
        JSONObject result = IrisApi.getBalance(configOptions);
        assertTrue(result.has("balance"));
    }

    @Test
    @Order(4)
    public void createUpdateGetBeneficiaries() throws MidtransError {
        beneficiaries = dataMockup.initDataBeneficiaries();

        /* Using method with Global Config */
        JSONObject result = IrisApi.createBeneficiaries(beneficiaries, configOptions);
        assertTrue(result.has("status"));
        assertEquals("created", result.getString("status"));
        getListAndUpdateBeneficiaries(configOptions);

    }

    private void getListAndUpdateBeneficiaries(Config config) throws MidtransError {
        JSONArray result2 = IrisApi.getBeneficiaries(config);
        JSONObject jsonResult2 = new JSONObject(result2.get(result2.length() - 1).toString());
        assertEquals(beneficiaries.get("alias_name"), jsonResult2.getString("alias_name"));
        String oldAliasName = beneficiaries.get("alias_name");
        updateBeneficiaries(oldAliasName, config);
    }

    private void updateBeneficiaries(String oldAliasName, Config config) throws MidtransError {
        beneficiaries.replace("alias_name", oldAliasName, oldAliasName + "edt");
        JSONObject result = IrisApi.updateBeneficiaries(oldAliasName, beneficiaries, config);
        assertEquals("updated", result.getString("status"));
    }


    @Test
    @Order(5)
    public void createAndApprovePayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries(configOptions);


        JSONObject response = IrisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries), configOptions);
        assertTrue(response.has("payouts"));

        /*
        Approve payouts to Iris API
         */
        Map<String, Object> approvePayout = new HashMap<>();
        approvePayout.put("reference_nos", getPayoutsFromResult(response));
        approvePayout.put("otp", "335163");

        configOptions.setServerKey(approverKey);
        JSONObject result = IrisApi.approvePayouts(approvePayout, configOptions);
        assertEquals("ok", result.getString("status"));
    }

    private ArrayList<String> getPayoutsFromResult(JSONObject response) {
        JSONArray responsePayouts = response.getJSONArray("payouts");
        responsePayouts.get(0);

        ArrayList<String> referenceNos = new ArrayList<>();
        referenceNos.add(responsePayouts.getJSONObject(0).getString("reference_no"));
        return referenceNos;
    }

    @Test
    @Order(6)
    public void createAndRejectPayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries(configOptions);

        Midtrans.serverKey = creatorKey;

        /*
        Request payout to Iris API
         */
        JSONObject response = IrisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries), configOptions);
        assertTrue(response.has("payouts"));

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
        configOptions.setServerKey(approverKey);
        JSONObject result = IrisApi.rejectPayouts(params, configOptions);
        assertEquals("ok", result.getString("status"));
    }

    @Test
    @Order(7)
    public void getTransactionHistory() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        LocalDate localDate = LocalDate.now();
        String fromDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate.minusMonths(1));
        String toDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
        JSONArray arrayResult = IrisApi.getTransactionHistory(fromDate, toDate, configOptions);
        assertFalse(arrayResult.isEmpty());
    }

    @Test
    @Order(8)
    public void getTopUpChannels() throws MidtransError {
        Midtrans.serverKey = creatorKey;
        JSONArray result = IrisApi.getTopUpChannels(configOptions);
        JSONObject jsonResult = new JSONObject(result.get(0).toString());
        assertEquals("mandiri_bill_key", jsonResult.getString("virtual_account_type"));
    }

    @Test
    @Order(9)
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
        assertNotNull(arrayList);
        assertEquals("bca", arrayList.getString("code"));
    }

    @Test
    @Order(10)
    public void validateBankAccount() throws MidtransError {
        JSONObject result = IrisApi.validateBankAccount("mandiri", "1111222233333", configOptions);
        assertEquals("1111222233333", result.getString("account_no"));
    }

    @Test
    @Order(11)
    public void createBeneficiaryWithIdempotencyKey() throws MidtransError {
        Map beneficiary = dataMockup.initDataBeneficiaries();

        // Request create beneficiary to Iris API
        Config configOptions = Config.builder()
                .setServerKey(creatorKey)
                .setIrisIdempotencyKey(String.valueOf(UUID.randomUUID()))
                .build();
        JSONObject result1 = IrisApi.createBeneficiaries(beneficiary, configOptions);
        assertEquals("created", result1.getString("status"));

        JSONObject result2 = IrisApi.createBeneficiaries(beneficiary, configOptions);
        assertEquals("created", result2.getString("status"));

    }

    @Test
    @Order(12)
    public void failureCredentials() {
        configOptions.setServerKey("dummy");
        try {
            IrisApi.getBalance(configOptions);
        } catch (MidtransError e) {
            e.printStackTrace();
            assertTrue(e.getResponseBody().contains("Access denied"));
        }
    }

    @Test
    @Order(13)
    public void failureGetPayoutsDetails() {
        JSONObject result;
        try {
            result = IrisApi.getPayoutDetails(refNumber, configOptions);
            assertEquals("pecified payout not found", result.getString("error_message"));
        } catch (MidtransError MidtransError) {
            MidtransError.printStackTrace();
        }
    }

    private JSONObject getRandomBeneficiaries(Config config) throws MidtransError {
        configOptions.setServerKey(creatorKey);
        JSONArray result = IrisApi.getBeneficiaries(config);
        return new JSONObject(result.get(result.length() - 2).toString());
    }

}
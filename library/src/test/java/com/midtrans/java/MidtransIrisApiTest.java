package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.service.MidtransIrisApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.midtrans.java.mockupdata.Constant.approverKey;
import static com.midtrans.java.mockupdata.Constant.creatorKey;
import static com.midtrans.java.mockupdata.DataMockup.refNumber;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MidtransIrisApiTest {
    private static MidtransIrisApi irisApi;
    private static DataMockup dataMockup;
    private static Map<String, String> beneficiaries = new HashMap<>();


    @BeforeAll
    public static void setUp() {
        dataMockup = new DataMockup();
        Config irisConfig = Config.builder().setServerKey(creatorKey).setIsProduction(false).build();
        irisApi = new ConfigFactory(irisConfig).getIrisApi();
    }

    @Test
    @Order(2)
    public void ping() throws MidtransError {
        String result = irisApi.ping();
        assertEquals("pong", result);
    }

    @Test
    @Order(3)
    public void getAggregatorBalance() throws MidtransError {
        JSONObject result = irisApi.getBalance();
        assertTrue(result.has("balance"));
    }

    @Test
    @Order(4)
    public void createUpdateGetBeneficiaries() throws MidtransError {
        beneficiaries = dataMockup.initDataBeneficiaries();
        JSONObject result = irisApi.createBeneficiaries(beneficiaries);
        assertTrue(result.has("status"));
        assertEquals("created", result.getString("status"));

        getListBeneficiaries();
        updateBeneficiaries();
    }

    private void updateBeneficiaries() throws MidtransError {
        String oldAliasName = beneficiaries.get("alias_name");
        beneficiaries.replace("alias_name", oldAliasName, oldAliasName + "edt");
        JSONObject result = irisApi.updateBeneficiaries(oldAliasName, beneficiaries);
        assertEquals("updated", result.getString("status"));
    }

    private void getListBeneficiaries() throws MidtransError {
        JSONArray result = irisApi.getBeneficiaries();
        JSONObject jsonResult = new JSONObject(result.get(result.length() - 1).toString());
        assertEquals(beneficiaries.get("alias_name"), jsonResult.getString("alias_name"));
    }


    @Test
    @Order(5)
    public void createAndApprovePayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries();

        /*
        Request payout to Iris API
         */
        JSONObject response = irisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries));
        assertTrue(response.has("payouts"));

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
        assertEquals("ok", result.getString("status"));
    }

    @Test
    @Order(6)
    public void createAndRejectPayouts() throws MidtransError {
        JSONObject beneficiaries = getRandomBeneficiaries();

        /*
        Request payout to Iris API
         */
        irisApi.apiConfig().setServerKey(creatorKey);
        JSONObject response = irisApi.createPayouts(dataMockup.initDataRequestPayout(beneficiaries));
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
        irisApi.apiConfig().setServerKey(approverKey);
        JSONObject result = irisApi.rejectPayouts(params);
        assertEquals("ok", result.getString("status"));
    }

    @Test
    @Order(7)
    public void getTransactionHistory() throws MidtransError {
        LocalDate localDate = LocalDate.now();
        String fromDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate.minusMonths(1));
        String toDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
        JSONArray arrayResult = irisApi.getTransactionHistory(fromDate, toDate);
        assertFalse(arrayResult.isEmpty());
    }

    @Test
    @Order(8)
    public void getTopUpChannels() throws MidtransError {
        JSONArray result = irisApi.getTopUpChannels();
        JSONObject jsonResult = new JSONObject(result.get(0).toString());
        assertEquals("mandiri_bill_key", jsonResult.getString("virtual_account_type"));
    }

    @Test
    @Order(9)
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
        assertNotNull(arrayList);
        assertEquals("bca", arrayList.getString("code"));
    }

    @Test
    @Order(10)
    public void validateBankAccount() throws MidtransError {
        JSONObject result = irisApi.validateBankAccount("mandiri", "1111222233333");
        assertEquals("1111222233333", result.getString("account_no"));
    }

    @Test
    @Order(11)
    public void createBeneficiaryWithIdempotencyKey() throws MidtransError {
        Map beneficiary = dataMockup.initDataBeneficiaries();

        // Use idempotency key
        irisApi.apiConfig().setIrisIdempotencyKey("Iris-Idempotency-Key" + dataMockup.random());

        // Request create beneficiary to Iris API
        JSONObject result1 = irisApi.createBeneficiaries(beneficiary);
        assertEquals("created", result1.getString("status"));


        JSONObject result2 = irisApi.createBeneficiaries(beneficiary);
        assertEquals("created", result2.getString("status"));
    }

    @Test
    @Order(12)
    public void failureCredentials() {
        irisApi.apiConfig().setServerKey("dummy");
        try {
            irisApi.getBalance();
        } catch (MidtransError e) {
            e.printStackTrace();
            assertTrue(e.getResponseBody().contains("Access denied"));
        }
    }

    @Test
    @Order(13)
    public void failureGetPayoutsDetails() {
        irisApi.apiConfig().setServerKey(approverKey);
        try {
            irisApi.getPayoutDetails(refNumber);
        } catch (MidtransError e) {
            e.printStackTrace();
            assertTrue(e.getMessage().contains("404"));
        }
    }

    private JSONObject getRandomBeneficiaries() throws MidtransError {
        JSONArray result = irisApi.getBeneficiaries();
        return new JSONObject(result.get(result.length() - 2).toString());
    }

}
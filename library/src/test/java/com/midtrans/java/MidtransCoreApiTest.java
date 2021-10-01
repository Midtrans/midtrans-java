package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.service.MidtransCoreApi;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Nested
public class MidtransCoreApiTest {

    private static MidtransCoreApi coreApi;
    private static DataMockup dataMockup;
    private static Config configOptions;

    private static String subscriptionName;
    private static String subscriptionId;

    private static String accountId;

    @BeforeAll
    public static void setUp() {
        configOptions = Config.builder()
                .setIsProduction(false)
                .setServerKey(mainServerKey)
                .setClientKey(mainClientKey)
                .build();

        coreApi = new ConfigFactory(configOptions).getCoreApi();
        dataMockup = new DataMockup();
    }

    @Test
    @Order(1)
    public void registerCard() throws MidtransError {
        JSONObject result = coreApi.registerCard(DataMockup.creditCard(cardNumberAccept));
        assertEquals("200", result.getString("status_code"));
        assertEquals(cardNumberAccept.substring(0, 6), result.getString("saved_token_id").substring(0, 6));
    }

    @Test
    @Order(2)
    public void cardToken() throws MidtransError {
        JSONObject result = coreApi.cardToken(DataMockup.creditCard(cardNumberAccept));
        assertTrue(result.has("token_id"));
        assertNotNull(result.getString("token_id"));
        assertEquals("200", result.getString("status_code"));
    }

    @Test
    @Order(3)
    public void chargeCreditCardTransaction() throws MidtransError {
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        cc.put("token_id", DataMockup.genCardToken(cardNumberAccept, configOptions));
        dataMockup.creditCard(cc);

        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        assertEquals("credit_card", result.getString("payment_type"));
    }

    @Test
    @Order(4)
    public void failChargeWithEmptyBody() {
        try {
            coreApi.chargeTransaction(null);
        } catch (MidtransError e) {
            assertTrue(e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 500 API response: {\"message\":\"An unexpected error occurred\"}"));
        }
    }

    @Test
    @Order(5)
    public void chargeGoPayTransaction() throws MidtransError {
        dataMockup.setPaymentType("gopay");
        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        assertEquals("201", result.getString("status_code"));
        assertEquals("gopay", result.getString("payment_type"));
    }

    @Test
    @Order(6)
    public void cardPointInquiry() throws MidtransError {
        JSONObject result = coreApi.cardPointInquiry(DataMockup.genCardToken(bniCardNumber, configOptions));
        assertEquals("200", result.getString("status_code"));
        assertEquals("Success, Credit Card Point inquiry is successful", result.getString("status_message"));
    }

    @Test
    @Order(7)
    public void orderIdNullCheckTransaction() {
        try {
            coreApi.checkTransaction("null");
        } catch (MidtransError midtransError) {
            assertEquals(404, midtransError.getStatusCode());
        }
    }

    @Test
    @Order(8)
    public void checkTransaction() throws MidtransError {
        JSONObject result = coreApi.checkTransaction(DataMockup.makeTransaction(configOptions));
        assertEquals("201", result.getString("status_code"));
        assertEquals("Success, transaction is found", result.getString("status_message"));
    }

    @Test
    @Order(9)
    public void approveTransaction() throws MidtransError {
        JSONObject result = coreApi.approveTransaction(DataMockup.makeFDSTransaction(configOptions));
        assertEquals("200", result.getString("status_code"));
        assertEquals("Success, transaction is approved", result.getString("status_message"));
    }

    @Test
    @Order(10)
    public void denyTransaction() throws MidtransError {
        JSONObject result = coreApi.denyTransaction(DataMockup.makeFDSTransaction(configOptions));
        assertEquals("200", result.getString("status_code"));
        assertEquals("Success, transaction is denied", result.getString("status_message"));
    }

    @Test
    @Order(11)
    public void cancelTransaction() throws MidtransError {
        JSONObject result = coreApi.cancelTransaction(DataMockup.makeTransaction(configOptions));
        assertEquals("200", result.getString("status_code"));
        assertEquals("Success, transaction is canceled", result.getString("status_message"));
    }

    @Test
    @Order(12)
    public void expireTransaction() throws MidtransError {
        JSONObject result = coreApi.expireTransaction(DataMockup.makeTransaction(configOptions));
        assertEquals("407", result.getString("status_code"));
        assertEquals("Success, transaction is expired", result.getString("status_message"));
    }

    @Test
    @Order(13)
    public void refundTransaction() {
        Map<String, String> refundBody = new HashMap<>();
        refundBody.put("amount", "265000");
        refundBody.put("reason", "Product is out of stock, payment is being refunded");

        try {
            coreApi.refundTransaction(DataMockup.makeTransaction(configOptions), refundBody);
        } catch (MidtransError midtransError) {
            assertEquals(412, midtransError.getStatusCode());
        }
    }

    @Test
    @Order(14)
    public void captureTransaction() {
        UUID idRandom = UUID.randomUUID();
        Map<String, String> params = new HashMap<>();

        params.put("transaction_id", idRandom.toString());
        params.put("gross_amount", "265.000");
        try {
            coreApi.captureTransaction(params);
        } catch (MidtransError midtransError) {
            assertEquals(404, midtransError.getStatusCode());
        }
    }

    @Test
    @Order(15)
    public void getStatusB2BTransaction() {
        try {
            coreApi.getTransactionStatusB2B(DataMockup.makeTransaction(configOptions));
        } catch (MidtransError midtransError) {
            assertEquals(404, midtransError.getStatusCode());
        }
    }

    @Test
    @Order(16)
    public void directRefund() {
        UUID stringRand = UUID.randomUUID();

        Map<String, String> params = new HashMap<>();
        params.put("refund_key", stringRand.toString());
        params.put("amount", "265000");
        params.put("reason", "Test direct refund");

        try {
            coreApi.directRefundTransaction(DataMockup.makeTransaction(configOptions), params);
        } catch (MidtransError midtransError) {
            assertEquals(412, midtransError.getStatusCode());
        }
    }

    @Test
    @Order(17)
    public void createSubscription() throws MidtransError {
        subscriptionName = "MID-JAVA-SUBS1" + dataMockup.random();
        Map<String, Object> request1 = dataMockup.subscriptionRequest(subscriptionName, 1, "month");
        JSONObject result1 = coreApi.createSubscription(request1);
        assertEquals(subscriptionName, result1.getString("name"));
        subscriptionId = result1.getString("id");
    }

    @Test
    @Order(18)
    public void disableSubscription() throws MidtransError {
        JSONObject result1 = coreApi.disableSubscription(subscriptionId);
        assertEquals("Subscription is updated.", result1.getString("status_message"));
    }

    @Test
    @Order(19)
    public void enableSubscription() throws MidtransError {
        JSONObject result1 = coreApi.enableSubscription(subscriptionId);
        assertEquals("Subscription is updated.", result1.getString("status_message"));
    }

    @Test
    @Order(20)
    public void updateSubscription() throws MidtransError {
        Map<String, Object> request1 = dataMockup.subscriptionRequest(subscriptionName, 2, "month");
        JSONObject result1 = coreApi.updateSubscription(subscriptionId, request1);
        assertEquals("Subscription is updated.", result1.getString("status_message"));
    }

    @Test
    @Order(21)
    public void getSubscription() throws MidtransError {
        JSONObject result1 = coreApi.getSubscription(subscriptionId);
        assertEquals(subscriptionName, result1.getString("name"));
    }

    @Test
    @Order(22)
    public void linkPaymentAccount() throws MidtransError {
        String json = "{\n" +
                "  \"payment_type\": \"gopay\",\n" +
                "  \"gopay_partner\": {\n" +
                "    \"phone_number\": \"81212345678\",\n" +
                "    \"country_code\": \"62\",\n" +
                "    \"redirect_url\": \"https://www.gojek.com\"\n" +
                "  }\n" +
                "}";
        Map<String, Object> request = dataMockup.jsonToMap(json);

        JSONObject result1 = coreApi.linkPaymentAccount(request);
        assertEquals("201", result1.getString("status_code"));
        accountId = result1.getString("account_id");
    }

    @Test
    @Order(23)
    public void getPaymentAccount() throws MidtransError {
        JSONObject result1 = coreApi.getPaymentAccount(accountId);
        assertEquals(accountId, result1.getString("account_id"));
        assertEquals("PENDING", result1.get("account_status"));
    }

    @Test
    @Order(24)
    public void unlinkPaymentAccount() {
        try {
            coreApi.unlinkPaymentAccount(accountId);
        } catch (MidtransError midtransError) {
            assertEquals(412, midtransError.getStatusCode());
        }
    }

    @Test
    @Order(25)
    public void getBinCard() throws MidtransError {
        coreApi.apiConfig().setServerKey(mainClientKey);
        JSONObject result = coreApi.getBIN("420191");
        assertEquals("INDONESIA", result.getJSONObject("data").getString("country_name"));
        assertEquals("VISA", result.getJSONObject("data").getString("brand"));
    }

    @Test
    @Order(26)
    public void failChargeTransactionWrongServerKey() {
        coreApi.apiConfig().setServerKey("dummy");
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");

        try {
            coreApi.chargeTransaction(dataMockup.initDataMock());
        } catch (MidtransError midtransError) {
            assertEquals(401, midtransError.getStatusCode());
        }
    }

    @Test
    @Order(27)
    public void chargeTransactionWithIdempotencyKey() throws MidtransError {
        coreApi.apiConfig().setServerKey(mainServerKey);
        dataMockup.setPaymentType("gopay");

        coreApi.apiConfig().setPaymentIdempotencyKey("123321123321");
        JSONObject result1 = coreApi.chargeTransaction(dataMockup.initDataMock());
        String transactionId1 = result1.getString("transaction_id");

        JSONObject result2 = coreApi.chargeTransaction(dataMockup.initDataMock());
        String transactionId2 = result2.getString("transaction_id");

        assertEquals(transactionId2, transactionId1);
    }

    @Test
    @Order(28)
    public void successChargeTransactionWithPermataVA() throws MidtransError {
        coreApi.apiConfig().setServerKey(mainServerKey);
        dataMockup.setPaymentType("bank_transfer");
        dataMockup.setBank("permata");

        JSONObject result1 = coreApi.chargeTransaction(dataMockup.initDataMock());
        assertEquals("bank_transfer", result1.getString("payment_type"));
        assertEquals("pending", result1.getString("transaction_status"));

        JSONObject result2 = coreApi.chargeTransaction(dataMockup.initDataMock());
        assertEquals("bank_transfer", result2.getString("payment_type"));
        assertEquals("pending", result2.getString("transaction_status"));
    }

}
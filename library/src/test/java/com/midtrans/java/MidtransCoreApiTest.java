package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.service.MidtransCoreApi;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.Assert.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MidtransCoreApiTest {

    private static MidtransCoreApi coreApi;
    private static DataMockup dataMockup;

    private static String subscriptionName;
    private static String subscriptionId;

    private static String accountId;

    @BeforeAll
    public static void setUp() {
        Config configOptions = Config.builder()
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
        JSONObject result = coreApi.registerCard(creditCard(cardNumberAccept));
        assert result.getString("status_code").equals("200");
        assert result.getString("saved_token_id").substring(0, 6).equals(cardNumberAccept.substring(0, 6));
    }

    @Test
    @Order(2)
    public void cardToken() throws MidtransError {
        JSONObject result = coreApi.cardToken(creditCard(cardNumberAccept));
        assert result.has("token_id");
        assertNotNull(result.getString("token_id"));
        assert result.getString("status_code").equals("200");
    }

    @Test
    @Order(3)
    public void chargeCreditCardTransaction() throws MidtransError {
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        cc.put("token_id", genCardToken(cardNumberAccept));
        dataMockup.creditCard(cc);

        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.length() != 0;
    }

    @Test
    @Order(4)
    public void failChargeWithEmptyBody() {
        try {
            coreApi.chargeTransaction(null);
        } catch (MidtransError e) {
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 500 API response: {\"message\":\"An unexpected error occurred\"}");
        }
    }

    @Test
    @Order(5)
    public void chargeGoPayTransaction() throws MidtransError {
        dataMockup.setPaymentType("gopay");
        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.getString("status_code").equals("201");
        assert result.getString("payment_type").equals("gopay");
    }

    @Test
    @Order(6)
    public void cardPointInquiry() throws MidtransError {
        JSONObject result = coreApi.cardPointInquiry(genCardToken(bniCardNumber));
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, Credit Card Point inquiry is successful");
    }

    @Test
    @Order(7)
    public void orderIdNullCheckTransaction() throws MidtransError {
        JSONObject result = coreApi.checkTransaction("null");
        assert result.getString("status_code").equals("404");
    }

    @Test
    @Order(8)
    public void checkTransaction() throws MidtransError {
        JSONObject result = coreApi.checkTransaction(makeTransaction());
        assert result.getString("status_code").equals("201");
        assert result.getString("status_message").equals("Success, transaction is found");
    }

    @Test
    @Order(9)
    public void approveTransaction() throws MidtransError {
        JSONObject result = coreApi.approveTransaction(makeTransaction());
        assert result.getString("status_code").equals("412");
        assert result.getString("status_message").equals("Transaction status cannot be updated.");
    }

    @Test
    @Order(10)
    public void denyTransaction() throws MidtransError {
        JSONObject result = coreApi.denyTransaction(makeTransaction());
        assert result.getString("status_code").equals("412");
        assert result.getString("status_message").equals("Transaction status cannot be updated.");
    }

    @Test
    @Order(11)
    public void cancelTransaction() throws MidtransError {
        JSONObject result = coreApi.cancelTransaction(makeTransaction());
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, transaction is canceled");
    }

    @Test
    @Order(12)
    public void expireTransaction() throws MidtransError {
        JSONObject result = coreApi.expireTransaction(makeTransaction());
        assert result.getString("status_code").equals("407");
        assert result.getString("status_message").equals("Success, transaction is expired");
    }

    @Test
    @Order(13)
    public void refundTransaction() throws MidtransError {
        Map<String, String> refundBody = new HashMap<>();
        refundBody.put("amount", "265000");
        refundBody.put("reason", "Product is out of stock, payment is being refunded");

        JSONObject result = coreApi.refundTransaction(makeTransaction(), refundBody);
        assert result.getString("status_code").equals("412");
    }

    @Test
    @Order(14)
    public void captureTransaction() throws MidtransError {
        UUID idRandom = UUID.randomUUID();
        Map<String, String> params = new HashMap<>();

        params.put("transaction_id", idRandom.toString());
        params.put("gross_amount", "265.000");
        JSONObject result = coreApi.captureTransaction(params);

        assert result.getString("status_code").equals("404");
    }

    @Test
    @Order(15)
    public void getStatusB2BTransaction() throws MidtransError {
        JSONObject result = coreApi.getTransactionStatusB2B(makeTransaction());
        assert result.getString("status_code").equals("404");
    }

    @Test
    @Order(16)
    public void directRefund() throws MidtransError {
        UUID stringRand = UUID.randomUUID();

        Map<String, String> params = new HashMap<>();
        params.put("refund_key", stringRand.toString());
        params.put("amount", "265000");
        params.put("reason", "Test direct refund");

        JSONObject result = coreApi.directRefundTransaction(makeTransaction(), params);
        assert result.getString("status_code").equals("412");
    }

    @Test
    @Order(17)
    public void createSubscription() throws MidtransError {
        subscriptionName = "MID-JAVA-SUBS1" + dataMockup.random();
        Map<String, Object> request1 = dataMockup.subscriptionRequest(subscriptionName, 1, "month");
        JSONObject result1 = coreApi.createSubscription(request1);
        assert result1.getString("name").equals(subscriptionName);
        subscriptionId = result1.getString("id");
    }

    @Test
    @Order(18)
    public void disableSubscription() throws MidtransError {
        JSONObject result1 = coreApi.disableSubscription(subscriptionId);
        assert result1.getString("status_message").equals("Subscription is updated.");
    }

    @Test
    @Order(19)
    public void enableSubscription() throws MidtransError {
        JSONObject result1 = coreApi.enableSubscription(subscriptionId);
        assert result1.getString("status_message").equals("Subscription is updated.");
    }

    @Test
    @Order(20)
    public void updateSubscription() throws MidtransError {
        Map<String, Object> request1 = dataMockup.subscriptionRequest(subscriptionName, 2, "month");
        JSONObject result1 = coreApi.updateSubscription(subscriptionId, request1);
        assert result1.getString("status_message").equals("Subscription is updated.");
    }

    @Test
    @Order(21)
    public void getSubscription() throws MidtransError {
        JSONObject result1 = coreApi.getSubscription(subscriptionId);
        assert result1.getString("name").equals(subscriptionName);
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
        assert result1.getString("status_code").equals("201");
        accountId = result1.getString("account_id");
    }

    @Test
    @Order(23)
    public void getPaymentAccount() throws MidtransError {
        JSONObject result1 = coreApi.getPaymentAccount(accountId);
        assert result1.getString("account_id").equals(accountId);
        assert result1.getString("account_status").equals("PENDING");
    }

    @Test
    @Order(24)
    public void unlinkPaymentAccount() throws MidtransError {
        JSONObject result1 = coreApi.unlinkPaymentAccount(accountId);
        assert result1.getString("status_code").equals("412");
    }

    @Test
    @Order(25)
    public void getBinCard() throws MidtransError {
        coreApi.apiConfig().setServerKey(mainClientKey);
        JSONObject result = coreApi.getBIN("420191");
        assert result.getJSONObject("data").getString("country_name").equals("INDONESIA");
        assert result.getJSONObject("data").getString("brand").equals("VISA");
    }

    @Test
    @Order(26)
    public void failChargeTransactionWrongServerKey() throws MidtransError {
        coreApi.apiConfig().setServerKey("dummy");
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");
        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());

        assert result.getString("status_code").equals("401");
    }

    // Make dummy transaction for get orderId
    private String makeTransaction() throws MidtransError {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");
        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        return result.getString("order_id");
    }

    // MockUp Transaction FDS Challenge
    private String makeFDSTransaction() throws MidtransError {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        String cardNumberFDS = "4811111111111114";
        cc.put("token_id", genCardToken(cardNumberFDS));
        dataMockup.creditCard(cc);

        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        return result.getString("order_id");
    }

    // Mock CreditCard Data
    private Map<String, String> creditCard(String cardNumber) {
        Map<String, String> cardParams = new HashMap<>();
        cardParams.put("card_number", cardNumber);
        cardParams.put("card_exp_month", "12");
        cardParams.put("card_exp_year", "2022");
        cardParams.put("card_cvv", "123");
        cardParams.put("client_key", coreApi.apiConfig().getClientKey());
        return cardParams;
    }

    //For generate tokenCard
    private String genCardToken(String cardNumber) throws MidtransError {
        JSONObject result = coreApi.cardToken(creditCard(cardNumber));
        return result.getString("token_id");

    }

}
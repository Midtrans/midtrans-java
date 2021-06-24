package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.Assert.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoreApiTest {

    private static DataMockup dataMockup;
    private static Config configOptions;

    private static String subscriptionName1;
    private static String subscriptionId1;
    private static String subscriptionName2;
    private static String subscriptionId2;

    private static String accountId1;
    private static String accountId2;

    @BeforeAll
    public static void setUp() {
        dataMockup = new DataMockup();

        /* Using Global Config */
        Midtrans.isProduction = false;
        Midtrans.clientKey = mainClientKey;
        Midtrans.serverKey = mainServerKey;
        Midtrans.enableLog = true;

        /* Using config option on each request */
        configOptions = Config.builder()
                .setServerKey(secondServerKey)
                .setClientKey(secondClientKey)
                .build();
    }

    @Test
    @Order(1)
    public void registerCard() throws MidtransError {
        /* Using Global Config */
        JSONObject result1 = CoreApi.registerCard(creditCard(cardNumberAccept, mainClientKey));
        assert result1.getString("status_code").equals("200");
        assert result1.getString("saved_token_id").substring(0, 6).equals(cardNumberAccept.substring(0, 6));

        /* Using config option on request */
        JSONObject result2 = CoreApi.registerCard(creditCard(cardNumberAccept, secondClientKey), configOptions);
        assert result2.getString("status_code").equals("200");
        assert result2.getString("saved_token_id").substring(0, 6).equals(cardNumberAccept.substring(0, 6));
    }

    @Test
    @Order(2)
    public void cardToken() throws MidtransError {
        /* Using method with Global Config */
        JSONObject result1 = CoreApi.cardToken(creditCard(cardNumberAccept, mainClientKey));
        assert result1.has("token_id");
        assertNotNull(result1.getString("token_id"));
        assert result1.getString("status_code").equals("200");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.cardToken(creditCard(cardNumberAccept, secondClientKey), configOptions);
        assert result2.has("token_id");
        assertNotNull(result2.getString("token_id"));
        assert result2.getString("status_code").equals("200");
    }

    @Test
    @Order(3)
    public void chargeCreditCardTransaction() throws MidtransError {
        /* Using method with Global Config */
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        cc.put("token_id", genCardToken(cardNumberAccept, mainClientKey));
        dataMockup.creditCard(cc);

        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.get("transaction_status").equals("capture");

        /* Using method with config option on request */
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc2 = new HashMap<>();
        cc2.put("token_id", genCardToken(cardNumberAccept, secondClientKey));
        dataMockup.creditCard(cc2);

        JSONObject result2 = CoreApi.chargeTransaction(dataMockup.initDataMock(), configOptions);
        assert result2.get("transaction_status").equals("capture");
    }

    @Test
    @Order(4)
    public void failChargeWithEmptyBody() {
        /* Using method with Global Config */
        try {
            CoreApi.chargeTransaction(null);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 500");
        }

        /* Using method with config option on request */
        try {
            CoreApi.chargeTransaction(null, configOptions);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 500");
        }
    }

    @Test
    @Order(5)
    public void chargeGoPayTransaction() throws MidtransError {
        dataMockup.setPaymentType("gopay");

        /* Using method with Global Config */
        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.getString("status_code").equals("201");
        assert result.getString("payment_type").equals("gopay");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.chargeTransaction(dataMockup.initDataMock(), configOptions);
        assert result2.getString("status_code").equals("201");
        assert result2.getString("payment_type").equals("gopay");
    }

    @Test
    @Order(6)
    public void chargeTransactionWithIdempotencyKey() throws MidtransError {
        dataMockup.setPaymentType("gopay");

        Config configOptions = Config.builder()
                .setServerKey(mainServerKey)
                .setPaymentIdempotencyKey(String.valueOf(UUID.randomUUID()))
                .build();

        JSONObject result1 = CoreApi.chargeTransaction(dataMockup.initDataMock(), configOptions);
        String transactionId1 = result1.getString("transaction_id");

        JSONObject result2 = CoreApi.chargeTransaction(dataMockup.initDataMock(), configOptions);
        String transactionId2 = result2.getString("transaction_id");

        assert transactionId1.equals(transactionId2);
    }

    @Test
    @Order(7)
    public void cardPointInquiry() throws MidtransError {
        /* Using method with Global Config */
        JSONObject result = CoreApi.cardPointInquiry(genCardToken(bniCardNumber, mainClientKey));
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, Credit Card Point inquiry is successful");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.cardPointInquiry(genCardToken(bniCardNumber, secondClientKey), configOptions);
        assert result2.getString("status_code").equals("200");
        assert result2.getString("status_message").equals("Success, Credit Card Point inquiry is successful");
    }

    @Test
    @Order(8)
    public void failChargeTransactionWrongServerKey() throws MidtransError {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");

        /* Using method with Global Config */
        Midtrans.serverKey = "dummy";
        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.getString("status_code").equals("401");

        /* Using method with config option on request */
        Config configOptions = Config.builder().setServerKey("dummy").build();
        JSONObject result2 = CoreApi.chargeTransaction(dataMockup.initDataMock(), configOptions);
        assert result2.getString("status_code").equals("401");
    }

    @Test
    @Order(9)
    public void getBinCard() throws MidtransError {
        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        JSONObject result = CoreApi.getBin("420191");
        assert result.getJSONObject("data").getString("country_name").equals("INDONESIA");
        assert result.getJSONObject("data").getString("brand").equals("VISA");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.getBin("420191", configOptions);
        assert result2.getJSONObject("data").getString("country_name").equals("INDONESIA");
        assert result2.getJSONObject("data").getString("brand").equals("VISA");
    }

    @Test
    @Order(10)
    public void createSubscription() throws MidtransError {
        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        subscriptionName1 = "MID-JAVA-SUBS1" + dataMockup.random();
        Map<String, Object> request1 = dataMockup.subscriptionRequest(subscriptionName1, 1, "month");
        JSONObject result1 = CoreApi.createSubscription(request1);
        assert result1.getString("name").equals(subscriptionName1);
        subscriptionId1 = result1.getString("id");

        /* Using method with config option on request */
        subscriptionName2 = "MID-JAVA-SUBS2" + dataMockup.random();
        Map<String, Object> request = dataMockup.subscriptionRequest(subscriptionName2, 2, "month");
        JSONObject result2 = CoreApi.createSubscription(request, configOptions);
        assert result2.getString("name").equals(subscriptionName2);
        subscriptionId2 = result2.getString("id");
    }

    @Test
    @Order(11)
    public void disableSubscription() throws MidtransError {
        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        JSONObject result1 = CoreApi.disableSubscription(subscriptionId1);
        assert result1.getString("status_message").equals("Subscription is updated.");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.disableSubscription(subscriptionId2, configOptions);
        assert result2.getString("status_message").equals("Subscription is updated.");
    }

    @Test
    @Order(12)
    public void enableSubscription() throws MidtransError {
        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        JSONObject result1 = CoreApi.enableSubscription(subscriptionId1);
        assert result1.getString("status_message").equals("Subscription is updated.");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.enableSubscription(subscriptionId2, configOptions);
        assert result2.getString("status_message").equals("Subscription is updated.");
    }

    @Test
    @Order(13)
    public void updateSubscription() throws MidtransError {
        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        Map<String, Object> request1 = dataMockup.subscriptionRequest(subscriptionName1, 2, "month");
        JSONObject result1 = CoreApi.updateSubscription(subscriptionId1, request1);
        assert result1.getString("status_message").equals("Subscription is updated.");

        /* Using method with config option on request */
        Map<String, Object> request2 = dataMockup.subscriptionRequest(subscriptionName2, 2, "month");
        JSONObject result2 = CoreApi.updateSubscription(subscriptionId2, request2, configOptions);
        assert result2.getString("status_message").equals("Subscription is updated.");
    }

    @Test
    @Order(14)
    public void getSubscription() throws MidtransError {
        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        JSONObject result1 = CoreApi.getSubscription(subscriptionId1);
        assert result1.getString("name").equals(subscriptionName1);

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.getSubscription(subscriptionId2, configOptions);
        assert result2.getString("name").equals(subscriptionName2);
    }

    @Test
    @Order(15)
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

        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        JSONObject result1 = CoreApi.linkPaymentAccount(request);
        assert result1.getString("status_code").equals("201");
        accountId1 = result1.getString("account_id");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.linkPaymentAccount(request, configOptions);
        assert result2.getString("status_code").equals("201");
        accountId2 = result2.getString("account_id");
    }

    @Test
    @Order(16)
    public void getPaymentAccount() throws MidtransError {
        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        JSONObject result1 = CoreApi.getPaymentAccount(accountId1);
        assert result1.getString("account_id").equals(accountId1);
        assert result1.getString("account_status").equals("PENDING");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.getPaymentAccount(accountId2, configOptions);
        assert result2.getString("account_id").equals(accountId2);
        assert result2.getString("account_status").equals("PENDING");
    }

    @Test
    @Order(17)
    public void unlinkPaymentAccount() throws MidtransError {
        /* Using method with Global Config */
        Midtrans.serverKey = mainServerKey;
        JSONObject result1 = CoreApi.unlinkPaymentAccount(accountId1);
        assert result1.getString("status_code").equals("412");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.unlinkPaymentAccount(accountId2, configOptions);
        assert result2.getString("status_code").equals("412");
    }

    // Mock CreditCard Data
    private Map<String, String> creditCard(String cardNumber, String clientKey) {
        Map<String, String> cardParams = new HashMap<>();
        cardParams.put("card_number", cardNumber);
        cardParams.put("card_exp_month", "12");
        cardParams.put("card_exp_year", "2022");
        cardParams.put("card_cvv", "123");
        cardParams.put("client_key", clientKey);
        return cardParams;
    }

    //For generate tokenCard
    private String genCardToken(String cardNumber, String clientKey) throws MidtransError {
        JSONObject result = CoreApi.cardToken(creditCard(cardNumber, clientKey));
        return result.getString("token_id");
    }
}
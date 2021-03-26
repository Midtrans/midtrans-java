package com.midtrans.java.v2;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.CoreApi;
import com.midtrans.v2.gateway.http.ApiConfig;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.*;

public class CoreApiV2Test {

    private DataMockup dataMockup;
    private ApiConfig apiConfig;
    private String CORE_API_VERSION = "v2";

    @Before
    public void setUp() {
        dataMockup = new DataMockup();
        Midtrans.isProduction = false;
        Midtrans.clientKey = mainClientKey;
        apiConfig = ApiConfig.builder()
                .setServerKey(secondServerKey)
                .setClientKey(secondClientKey)
                .build();
    }

    @Test
    public void baseUrl() {
        String sandboxBaseUrl = CoreApi.coreApiBaseUrl(false, CORE_API_VERSION);
        assert sandboxBaseUrl.equals(Midtrans.getSandboxBaseUrl() + CORE_API_VERSION + "/");

        String productionBaseUrl = CoreApi.coreApiBaseUrl(true, CORE_API_VERSION);
        assert productionBaseUrl.equals(Midtrans.getProductionBaseUrl() + CORE_API_VERSION + "/");
    }

    @Test
    public void registerCard() throws MidtransError {
        JSONObject result = CoreApi.registerCard(creditCard(cardNumberAccept, mainClientKey));
        assert result.getString("status_code").equals("200");
        assert result.getString("saved_token_id").substring(0, 6).equals(cardNumberAccept.substring(0, 6));
    }

    @Test
    public void cardToken() throws MidtransError {
        JSONObject result = CoreApi.cardToken(creditCard(cardNumberAccept, mainClientKey));
        assert result.has("token_id");
        assert result.getString("token_id").matches("[0-9]{6}-[0-9]{4}-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
        assert result.getString("status_code").equals("200");
    }

    @Test
    public void chargeCreditCardTransaction() throws MidtransError {
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        cc.put("token_id", genCardToken(cardNumberAccept, mainClientKey));
        dataMockup.creditCard(cc);

        Midtrans.serverKey = mainServerKey;
        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.get("transaction_status").equals("capture");

        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc2 = new HashMap<>();
        cc2.put("token_id", genCardToken(cardNumberAccept, secondClientKey));
        dataMockup.creditCard(cc2);
        JSONObject result2 = CoreApi.chargeTransaction(apiConfig, dataMockup.initDataMock());
        assert result2.get("transaction_status").equals("capture");
    }

    @Test
    public void failChargeWithEmptyBody() {
        try {
            CoreApi.chargeTransaction(null);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 500 API response: {\"message\":\"An unexpected error occurred\"}");
        }

        try {
            CoreApi.chargeTransaction(apiConfig, null);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 500 API response: {\"message\":\"An unexpected error occurred\"}");
        }
    }

    @Test
    public void chargeGoPayTransaction() throws MidtransError {
        dataMockup.setPaymentType("gopay");

        Midtrans.serverKey = mainServerKey;
        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.getString("status_code").equals("201");
        assert result.getString("payment_type").equals("gopay");

        JSONObject result2 = CoreApi.chargeTransaction(apiConfig, dataMockup.initDataMock());
        assert result2.getString("status_code").equals("201");
        assert result2.getString("payment_type").equals("gopay");
    }

    @Test
    public void chargeTransactionWithIdempotencyKey() throws MidtransError {
        dataMockup.setPaymentType("gopay");

        ApiConfig apiConfig = ApiConfig.builder()
                .setServerKey(mainServerKey)
                .setPaymentIdempotencyKey(String.valueOf(UUID.randomUUID()))
                .build();

        JSONObject result1 = CoreApi.chargeTransaction(apiConfig, dataMockup.initDataMock());
        String transactionId1 = result1.getString("transaction_id");

        JSONObject result2 = CoreApi.chargeTransaction(apiConfig, dataMockup.initDataMock());
        String transactionId2 = result2.getString("transaction_id");

        assert transactionId1.equals(transactionId2);
    }

    @Test
    public void cardPointInquiry() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = CoreApi.cardPointInquiry(genCardToken(bniCardNumber, mainClientKey));
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, Credit Card Point inquiry is successful");


        JSONObject result2 = CoreApi.cardPointInquiry(apiConfig, genCardToken(bniCardNumber, secondClientKey));
        assert result2.getString("status_code").equals("200");
        assert result2.getString("status_message").equals("Success, Credit Card Point inquiry is successful");
    }

    @Test
    public void failChargeTransactionNoServerKey() throws MidtransError {
        Midtrans.serverKey = "l";
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");
        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());

        assert result.getString("status_code").equals("401");
    }

    @Test
    public void getBinCard() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = CoreApi.getBin("420191");
        assert result.getJSONObject("data").getString("country_name").equals("INDONESIA");
        assert result.getJSONObject("data").getString("brand").equals("VISA");

        JSONObject result2 = CoreApi.getBin(apiConfig, "420191");
        assert result2.getJSONObject("data").getString("country_name").equals("INDONESIA");
        assert result2.getJSONObject("data").getString("brand").equals("VISA");
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
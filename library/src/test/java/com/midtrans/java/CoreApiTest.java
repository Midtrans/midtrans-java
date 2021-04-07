package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.*;

public class CoreApiTest {

    private DataMockup dataMockup;
    private Config configOptions;

    @Before
    public void setUp() {
        dataMockup = new DataMockup();

        /* Using Global Config */
        Midtrans.isProduction = false;
        Midtrans.clientKey = mainClientKey;
        Midtrans.serverKey = mainServerKey;
        Midtrans.enableLog = true;

        /* Using config option on each request */
        configOptions = Config.builder()
                .setSERVER_KEY(secondServerKey)
                .setCLIENT_KEY(secondClientKey)
                .build();
    }

    @Test
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
    public void cardToken() throws MidtransError {
        /* Using method with Global Config */
        JSONObject result1 = CoreApi.cardToken(creditCard(cardNumberAccept, mainClientKey));
        assert result1.has("token_id");
        assert result1.getString("token_id").matches("[0-9]{6}-[0-9]{4}-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
        assert result1.getString("status_code").equals("200");

        /* Using method with config option on request */
        JSONObject result2 = CoreApi.cardToken(creditCard(cardNumberAccept, secondClientKey), configOptions);
        assert result2.has("token_id");
        assert result2.getString("token_id").matches("[0-9]{6}-[0-9]{4}-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
        assert result2.getString("status_code").equals("200");
    }

    @Test
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
    public void failChargeWithEmptyBody() {
        /* Using method with Global Config */
        try {
            CoreApi.chargeTransaction(null);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 500 API response: {\"message\":\"An unexpected error occurred\"}");
        }

        /* Using method with config option on request */
        try {
            CoreApi.chargeTransaction(null, configOptions);
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 500 API response: {\"message\":\"An unexpected error occurred\"}");
        }
    }

    @Test
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
    public void chargeTransactionWithIdempotencyKey() throws MidtransError {
        dataMockup.setPaymentType("gopay");

        Config configOptions = Config.builder()
                .setSERVER_KEY(mainServerKey)
                .setPaymentIdempotencyKey(String.valueOf(UUID.randomUUID()))
                .build();

        JSONObject result1 = CoreApi.chargeTransaction(dataMockup.initDataMock(), configOptions);
        String transactionId1 = result1.getString("transaction_id");

        JSONObject result2 = CoreApi.chargeTransaction(dataMockup.initDataMock(), configOptions);
        String transactionId2 = result2.getString("transaction_id");

        assert transactionId1.equals(transactionId2);
    }

    @Test
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
    public void failChargeTransactionWrongServerKey() throws MidtransError {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");

        /* Using method with Global Config */
        Midtrans.serverKey = "dummy";
        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.getString("status_code").equals("401");

        /* Using method with config option on request */
        Config configOptions = Config.builder().setSERVER_KEY("dummy").build();
        JSONObject result2 = CoreApi.chargeTransaction(dataMockup.initDataMock(), configOptions);
        assert result2.getString("status_code").equals("401");
    }

    @Test
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
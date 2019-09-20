package com.midtrans.java;

import com.midtrans.ConfigBuilder;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.clientKey;
import static com.midtrans.java.mockupdata.Constant.serverKey;

public class CoreApiTest {

    private ConfigFactory configFactory;
    private MidtransCoreApi coreApi;
    private DataMockup dataMockup;

    private final String cardNumberAccept = "4811111111111114";

    @Before
    public void setUp() throws Exception {
        configFactory = new ConfigFactory(new ConfigBuilder()
                .setCLIENT_KEY(clientKey)
                .setSERVER_KEY(serverKey)
                .setIsProduction(false)
                .build());
        coreApi = configFactory.getCoreApi();
        dataMockup = new DataMockup();
    }

    @Test
    public void registerCard() {
        JSONObject result = coreApi.registerCard(creditCard(cardNumberAccept));
        assert result.getString("status_code").equals("200");
        assert result.getString("saved_token_id").substring(0, 6).equals(cardNumberAccept.substring(0, 6));
    }

    @Test
    public void cardToken() {
        JSONObject result = coreApi.cardToken(creditCard(cardNumberAccept));
        assert result.has("token_id");
        assert result.getString("token_id").matches("[0-9]{6}-[0-9]{4}-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
        assert result.getString("status_code").equals("200");
    }

    @Test
    public void chargeCreditCardTransaction() {
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        cc.put("token_id", genCardToken(cardNumberAccept));
        dataMockup.creditCard(cc);

        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.length() != 0;
    }

    @Test
    public void failChargeWithEmptyBody() {
        JSONObject result = coreApi.chargeTransaction(null);
        assert result.toString().equals("{}");
    }

    @Test
    public void chargeGoPayTransaction() {
        dataMockup.setPaymentType("gopay");
        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        assert result.getString("status_code").equals("201");
        assert result.getString("payment_type").equals("gopay");
    }

    @Test
    public void cardPointInquiry() {
        JSONObject result = coreApi.cardPointInquiry(genCardToken(cardNumberAccept));
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, Credit Card Point inquiry is successful");
    }

    @Test
    public void orderIdNullCheckTransaction() {
        JSONObject result = coreApi.checkTransaction("null");
        assert result.getString("status_code").equals("404");
    }

    @Test
    public void checkTransaction() {
        JSONObject result = coreApi.checkTransaction(makeTransaction());
        assert result.getString("status_code").equals("201");
        assert result.getString("status_message").equals("Success, transaction is found");
    }

    @Test
    public void approveTransaction() {
        JSONObject result = coreApi.approveTransaction(makeTransaction());
        assert result.getString("status_code").equals("412");

    }

    @Test
    public void cancelTransaction() {
        JSONObject result = coreApi.cancelTransaction(makeTransaction());
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, transaction is canceled");
    }

    @Test
    public void expireTransaction() {
        JSONObject result = coreApi.expireTransaction(makeTransaction());
        assert result.getString("status_code").equals("407");
        assert result.getString("status_message").equals("Success, transaction is expired");
    }

    @Test
    public void refundTransaction() {
        Map<String, String> refundBody = new HashMap<>();
        refundBody.put("amount", "265000");
        refundBody.put("reason", "Product is out of stock, payment is being refunded");

        JSONObject result = coreApi.refundTransaction(makeTransaction(), refundBody);
        assert result.getString("status_code").equals("412");
    }

    @Test
    public void captureTransaction() {
        UUID idRandom = UUID.randomUUID();
        Map<String, String> params = new HashMap<>();

        params.put("transaction_id", idRandom.toString());
        params.put("gross_amount", "265.000");
        JSONObject result = coreApi.captureTransaction(params);

        assert result.getString("status_code").equals("404");
    }

    @Test
    public void getStatusB2BTransaction() {
        JSONObject result = coreApi.getTransactionStatusB2B(makeTransaction());
        assert result.getString("status_code").equals("404");
    }

    @Test
    public void directRefund() {
        UUID stringRand = UUID.randomUUID();

        Map<String, String> params = new HashMap<>();
        params.put("refund_key", stringRand.toString());
        params.put("amount", "265000");
        params.put("reason", "Test direct refund");

        JSONObject result = coreApi.directRefundTransaction(makeTransaction(), params);
        assert result.getString("status_code").equals("412");
    }

    @Test
    public void failChargeTransactionNoServerKey() {
        coreApi.apiConfig().setSERVER_KEY("");
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");
        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());

        assert result.getString("status_code").equals("401");
    }


    // Make dummy transaction for get orderId
    private String makeTransaction() {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");
        JSONObject result = coreApi.chargeTransaction(dataMockup.initDataMock());
        return result.getString("order_id");
    }

    // MockUp Transaction FDS Challenge
    private String makeFDSTransaction() {
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
        cardParams.put("client_key", configFactory.getConfig().getCLIENT_KEY());
        return cardParams;
    }

    //For generate tokenCard
    private String genCardToken(String cardNumber) {
        JSONObject result = coreApi.cardToken(creditCard(cardNumber));
        return result.getString("token_id");
    }
}
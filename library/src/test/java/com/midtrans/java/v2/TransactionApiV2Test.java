package com.midtrans.java.v2;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.CoreApi;
import com.midtrans.v2.gateway.TransactionApi;
import com.midtrans.v2.gateway.http.ApiConfig;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.*;

public class TransactionApiV2Test {

    private DataMockup dataMockup;
    private Timestamp timestamp;
    private ApiConfig apiConfig;

    @Before
    public void setUp() {
        timestamp = new Timestamp(System.currentTimeMillis());
        dataMockup = new DataMockup();
        Midtrans.isProduction = false;
        apiConfig = ApiConfig.builder()
                .setServerKey(secondServerKey)
                .setClientKey(secondClientKey)
                .build();
    }


    @Test
    public void orderIdNullCheckTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.checkTransaction("null");
        assert result.getString("status_code").equals("404");

        JSONObject result2 = TransactionApi.checkTransaction(apiConfig, "null");
        assert result2.getString("status_code").equals("404");
    }

    @Test
    public void checkTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.checkTransaction(makeTransaction(mainServerKey));
        assert result.getString("status_code").equals("201");
        assert result.getString("status_message").equals("Success, transaction is found");

        JSONObject result2 = TransactionApi.checkTransaction(apiConfig, makeTransaction(apiConfig.getServerKey()));
        assert result2.getString("status_code").equals("201");
        assert result2.getString("status_message").equals("Success, transaction is found");
    }

    @Test
    public void approveTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.approveTransaction(makeFDSTransaction(mainServerKey, mainClientKey));
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, transaction is approved");

        JSONObject result2 = TransactionApi.approveTransaction(apiConfig, makeFDSTransaction(apiConfig.getServerKey(), apiConfig.getClientKey()));
        assert result2.getString("status_code").equals("200");
        assert result2.getString("status_message").equals("Success, transaction is approved");
    }

    @Test
    public void denyTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.denyTransaction(makeFDSTransaction(mainServerKey, mainClientKey));
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, transaction is denied");

        JSONObject result2 = TransactionApi.denyTransaction(apiConfig, makeFDSTransaction(apiConfig.getServerKey(), apiConfig.getClientKey()));
        assert result2.getString("status_code").equals("200");
        assert result2.getString("status_message").equals("Success, transaction is denied");
    }

    @Test
    public void cancelTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.cancelTransaction(makeTransaction(mainServerKey));
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, transaction is canceled");

        JSONObject result2 = TransactionApi.cancelTransaction(apiConfig, makeTransaction(apiConfig.getServerKey()));
        assert result2.getString("status_code").equals("200");
        assert result2.getString("status_message").equals("Success, transaction is canceled");
    }

    @Test
    public void expireTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.expireTransaction(makeTransaction(mainServerKey));
        assert result.getString("status_code").equals("407");
        assert result.getString("status_message").equals("Success, transaction is expired");

        JSONObject result2 = TransactionApi.expireTransaction(apiConfig, makeTransaction(apiConfig.getServerKey()));
        assert result2.getString("status_code").equals("407");
        assert result2.getString("status_message").equals("Success, transaction is expired");
    }

    @Test
    public void refundTransaction() throws MidtransError {
        Map<String, String> refundBody = new HashMap<>();
        refundBody.put("amount", "265000");
        refundBody.put("reason", "Product is out of stock, payment is being refunded");

        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.refundTransaction(makeTransaction(mainServerKey), refundBody);
        assert result.getString("status_code").equals("412");

        JSONObject result2 = TransactionApi.refundTransaction(apiConfig, makeTransaction(apiConfig.getServerKey()), refundBody);
        assert result2.getString("status_code").equals("412");
    }

    @Test
    public void captureTransaction() throws MidtransError {
        UUID idRandom = UUID.randomUUID();
        Map<String, String> params = new HashMap<>();

        params.put("transaction_id", idRandom.toString());
        params.put("gross_amount", "265000");
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.captureTransaction(params);
        assert result.getString("status_code").equals("404");

        JSONObject result2 = TransactionApi.captureTransaction(apiConfig, params);
        assert result2.getString("status_code").equals("404");
    }

    @Test
    public void getStatusB2BTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.getStatusB2b(makeTransaction(mainServerKey));
        assert result.getString("status_code").equals("404");

        JSONObject result2 = TransactionApi.getStatusB2b(apiConfig, makeTransaction(apiConfig.getServerKey()));
        assert result2.getString("status_code").equals("404");
    }

    @Test
    public void directRefund() throws MidtransError {
        UUID stringRand = UUID.randomUUID();

        Map<String, String> params = new HashMap<>();
        params.put("refund_key", stringRand.toString());
        params.put("amount", "265000");
        params.put("reason", "Test direct refund");

        JSONObject result = TransactionApi.directRefundTransaction(makeTransaction(mainServerKey), params);
        assert result.getString("status_code").equals("412");

        JSONObject result2 = TransactionApi.directRefundTransaction(apiConfig, makeTransaction(apiConfig.getServerKey()), params);
        assert result2.getString("status_code").equals("412");
    }


    // Make dummy transaction for get orderId
    private String makeTransaction(String serverKey) throws MidtransError {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");
        Midtrans.serverKey = serverKey;
        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        return result.getString("order_id");
    }

    // MockUp Transaction FDS Challenge
    private String makeFDSTransaction(String serverKey, String clientKey) throws MidtransError {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        cc.put("token_id", genCardToken(cardNumberFDS, clientKey));
        dataMockup.creditCard(cc);

        Midtrans.serverKey = serverKey;
        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        return result.getString("order_id");
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
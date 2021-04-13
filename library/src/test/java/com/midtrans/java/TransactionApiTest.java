package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.TransactionApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.*;

public class TransactionApiTest {

    private DataMockup dataMockup;
    private Config configOptions;

    @Before
    public void setUp() {
        dataMockup = new DataMockup();

        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;

        configOptions = Config.builder()
                .setServerKey(secondServerKey)
                .setClientKey(secondClientKey)
                .build();
    }


    @Test
    public void orderIdNullCheckTransaction() throws MidtransError {
        JSONObject result = TransactionApi.checkTransaction(null);
        assert result.getString("status_code").equals("404");

        JSONObject result2 = TransactionApi.checkTransaction(null, configOptions);
        assert result2.getString("status_code").equals("404");
    }

    @Test
    public void checkTransaction() throws MidtransError {
        JSONObject result = TransactionApi.checkTransaction(makeTransaction(null));
        assert result.getString("status_code").equals("201");
        assert result.getString("status_message").equals("Success, transaction is found");

        JSONObject result2 = TransactionApi.checkTransaction(makeTransaction(configOptions), configOptions);
        assert result2.getString("status_code").equals("201");
        assert result2.getString("status_message").equals("Success, transaction is found");
    }

    @Test
    public void approveTransaction() throws MidtransError {
        Config config = Config.builder().setServerKey(mainServerKey).setClientKey(mainClientKey).build();
        JSONObject result1 = TransactionApi.approveTransaction(makeFDSTransaction(config));
        assert result1.getString("status_code").equals("412");
        assert result1.getString("status_message").equals("Transaction status cannot be updated.");

        JSONObject result2 = TransactionApi.approveTransaction(makeFDSTransaction(configOptions), configOptions);
        assert result2.getString("status_code").equals("412");
        assert result2.getString("status_message").equals("Transaction status cannot be updated.");
    }

    @Test
    public void denyTransaction() throws MidtransError {
        Config config = Config.builder().setServerKey(mainServerKey).setClientKey(mainClientKey).build();
        JSONObject result1 = TransactionApi.denyTransaction(makeFDSTransaction(config));
        assert result1.getString("status_code").equals("412");
        assert result1.getString("status_message").equals("Transaction status cannot be updated.");

        JSONObject result2 = TransactionApi.denyTransaction(makeFDSTransaction(configOptions), configOptions);
        assert result2.getString("status_code").equals("412");
        assert result2.getString("status_message").equals("Transaction status cannot be updated.");
    }

    @Test
    public void cancelTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.cancelTransaction(makeTransaction(null));
        assert result.getString("status_code").equals("200");
        assert result.getString("status_message").equals("Success, transaction is canceled");

        JSONObject result2 = TransactionApi.cancelTransaction(makeTransaction(configOptions), configOptions);
        assert result2.getString("status_code").equals("200");
        assert result2.getString("status_message").equals("Success, transaction is canceled");
    }

    @Test
    public void expireTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.expireTransaction(makeTransaction(null));
        assert result.getString("status_code").equals("407");
        assert result.getString("status_message").equals("Success, transaction is expired");

        JSONObject result2 = TransactionApi.expireTransaction(makeTransaction(configOptions), configOptions);
        assert result2.getString("status_code").equals("407");
        assert result2.getString("status_message").equals("Success, transaction is expired");
    }

    @Test
    public void refundTransaction() throws MidtransError {
        Map<String, String> refundBody = new HashMap<>();
        refundBody.put("amount", "265000");
        refundBody.put("reason", "Product is out of stock, payment is being refunded");

        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.refundTransaction(makeTransaction(null), refundBody);
        assert result.getString("status_code").equals("412");

        JSONObject result2 = TransactionApi.refundTransaction(makeTransaction(configOptions), refundBody, configOptions);
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

        JSONObject result2 = TransactionApi.captureTransaction(params, configOptions);
        assert result2.getString("status_code").equals("404");
    }

    @Test
    public void getStatusB2BTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.getStatusB2b(makeTransaction(null));
        assert result.getString("status_code").equals("404");

        JSONObject result2 = TransactionApi.getStatusB2b(makeTransaction(configOptions), configOptions);
        assert result2.getString("status_code").equals("404");
    }

    @Test
    public void directRefund() throws MidtransError {
        UUID stringRand = UUID.randomUUID();

        Map<String, String> params = new HashMap<>();
        params.put("refund_key", stringRand.toString());
        params.put("amount", "265000");
        params.put("reason", "Test direct refund");

        JSONObject result = TransactionApi.directRefundTransaction(makeTransaction(null), params);
        assert result.getString("status_code").equals("412");

        JSONObject result2 = TransactionApi.directRefundTransaction(makeTransaction(configOptions), params, configOptions);
        assert result2.getString("status_code").equals("412");
    }


    // Make dummy transaction for get orderId
    private String makeTransaction(Config config) throws MidtransError {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");
        JSONObject result;

        if (config == null) {
            result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        } else {
            result = CoreApi.chargeTransaction(dataMockup.initDataMock(), config);
        }
        return result.getString("order_id");
    }

    // MockUp Transaction FDS Challenge
    private String makeFDSTransaction(Config config) throws MidtransError {
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        cc.put("token_id", genCardToken(cardNumberFDS, config.getClientKey()));
        dataMockup.creditCard(cc);

        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock(), config);
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
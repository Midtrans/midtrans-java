package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.TransactionApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.midtrans.java.mockupdata.Constant.*;
import static com.midtrans.java.mockupdata.DataMockup.makeFDSTransaction;
import static com.midtrans.java.mockupdata.DataMockup.makeTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionApiTest {

    private static Config configOptions;

    @BeforeAll
    public static void setUp() {
        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;

        configOptions = Config.builder()
                .setServerKey(secondServerKey)
                .setClientKey(secondClientKey)
                .build();
    }

    @Test
    public void orderIdNullCheckTransaction() {
        try {
            TransactionApi.checkTransaction(null);
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
            assertEquals(404, midtransError.getStatusCode());
        }

        try {
             TransactionApi.checkTransaction(null, configOptions);
        } catch (MidtransError midtransError) {
            assertEquals(404, midtransError.getStatusCode());
        }
    }

    @Test
    public void checkTransaction() throws MidtransError {
        JSONObject result = TransactionApi.checkTransaction(makeTransaction(null));
        assertEquals("201", result.getString("status_code"));
        assertEquals("Success, transaction is found", result.getString("status_message"));

        JSONObject result2 = TransactionApi.checkTransaction(makeTransaction(configOptions), configOptions);
        assertEquals("201", result2.getString("status_code"));
        assertEquals("Success, transaction is found", result2.getString("status_message"));
    }

    @Test
    public void approveTransaction() throws MidtransError {
        Config config = Config.builder().setServerKey(mainServerKey).setClientKey(mainClientKey).build();
        JSONObject result1 = TransactionApi.approveTransaction(makeFDSTransaction(config));
        assertEquals("200", result1.getString("status_code"));
        assertEquals("Success, transaction is approved", result1.getString("status_message"));

        JSONObject result2 = TransactionApi.approveTransaction(makeFDSTransaction(configOptions), configOptions);
        assertEquals("200", result2.getString("status_code"));
        assertEquals("Success, transaction is approved", result2.getString("status_message"));
    }

    @Test
    public void denyTransaction() throws MidtransError {
        Config config = Config.builder().setServerKey(mainServerKey).setClientKey(mainClientKey).build();
        JSONObject result1 = TransactionApi.denyTransaction(makeFDSTransaction(config));
        assertEquals("200", result1.getString("status_code"));
        assertEquals("Success, transaction is denied", result1.getString("status_message"));

        JSONObject result2 = TransactionApi.denyTransaction(makeFDSTransaction(configOptions), configOptions);
        assertEquals("200", result2.getString("status_code"));
        assertEquals("Success, transaction is denied", result2.getString("status_message"));
    }

    @Test
    public void cancelTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.cancelTransaction(makeTransaction(null));
        assertEquals("200", result.getString("status_code"));
        assertEquals("Success, transaction is canceled", result.getString("status_message"));

        JSONObject result2 = TransactionApi.cancelTransaction(makeTransaction(configOptions), configOptions);
        assertEquals("200", result2.getString("status_code"));
        assertEquals("Success, transaction is canceled", result2.getString("status_message"));
    }

    @Test
    public void expireTransaction() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = TransactionApi.expireTransaction(makeTransaction(null));
        assertEquals("407", result.getString("status_code"));
        assertEquals("Success, transaction is expired", result.getString("status_message"));

        JSONObject result2 = TransactionApi.expireTransaction(makeTransaction(configOptions), configOptions);
        assertEquals("407", result2.getString("status_code"));
        assertEquals("Success, transaction is expired", result2.getString("status_message"));
    }

    @Test
    public void refundTransaction() {
        Map<String, String> refundBody = new HashMap<>();
        refundBody.put("amount", "265000");
        refundBody.put("reason", "Product is out of stock, payment is being refunded");

        Midtrans.serverKey = mainServerKey;
        try {
            TransactionApi.refundTransaction(makeTransaction(null), refundBody);
        } catch (MidtransError midtransError) {
            assertEquals(412, midtransError.getStatusCode());
        }

        try {
            TransactionApi.refundTransaction(makeTransaction(configOptions), refundBody, configOptions);
        } catch (MidtransError midtransError) {
            assertEquals(412, midtransError.getStatusCode());
        }
    }

    @Test
    public void captureTransaction() {
        UUID idRandom = UUID.randomUUID();
        Map<String, String> params = new HashMap<>();

        params.put("transaction_id", idRandom.toString());
        params.put("gross_amount", "265000");
        Midtrans.serverKey = mainServerKey;
        try {
            TransactionApi.captureTransaction(params);
        } catch (MidtransError midtransError) {
            assertEquals(404, midtransError.getStatusCode());
        }

        try {
            TransactionApi.captureTransaction(params, configOptions);
        } catch (MidtransError midtransError) {
            assertEquals(404, midtransError.getStatusCode());
        }
    }

    @Test
    public void getStatusB2BTransaction() {
        Midtrans.serverKey = mainServerKey;
        try {
            TransactionApi.getStatusB2b(makeTransaction(null));
        } catch (MidtransError midtransError) {
            assertEquals(404, midtransError.getStatusCode());
        }

        try {
            TransactionApi.getStatusB2b(makeTransaction(configOptions), configOptions);
        } catch (MidtransError midtransError) {
            assertEquals(404, midtransError.getStatusCode());
        }
    }

    @Test
    public void directRefund() {
        UUID stringRand = UUID.randomUUID();

        Map<String, String> params = new HashMap<>();
        params.put("refund_key", stringRand.toString());
        params.put("amount", "265000");
        params.put("reason", "Test direct refund");

        try {
            TransactionApi.directRefundTransaction(makeTransaction(null), params);
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
            assertEquals(412, midtransError.getStatusCode());
        }

        try {
            TransactionApi.directRefundTransaction(makeTransaction(configOptions), params, configOptions);
        } catch (MidtransError midtransError) {
            assertEquals(412, midtransError.getStatusCode());
        }
    }
}
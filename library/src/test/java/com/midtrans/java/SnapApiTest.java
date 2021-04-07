package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.Assert.assertEquals;

public class SnapApiTest {

    private Config configOptions;
    private DataMockup dataMockup;

    @Before
    public void setUp() {
        dataMockup = new DataMockup();

        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;

        configOptions = Config.builder()
                .setSERVER_KEY(secondServerKey)
                .setCLIENT_KEY(secondClientKey)
                .build();
    }

    @Test
    public void createTransactionSimpleParam() throws MidtransError {
        JSONObject resultMethod1 = SnapApi.createTransaction(dataMockup.miniDataMockUp());
        assert resultMethod1.has("token");
        assert resultMethod1.has("redirect_url");

        JSONObject resultMethod2 = SnapApi.createTransaction(dataMockup.miniDataMockUp(), configOptions);
        assert resultMethod2.has("token");
        assert resultMethod2.has("redirect_url");
    }

    @Test
    public void createTransactionMaxParam() throws MidtransError, IOException {
        JSONObject result = SnapApi.createTransaction(dataMockup.maxDataMockUp());
        assert result.has("token");
        assert result.has("redirect_url");

        JSONObject result2 = SnapApi.createTransaction(dataMockup.maxDataMockUp(), configOptions);
        assert result2.has("token");
        assert result2.has("redirect_url");
    }

    @Test
    public void createTransactionToken() throws MidtransError {
        String token = SnapApi.createTransactionToken(dataMockup.miniDataMockUp());
        assert token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

        String token2 = SnapApi.createTransactionToken(dataMockup.miniDataMockUp(), configOptions);
        assert token2.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }

    @Test
    public void createTransactionRedirectUrl() throws MidtransError {
        // Method 1
        String redirectURL = SnapApi.createTransactionRedirectUrl(dataMockup.miniDataMockUp());
        String expected = "https://app.sandbox.midtrans.com/snap/v2/vtweb/";
        assertEquals(expected, redirectURL.substring(0, 47));

        // Method 2
        String redirectURL2 = SnapApi.createTransactionRedirectUrl(dataMockup.miniDataMockUp(), configOptions);
        String expected2 = "https://app.sandbox.midtrans.com/snap/v2/vtweb/";
        assertEquals(expected2, redirectURL2.substring(0, 47));
    }

    @Test
    public void badRequestBodyOnSnapTrans() {

        //Method 1 with global config
        try {
            JSONObject result1 = SnapApi.createTransaction(dataMockup.badDataMockUp());
            assert result1.getJSONArray("error_messages").get(0).toString().equals("transaction_details.gross_amount is required");
            assert result1.getJSONArray("error_messages").get(1).toString().equals("transaction_details.gross_amount is not a number");
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 400 API response:");
        }

        // Method 2 with apiConfigOptions
        try {
            JSONObject result2 = SnapApi.createTransaction(dataMockup.badDataMockUp(), configOptions);
            assert result2.getJSONArray("error_messages").get(0).toString().equals("transaction_details.gross_amount is required");
            assert result2.getJSONArray("error_messages").get(1).toString().equals("transaction_details.gross_amount is not a number");
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 400 API response:");
        }
    }

    @Test
    public void errorServerKey() {
        Midtrans.serverKey = "DUMMY";
        Config configOptions1 = Config.builder().setSERVER_KEY("DUMMY").build();

        //Method 1
        try {
            SnapApi.createTransaction(dataMockup.miniDataMockUp());
        } catch (MidtransError e) {
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 401");
            assert e.getResponseBody().equals("{\"error_messages\":[\"Access denied due to unauthorized transaction, please check client or server key\",\"Visit https://snap-docs.midtrans.com/#request-headers for more details\"]}");
        }

        // Method 2
        try {
            SnapApi.createTransaction(dataMockup.miniDataMockUp(), configOptions1);
        } catch (MidtransError e) {
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 401");
            assert e.getResponseBody().equals("{\"error_messages\":[\"Access denied due to unauthorized transaction, please check client or server key\",\"Visit https://snap-docs.midtrans.com/#request-headers for more details\"]}");
        }
    }

}
package com.midtrans.java.v2;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.SnapApi;
import com.midtrans.v2.gateway.http.ApiConfig;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.Assert.assertEquals;

public class SnapApiV2Test {

    private ApiConfig apiConfig;
    private DataMockup dataMockup;

    @Before
    public void setUp() {
        dataMockup = new DataMockup();
        Midtrans.isProduction = isProduction;
        apiConfig = ApiConfig.builder()
                .setServerKey(secondServerKey)
                .setClientKey(secondClientKey)
                .isProduction(isProduction)
                .build();
    }

    @Test
    public void baseUrl() {
        String pathUrl = "snap/v1/transactions";

        String sandboxBaseUrl = SnapApi.snapBaseUrl(false);
        assert sandboxBaseUrl.equals(Midtrans.getSnapSandboxBaseUrl() + pathUrl);

        String productionBaseUrl = SnapApi.snapBaseUrl(true);
        assert productionBaseUrl.equals(Midtrans.getSnapProductionBaseUrl() + pathUrl);
    }

    @Test
    public void createTransactionSimpleParam() throws MidtransError {
        Midtrans.serverKey = mainServerKey;

        JSONObject resultMethod1 = SnapApi.createTransaction(dataMockup.miniDataMockUp());
        assert resultMethod1.has("token");
        assert resultMethod1.has("redirect_url");

        JSONObject resultMethod2 = SnapApi.createTransaction(apiConfig, dataMockup.miniDataMockUp());
        assert resultMethod2.has("token");
        assert resultMethod2.has("redirect_url");
    }

    @Test
    public void createTransactionMaxParam() throws MidtransError, IOException {
        Midtrans.serverKey = mainServerKey;
        JSONObject result = SnapApi.createTransaction(dataMockup.maxDataMockUp());
        assert result.has("token");
        assert result.has("redirect_url");

        JSONObject result2 = SnapApi.createTransaction(apiConfig, dataMockup.maxDataMockUp());
        assert result2.has("token");
        assert result2.has("redirect_url");

    }

    @Test
    public void createTransactionToken() throws MidtransError {
        Midtrans.serverKey = mainServerKey;
        String token = SnapApi.createTransactionToken(dataMockup.miniDataMockUp());
        assert token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

        String token2 = SnapApi.createTransactionToken(apiConfig, dataMockup.miniDataMockUp());
        assert token2.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }

    @Test
    public void createTransactionRedirectUrl() throws MidtransError {
        // Method 1
        Midtrans.serverKey = mainServerKey;
        String redirectURL = SnapApi.createTransactionRedirectUrl(dataMockup.miniDataMockUp());
        String expected = "https://app.sandbox.midtrans.com/snap/v2/vtweb/";
        assertEquals(expected, redirectURL.substring(0, 47));

        // Method 2
        String redirectURL2 = SnapApi.createTransactionRedirectUrl(apiConfig, dataMockup.miniDataMockUp());
        String expected2 = "https://app.sandbox.midtrans.com/snap/v2/vtweb/";
        assertEquals(expected2, redirectURL2.substring(0, 47));
    }

    @Test
    public void badRequestBodyOnSnapTrans() {
        Midtrans.serverKey = mainServerKey;

        //Method 1
        try {
            JSONObject result1 = SnapApi.createTransaction(dataMockup.badDataMockUp());
            assert result1.getJSONArray("error_messages").get(0).toString().equals("transaction_details.gross_amount is required");
            assert result1.getJSONArray("error_messages").get(1).toString().equals("transaction_details.gross_amount is not a number");
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 400 API response:");
        }

        // Method 2
        try {
            JSONObject result2 = SnapApi.createTransaction(apiConfig, dataMockup.badDataMockUp());
            assert result2.getJSONArray("error_messages").get(0).toString().equals("transaction_details.gross_amount is required");
            assert result2.getJSONArray("error_messages").get(1).toString().equals("transaction_details.gross_amount is not a number");
        } catch (MidtransError e) {
            e.printStackTrace();
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 400 API response:");
        }
    }

    @Test
    public void errorServerKey() {
        Midtrans.serverKey = "";
        ApiConfig apiConfig1 = ApiConfig.builder().setServerKey("").build();

        //Method 1
        try {
            SnapApi.createTransaction(dataMockup.miniDataMockUp());
        } catch (MidtransError e) {
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 401");
            assert e.getResponseBody().equals("{\"error_messages\":[\"Access denied due to unauthorized transaction, please check client or server key\",\"Visit https://snap-docs.midtrans.com/#request-headers for more details\"]}");
        }

        // Method 2
        try {
            SnapApi.createTransaction(apiConfig1, dataMockup.miniDataMockUp());
        } catch (MidtransError e) {
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 401");
            assert e.getResponseBody().equals("{\"error_messages\":[\"Access denied due to unauthorized transaction, please check client or server key\",\"Visit https://snap-docs.midtrans.com/#request-headers for more details\"]}");
        }
    }

}
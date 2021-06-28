package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SnapApiTest {

    private static Config configOptions;
    private static DataMockup dataMockup;

    @BeforeAll
    public static void setUp() {
        dataMockup = new DataMockup();

        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;

        configOptions = Config.builder()
                .setServerKey(secondServerKey)
                .setClientKey(secondClientKey)
                .build();
    }

    @Test
    @Order(1)
    public void createTransactionSimpleParam() throws MidtransError {
        JSONObject resultMethod1 = SnapApi.createTransaction(dataMockup.miniDataMockUp());
        assertTrue(resultMethod1.has("token"));
        assertTrue(resultMethod1.has("redirect_url"));

        JSONObject resultMethod2 = SnapApi.createTransaction(dataMockup.miniDataMockUp(), configOptions);
        assertTrue(resultMethod2.has("token"));
        assertTrue(resultMethod2.has("redirect_url"));
    }

    @Test
    @Order(2)
    public void createTransactionMaxParam() throws MidtransError, IOException {
        JSONObject result = SnapApi.createTransaction(dataMockup.maxDataMockUp());
        assertTrue(result.has("token"));
        assertTrue(result.has("redirect_url"));

        JSONObject result2 = SnapApi.createTransaction(dataMockup.maxDataMockUp(), configOptions);
        assertTrue(result2.has("token"));
        assertTrue(result2.has("redirect_url"));
    }

    @Test
    @Order(3)
    public void createTransactionToken() throws MidtransError {
        String token = SnapApi.createTransactionToken(dataMockup.miniDataMockUp());
        assertTrue(token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"));

        String token2 = SnapApi.createTransactionToken(dataMockup.miniDataMockUp(), configOptions);
        assertTrue(token2.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"));
    }

    @Test
    @Order(4)
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
    @Order(5)
    public void badRequestBodyOnSnapTrans() {

        //Method 1 with global config
        try {
            JSONObject result1 = SnapApi.createTransaction(dataMockup.badDataMockUp());
            assertEquals("transaction_details.gross_amount is required", result1.getJSONArray("error_messages").get(0));
            assertEquals("transaction_details.gross_amount is not a number", result1.getJSONArray("error_messages").get(1));
        } catch (MidtransError e) {
            e.printStackTrace();
            assertTrue(e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 400 API response:"));
        }

        // Method 2 with apiConfigOptions
        try {
            JSONObject result2 = SnapApi.createTransaction(dataMockup.badDataMockUp(), configOptions);
            assert result2.getJSONArray("error_messages").get(0).toString().equals("transaction_details.gross_amount is required");
            assert result2.getJSONArray("error_messages").get(1).toString().equals("transaction_details.gross_amount is not a number");
        } catch (MidtransError e) {
            e.printStackTrace();
            assertTrue(e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 400 API response:"));
        }
    }

    @Test
    @Order(6)
    public void errorServerKey() {
        Midtrans.serverKey = "DUMMY";
        Config configOptions1 = Config.builder().setServerKey("DUMMY").build();

        //Method 1
        try {
            SnapApi.createTransaction(dataMockup.miniDataMockUp());
        } catch (MidtransError e) {
            assertTrue(e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 401"));
            assertEquals("{\"error_messages\":[\"Access denied due to unauthorized transaction, please check client or server key\",\"Visit https://snap-docs.midtrans.com/#request-headers for more details\"]}", e.getResponseBody());
        }

        // Method 2
        try {
            SnapApi.createTransaction(dataMockup.miniDataMockUp(), configOptions1);
        } catch (MidtransError e) {
            assertTrue(e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 401"));
            assertEquals("{\"error_messages\":[\"Access denied due to unauthorized transaction, please check client or server key\",\"Visit https://snap-docs.midtrans.com/#request-headers for more details\"]}", e.getResponseBody());
        }
    }

}
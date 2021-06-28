package com.midtrans.java;

import com.midtrans.ConfigBuilder;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.service.MidtransSnapApi;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MidtransSnapApiTest {

    private static MidtransSnapApi snapApi;
    private static DataMockup dataMockup;

    @BeforeAll
    public static void setUp() {
        dataMockup = new DataMockup();
        ConfigFactory configFactory = new ConfigFactory(new ConfigBuilder()
                .setServerKey(mainServerKey)
                .setClientKey(mainClientKey)
                .setIsProduction(isProduction)
                .build());
        snapApi = configFactory.getSnapApi();
    }

    @Test
    @Order(1)
    public void createTransactionSimpleParam() {
        JSONObject result = null;
        try {
            result = snapApi.createTransaction(dataMockup.miniDataMockUp());
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
        }
        assertTrue(result.has("token"));
        assertTrue(result.has("redirect_url"));
    }

    @Test
    @Order(2)
    public void createTransactionMaxParam() throws IOException, MidtransError {
        JSONObject result = snapApi.createTransaction(dataMockup.maxDataMockUp());

        assertTrue(result.has("token"));
        assertTrue(result.has("redirect_url"));
    }

    @Test
    @Order(3)
    public void createTransactionToken() throws MidtransError {
        String token = snapApi.createTransactionToken(dataMockup.miniDataMockUp());
        assertTrue(token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"));
    }

    @Test
    @Order(4)
    public void createTransactionRedirectUrl() throws MidtransError {
        String redirectURL = snapApi.createTransactionRedirectUrl(dataMockup.miniDataMockUp());
        String expected = "https://app.sandbox.midtrans.com/snap/v2/vtweb/";
        assertEquals(expected, redirectURL.substring(0, 47));
    }

    @Test
    @Order(5)
    public void badRequestBodyOnSnapTrans() {
        try {
            snapApi.createTransaction(dataMockup.badDataMockUp());
        } catch (MidtransError e) {
            assertTrue(e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 400 API response"));
        }
    }

    @Test
    @Order(6)
    public void errorServerKey() {
        snapApi.apiConfig().setServerKey("dummy");
        try {
            snapApi.createTransaction(dataMockup.miniDataMockUp());
        } catch (MidtransError e) {
            assertTrue(e.getMessage().contains("unauthorized"));
        }

    }

}
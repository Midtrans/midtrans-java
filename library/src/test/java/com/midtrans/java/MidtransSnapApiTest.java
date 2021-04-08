package com.midtrans.java;

import com.midtrans.ConfigBuilder;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.java.mockupdata.DataMockup;
import com.midtrans.service.MidtransSnapApi;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.Assert.assertEquals;

public class MidtransSnapApiTest {

    private MidtransSnapApi snapApi;
    private DataMockup dataMockup;

    @Before
    public void setUp() {
        dataMockup = new DataMockup();
        ConfigFactory configFactory = new ConfigFactory(new ConfigBuilder()
                .setServerKey(mainServerKey)
                .setClientKey(mainClientKey)
                .setIsProduction(isProduction)
                .build());
        snapApi = configFactory.getSnapApi();
    }

    @Test
    public void createTransactionSimpleParam() {
        JSONObject result = null;
        try {
            result = snapApi.createTransaction(dataMockup.miniDataMockUp());
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
        }
        assert result.has("token");
        assert result.has("redirect_url");
    }

    @Test
    public void createTransactionMaxParam() throws IOException, MidtransError {
        JSONObject result = snapApi.createTransaction(dataMockup.maxDataMockUp());

        assert result.has("token");
        assert result.has("redirect_url");
    }

    @Test
    public void createTransactionToken() throws MidtransError {
        String token = snapApi.createTransactionToken(dataMockup.miniDataMockUp());
        assert token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }

    @Test
    public void createTransactionRedirectUrl() throws MidtransError {
        String redirectURL = snapApi.createTransactionRedirectUrl(dataMockup.miniDataMockUp());
        String expected = "https://app.sandbox.midtrans.com/snap/v2/vtweb/";
        assertEquals(expected, redirectURL.substring(0, 47));
    }

    @Test
    public void badRequestBodyOnSnapTrans() {
        try {
            snapApi.createTransaction(dataMockup.badDataMockUp());
        } catch (MidtransError e) {
            assert e.getMessage().contains("Midtrans API is returning API error. HTTP status code: 400 API response");
        }
    }

    @Test
    public void errorServerKey() {
        snapApi.apiConfig().setServerKey("dummy");
        try {
            snapApi.createTransaction(dataMockup.miniDataMockUp());
        } catch (MidtransError e) {
            assert e.getMessage().contains("unauthorized");
        }

    }

}
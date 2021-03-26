package com.midtrans.java.v2;

import com.midtrans.proxy.ProxyConfig;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.http.ApiConfig;
import org.junit.Before;
import org.junit.Test;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.Assert.assertEquals;

public class ConfigTestV2 {

    private static final int CONNECT_TIME_OUT = 90;
    private static final int WRITE_TIMEOUT = 80;
    private static final int READ_TIMEOUT = 70;
    private static final int MAX_CONNECTION_POOL = 60;
    private static final int KEEP_ALIVE_DURATION = 50;

    private static final String APPEND_NOTIFICATION = "https://google.com/";
    private static final String OVERRIDE_NOTIFICATION = "https://example.com/";

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Before
    public void setUp() {
        ProxyConfig proxyConfig = new ProxyConfig(HOST, PORT, USERNAME, PASSWORD);

        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;
        Midtrans.isProduction = isProduction;
        Midtrans.paymentOverrideNotification(OVERRIDE_NOTIFICATION);
        Midtrans.paymentAppendNotification(APPEND_NOTIFICATION);
        Midtrans.setConnectTimeout(CONNECT_TIME_OUT);
        Midtrans.setWriteTimeout(WRITE_TIMEOUT);
        Midtrans.setReadTimeout(READ_TIMEOUT);
        Midtrans.setMaxConnectionPool(MAX_CONNECTION_POOL);
        Midtrans.setKeepAliveDuration(KEEP_ALIVE_DURATION);
        Midtrans.enableLog = true;
        Midtrans.setProxyConfig(proxyConfig);

    }

    @Test
    public void serverKey() {
        assertEquals(mainServerKey, Midtrans.getServerKey());

        ApiConfig apiConfig = ApiConfig.builder().setServerKey(secondServerKey).build();
        assertEquals(secondServerKey, apiConfig.getServerKey());
    }

    @Test
    public void clientKey() {
        assertEquals(mainClientKey, Midtrans.getClientKey());

        ApiConfig apiConfig = ApiConfig.builder().setClientKey(secondClientKey).build();
        assertEquals(secondClientKey, apiConfig.getClientKey());
    }

    @Test
    public void isProduction() {
        ApiConfig apiConfig = ApiConfig.builder()
                .isProduction(true)
                .build();
        assertEquals(isProduction, Midtrans.isProduction());
        assertEquals(true, apiConfig.isProduction());
    }

    @Test
    public void getConnectTimeout() {
        assertEquals(CONNECT_TIME_OUT, Midtrans.getConnectTimeout());
    }

    @Test
    public void getReadTimeOut() {
        assertEquals(READ_TIMEOUT, Midtrans.getReadTimeout());

        ApiConfig apiConfig1 = ApiConfig.builder().setReadTimeout(200).build();
        assertEquals(200, apiConfig1.getReadTimeout());

        ApiConfig apiConfig2 = ApiConfig.builder().setReadTimeout(-200).build();
        assertEquals(Midtrans.DEFAULT_READ_TIMEOUT, apiConfig2.getReadTimeout());
    }

    @Test
    public void getEnabledLog() {
        ApiConfig apiConfig = ApiConfig.builder()
                .enableLog(true)
                .build();
        assertEquals(true, Midtrans.enableLog());
        assertEquals(true, apiConfig.isEnableLog());
    }

    @Test
    public void getWriteTimeOut() {
        assertEquals(WRITE_TIMEOUT, Midtrans.getWriteTimeout());

        ApiConfig apiConfig1 = ApiConfig.builder().setWriteTimeout(100).build();
        assertEquals(100, apiConfig1.getWriteTimeout());

        ApiConfig apiConfig2 = ApiConfig.builder().setWriteTimeout(-100).build();
        assertEquals(Midtrans.DEFAULT_WRITE_TIMEOUT, apiConfig2.getWriteTimeout());
    }

    @Test
    public void proxyConfig() {
        ProxyConfig proxyConfig = Midtrans.getProxyConfig();
        assertEquals(HOST, proxyConfig.getHost());
        assertEquals(PORT, proxyConfig.getPort());
        assertEquals(USERNAME, proxyConfig.getUsername());
        assertEquals(PASSWORD, proxyConfig.getPassword());
    }

    @Test
    public void connectionPool() {
        assertEquals(MAX_CONNECTION_POOL, Midtrans.getMaxConnectionPool());

        ApiConfig apiConfig1 = ApiConfig.builder().setMaxConnectionPool(300).build();
        assertEquals(300, apiConfig1.getMaxConnectionPool());

        ApiConfig apiConfig2 = ApiConfig.builder().setMaxConnectionPool(-300).build();
        assertEquals(Midtrans.DEFAULT_MAX_CONNECTION_POOL_SIZE, apiConfig2.getMaxConnectionPool());

    }

    @Test
    public void keepAliveDuration() {
        assertEquals(KEEP_ALIVE_DURATION, Midtrans.getKeepAliveDuration());

        ApiConfig apiConfig1 = ApiConfig.builder().setKeepAliveDuration(400).build();
        assertEquals(400, apiConfig1.getKeepAliveDuration());

        ApiConfig apiConfig2 = ApiConfig.builder().setKeepAliveDuration(-400).build();
        assertEquals(Midtrans.DEFAULT_KEEP_ALIVE_DURATION, apiConfig2.getKeepAliveDuration());
    }

    @Test
    public void irisIdempotencyKey() {
        String idempotencyKey = "IRIS-IDEMPOTENCY-KEY";
        ApiConfig apiConfig = ApiConfig.builder().setIrisIdempotencykey(idempotencyKey).build();
        assertEquals(idempotencyKey, apiConfig.getIrisIdempotencykey());
    }

    @Test
    public void paymentIdempotencyKey() {
        String idempotencyKey = "PAYMENT-IDEMPOTENCY-KEY";
        ApiConfig apiConfig = ApiConfig.builder().setPaymentIdempotencyKey(idempotencyKey).build();
        assertEquals(idempotencyKey, apiConfig.getPaymentIdempotencyKey());
    }

    @Test
    public void paymentAppendNotification() {
        ApiConfig apiConfig = ApiConfig.builder()
                .setPaymentAppendNotification("https://midtrans.com/")
                .build();
        assertEquals(APPEND_NOTIFICATION, Midtrans.getPaymentAppendNotification());
        assertEquals("https://midtrans.com/", apiConfig.getPaymentAppendNotification());
    }

    @Test
    public void paymentOverrideNotification() {
        ApiConfig apiConfig = ApiConfig.builder()
                .setPaymentOverrideNotification("https://midtrans.com/")
                .build();
        assertEquals(OVERRIDE_NOTIFICATION, Midtrans.getPaymentOverrideNotification());
        assertEquals("https://midtrans.com/", apiConfig.getPaymentOverrideNotification());
    }

}
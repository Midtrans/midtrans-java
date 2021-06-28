package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.proxy.ProxyConfig;
import org.junit.jupiter.api.*;
import java.util.concurrent.TimeUnit;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigGlobalTest {

    private static Config config;

    @BeforeAll
    public static void setUp() {
        ProxyConfig proxyConfig = ProxyConfig.builder()
                .setHost(PROXY_HOTS)
                .setPort(PROXY_PORT)
                .setUsername(PROXY_USERNAME)
                .setPassword(PROXY_PASSWORD)
                .build();


        // set config for global
        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;
        Midtrans.isProduction = true;
        Midtrans.enableLog = true;
        Midtrans.setReadTimeout(READ_TIMEOUT);
        Midtrans.setConnectTimeout(CONNECT_TIMEOUT);
        Midtrans.setKeepAliveDuration(KEEP_ALIVE_DURATION);
        Midtrans.setMaxConnectionPool(CONNECTION_POOL_SIZE);
        Midtrans.setWriteTimeout(WRITE_TIMEOUT);
        Midtrans.setHttpClientTimeUnit(TimeUnit.MILLISECONDS);
        Midtrans.setProxyConfig(proxyConfig);
        Midtrans.paymentAppendNotification(APPEND_NOTIFICATION);
        Midtrans.paymentOverrideNotification(OVERRIDE_NOTIFICATION);

        config = Config.builder().build();
    }

    @Test
    public void clientKey() {
        assertEquals(mainClientKey, config.getClientKey());
    }

    @Test
    public void isProduction() {
        assertEquals(config.isProduction(), Midtrans.isProduction());
    }

    @Test
    public void enableLog() {
        assertEquals(config.isEnabledLog(), Midtrans.enableLog());
    }

    @Test
    public void idempotencyKey() {
        assertNull(config.getPaymentIdempotencyKey());
        assertNull(config.getIrisIdempotencyKey());
    }

    @Test
    public void appendOverrideNotification() {
        assertEquals(config.getPaymentAppendNotification(), Midtrans.getPaymentAppendNotification());
        assertEquals(config.getPaymentOverrideNotification(), Midtrans.getPaymentOverrideNotification());
    }

    @Test
    public void serverKey() {
        assertEquals(config.getServerKey(), mainServerKey);


        assertNull(config.getCustomHeaders());
        assertEquals(config.getProxyConfig(), Midtrans.getProxyConfig());
    }


    @Test
    public void networkPreferences() {
        assertEquals(config.getConnectionTimeout(), Midtrans.getConnectTimeout());
        assertEquals(config.getReadTimeout(), Midtrans.getReadTimeout());
        assertEquals(config.getMaxConnectionPool(), Midtrans.getMaxConnectionPool());
        assertEquals(config.getKeepAliveDuration(), Midtrans.getKeepAliveDuration());
        assertEquals(config.getHttpClientTimeUnit(), Midtrans.getHttpClientTimeUnit());
    }

    @Test
    public void customHeaders() {
        assertNull(config.getCustomHeaders());
    }

    @Test
    public void proxy() {
        assertEquals(config.getProxyConfig(), Midtrans.getProxyConfig());
    }

    @AfterAll
    public static void resetConfig() {
        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;
        Midtrans.isProduction = false;
        Midtrans.enableLog = true;
        Midtrans.setReadTimeout(READ_TIMEOUT);
        Midtrans.setConnectTimeout(CONNECT_TIMEOUT);
        Midtrans.setKeepAliveDuration(KEEP_ALIVE_DURATION);
        Midtrans.setMaxConnectionPool(CONNECTION_POOL_SIZE);
        Midtrans.setWriteTimeout(WRITE_TIMEOUT);
        Midtrans.setHttpClientTimeUnit(TimeUnit.SECONDS);
        Midtrans.setProxyConfig(null);

        config = Config.builder().build();
    }

}

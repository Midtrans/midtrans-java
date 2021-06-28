package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.proxy.ProxyConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigViaBuilderTest {

    @Test
    public void configMinimal() {
        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;

        Config config = Config.getGlobalConfig();

        assertEquals(config.getServerKey(), mainServerKey);
        assertEquals(config.getClientKey(), mainClientKey);
        assertEquals(config.isProduction(), Midtrans.isProduction());
        assertEquals(config.isEnabledLog(), Midtrans.enableLog());
        assertNull(config.getPaymentIdempotencyKey());
        assertNull(config.getIrisIdempotencyKey());
        assertEquals(config.getPaymentAppendNotification(), Midtrans.getPaymentAppendNotification());
        assertEquals(config.getPaymentOverrideNotification(), Midtrans.getPaymentOverrideNotification());
        assertEquals(config.getConnectionTimeout(), Midtrans.getConnectTimeout());
        assertEquals(config.getReadTimeout(), Midtrans.getReadTimeout());
        assertEquals(config.getMaxConnectionPool(), Midtrans.getMaxConnectionPool());
        assertEquals(config.getKeepAliveDuration(), Midtrans.getKeepAliveDuration());
        assertEquals(config.getHttpClientTimeUnit(), Midtrans.getHttpClientTimeUnit());
        assertNull(config.getCustomHeaders());
        assertEquals(config.getProxyConfig(), Midtrans.getProxyConfig());
    }

    @Test
    public void networkPreferences() {
        Config config1 = Config.builder()
                .setConnectionTimeout(CONNECT_TIMEOUT)
                .setKeepAliveDuration(KEEP_ALIVE_DURATION)
                .setWriteTimeout(WRITE_TIMEOUT)
                .setReadTimeout(READ_TIMEOUT)
                .setMaxConnectionPoolSize(CONNECTION_POOL_SIZE)
                .build();

        assertEquals(config1.getConnectionTimeout(), CONNECT_TIMEOUT);
        assertEquals(config1.getKeepAliveDuration(), KEEP_ALIVE_DURATION);
        assertEquals(config1.getWriteTimeout(), WRITE_TIMEOUT);
        assertEquals(config1.getReadTimeout(), READ_TIMEOUT);
        assertEquals(config1.getMaxConnectionPool(), CONNECTION_POOL_SIZE);
        assertEquals(config1.getHttpClientTimeUnit(), TimeUnit.SECONDS);

        Config config2 = Config.builder()
                .setConnectionTimeout(-100)
                .setKeepAliveDuration(-200)
                .setWriteTimeout(-300)
                .setReadTimeout(-400)
                .setMaxConnectionPoolSize(-500)
                .build();

        assertEquals(config2.getConnectionTimeout(), DEFAULT_CONNECT_TIMEOUT);
        assertEquals(config2.getKeepAliveDuration(), DEFAULT_KEEP_ALIVE_DURATION);
        assertEquals(config2.getWriteTimeout(), DEFAULT_WRITE_TIMEOUT);
        assertEquals(config2.getReadTimeout(), DEFAULT_READ_TIMEOUT);
        assertEquals(config2.getMaxConnectionPool(), DEFAULT_MAX_CONNECTION_POOL_SIZE);
        assertEquals(config2.getHttpClientTimeUnit(), TimeUnit.SECONDS);

        // Set network preferences using option timeunit
        Config config3 = Config.builder()
                .setConnectionTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .setKeepAliveDuration(KEEP_ALIVE_DURATION, TimeUnit.MILLISECONDS)
                .setWriteTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .setReadTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        assertEquals(config3.getConnectionTimeout(), CONNECT_TIMEOUT);
        assertEquals(config3.getKeepAliveDuration(), KEEP_ALIVE_DURATION);
        assertEquals(config3.getWriteTimeout(), WRITE_TIMEOUT);
        assertEquals(config3.getReadTimeout(), READ_TIMEOUT);
        assertEquals(config3.getHttpClientTimeUnit(), TimeUnit.MILLISECONDS);

        Config config4 = Config.builder()
                .setConnectionTimeout(-100, TimeUnit.MILLISECONDS)
                .setKeepAliveDuration(-200, TimeUnit.MILLISECONDS)
                .setWriteTimeout(-300, TimeUnit.MILLISECONDS)
                .setReadTimeout(-400, TimeUnit.MILLISECONDS)
                .build();

        assertEquals(config4.getConnectionTimeout(), DEFAULT_CONNECT_TIMEOUT);
        assertEquals(config4.getKeepAliveDuration(), DEFAULT_KEEP_ALIVE_DURATION);
        assertEquals(config4.getWriteTimeout(), DEFAULT_WRITE_TIMEOUT);
        assertEquals(config4.getReadTimeout(), DEFAULT_READ_TIMEOUT);
        assertEquals(config4.getHttpClientTimeUnit(), TimeUnit.MILLISECONDS);
    }

    @Test
    public void paymentAppendOverrideNotification() {
        Config config = Config.builder()
                .setPaymentAppendNotification(APPEND_NOTIFICATION)
                .setPaymentOverrideNotification(OVERRIDE_NOTIFICATION)
                .build();

        assertEquals(APPEND_NOTIFICATION, config.getPaymentAppendNotification());
        assertEquals(OVERRIDE_NOTIFICATION, config.getPaymentOverrideNotification());
    }

    @Test
    public void proxyConfig() {
        ProxyConfig proxyConfig1 = new ProxyConfig(PROXY_HOTS, PROXY_PORT, PROXY_USERNAME, PROXY_PASSWORD);
        Config config1 = Config.builder()
                .setProxyConfig(proxyConfig1)
                .build();

        assertEquals(proxyConfig1, config1.getProxyConfig());
        assertEquals(PROXY_HOTS, config1.getProxyConfig().getHost());
        assertEquals(PROXY_PORT, config1.getProxyConfig().getPort());
        assertEquals(PROXY_USERNAME, config1.getProxyConfig().getUsername());
        assertEquals(PROXY_PASSWORD, config1.getProxyConfig().getPassword());

        ProxyConfig proxyConfig2 = ProxyConfig.builder()
                .setHost(PROXY_HOTS)
                .setPort(PROXY_PORT)
                .setUsername(PROXY_USERNAME)
                .setPassword(PROXY_PASSWORD)
                .build();

        Config config2 = Config.builder()
                .setProxyConfig(proxyConfig2)
                .build();

        assertEquals(proxyConfig2, config2.getProxyConfig());
        assertEquals(PROXY_HOTS, config2.getProxyConfig().getHost());
        assertEquals(PROXY_PORT, config2.getProxyConfig().getPort());
        assertEquals(PROXY_USERNAME, config2.getProxyConfig().getUsername());
        assertEquals(PROXY_PASSWORD, config2.getProxyConfig().getPassword());
    }

    @AfterAll
    public static void resetConfig() {
        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;
        Midtrans.isProduction = false;
        Midtrans.setReadTimeout(READ_TIMEOUT);
        Midtrans.setConnectTimeout(CONNECT_TIMEOUT);
        Midtrans.setKeepAliveDuration(KEEP_ALIVE_DURATION);
        Midtrans.setMaxConnectionPool(CONNECTION_POOL_SIZE);
        Midtrans.setWriteTimeout(WRITE_TIMEOUT);
        Midtrans.setHttpClientTimeUnit(TimeUnit.SECONDS);
        Midtrans.setProxyConfig(null);
    }


}
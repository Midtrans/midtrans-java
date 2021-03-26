package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.proxy.ProxyConfig;
import org.junit.Before;
import org.junit.Test;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.Assert.*;

public class ConfigTest {

    private Config config;

    @Before
    public void setUp() {
        config = new Config(mainServerKey, mainClientKey, isProduction);
    }

    @Test
    public void setSERVER_KEY() {
        config.setSERVER_KEY("TEST SET SERVER KEY");
        assertEquals("TEST SET SERVER KEY", config.getSERVER_KEY());
    }

    @Test
    public void setCLIENT_KEY() {
        config.setCLIENT_KEY("TEST SET CLIENT KEY");
        assertEquals("TEST SET CLIENT KEY", config.getCLIENT_KEY());
    }

    @Test
    public void setProduction() {
        config.setProduction(true);
        assertEquals(true, config.isProduction());
    }

    @Test
    public void setConnetionTimeout() {
        config.setConnectionTimeout(20);
        assertEquals(20, config.getConnectionTimeout());
    }

    @Test
    public void setEnabledLog() {
        config.setEnabledLog(true);
        assertEquals(true, config.isEnabledLog());
    }

    @Test
    public void setReadTimeOut() {
        config.setReadTimeout(200);
        assertEquals(200, config.getReadTimeout());
    }

    @Test
    public void setWriteTimeOut() {
        config.setWriteTimeout(200);
        assertEquals(200, config.getWriteTimeout());
        config.setWriteTimeout(-200);
        assertEquals(10, config.getWriteTimeout());
    }

    @Test
    public void setProxyConfig() {
        ProxyConfig proxyConfig = new ProxyConfig("localhost", 8080, "username", "password" );
        config.setProxyConfig(proxyConfig);
        assertEquals("localhost", config.getProxyConfig().getHost());
        assertEquals(8080, config.getProxyConfig().getPort());
        assertEquals("username", config.getProxyConfig().getUsername());
        assertEquals("password", config.getProxyConfig().getPassword());
    }

    @Test
    public void setConnectionPool() {
        config.setMaxConnectionPool(20);
        assertEquals(20, config.getMaxConnectionPool());
        config.setMaxConnectionPool(-10);
        assertEquals(16, config.getMaxConnectionPool());
    }

    @Test
    public void setKeepAliveDuration() {
        config.setKeepAliveDuration(100);
        assertEquals(100, config.getKeepAliveDuration());
        config.setKeepAliveDuration(-200);
        assertEquals(300, config.getKeepAliveDuration());
    }

    @Test
    public void setIrisIdempotencyKey() {
        config.setIrisIdempotencyKey("IRIS-IDEMPOTENCY-KEY");
        assertEquals("IRIS-IDEMPOTENCY-KEY", config.getIrisIdempotencyKey());
    }

    @Test
    public void setPaymentIdempotencyKey() {
        config.setPaymentIdempotencyKey("PAYMENT-IDEMPOTENCY-KEY");
        assertEquals("PAYMENT-IDEMPOTENCY-KEY", config.getPaymentIdempotencyKey());
    }

    @Test
    public void paymentAppendNotification() {
        config.paymentAppendNotification("http://example.com/");
        assertEquals("http://example.com/", config.getPaymentAppendNotification());
    }

    @Test
    public void paymentOverrideNotification() {
        config.paymentOverrideNotification("http://example.com/");
        assertEquals("http://example.com/", config.getPaymentOverrideNotification());
    }

    @Test
    public void setIrisMerchantKey() {
        config.setIRIS_MERCHANT_KEY("IRIS-MERCHANT-KEY");
        assertEquals(config.getIRIS_MERCHANT_KEY(), "IRIS-MERCHANT-KEY");
    }

    @Test
    public void getSERVER_KEY() {
        assertEquals(mainServerKey, config.getSERVER_KEY());
    }

    @Test
    public void getCLIENT_KEY() {
        assertEquals(mainClientKey, config.getCLIENT_KEY());
    }

    @Test
    public void getCoreApiURL() {
        config.getCoreApiURL();
        if (isProduction) {
            assertEquals(COREAPI_PRODUCTION_BASE_URL, config.getBASE_URL());
        } else {
            assertEquals(COREAPI_SANDBOX_BASE_URL, config.getBASE_URL());
        }
    }

    @Test
    public void getSnapApiURL() {
        config.getSnapApiURL();
        if (isProduction) {
            assertEquals(SNAP_PRODUCTION_BASE_URL, config.getBASE_URL());
        } else {
            assertEquals(SNAP_SANDBOX_BASE_URL, config.getBASE_URL());
        }
    }

    @Test
    public void getIrisApiURL() {
        config.getIrisApiURL();
        if (isProduction) {
            assertEquals(IRIS_PRODUCTION_BASE_URL, config.getBASE_URL());
        } else {
            assertEquals(IRIS_SANDBOX_BASE_URL, config.getBASE_URL());
        }
    }
}
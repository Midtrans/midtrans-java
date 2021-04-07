package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.proxy.ProxyConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigTest {

    private Config config;

    @Before
    public void setUp() {
        config = new Config(mainServerKey, mainClientKey, isProduction);
    }


    @Test
    public void setSERVER_KEY() {
        config.setSERVER_KEY(secondServerKey);
        assertEquals(secondServerKey, config.getSERVER_KEY());
    }

    @Test
    public void setCLIENT_KEY() {
        config.setCLIENT_KEY(secondClientKey);
        assertEquals(secondClientKey, config.getCLIENT_KEY());
    }

    @Test
    public void setProduction() {
        config.setProduction(true);
        assertTrue(config.isProduction());
    }

    @Test
    public void setEnabledLog() {
        config.setEnabledLog(true);
        assertTrue(config.isEnabledLog());
    }

    @Test
    public void setConnectTimeout() {
        /*
          Set connect timeout with minus value
          */
        config.setConnectionTimeout(-200);
        assertEquals(DEFAULT_CONNECT_TIMEOUT, config.getConnectionTimeout());
        assertEquals(TimeUnit.SECONDS, config.getHttpClientTimeUnit());

        /*
          Set connect timeout with default TimeUnit SECONDS
          */
        config.setConnectionTimeout(CONNECT_TIMEOUT);
        assertEquals(CONNECT_TIMEOUT, config.getConnectionTimeout());
        assertEquals(TimeUnit.SECONDS, config.getHttpClientTimeUnit());


        /*
          Set connect timeout with options TimeUnit MILLISECONDS
          */
        config.setConnectionTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        assertEquals(CONNECT_TIMEOUT, config.getConnectionTimeout());
        assertEquals(TimeUnit.MILLISECONDS, config.getHttpClientTimeUnit());
    }

    @Test
    public void setReadTimeOut() {
        /*
          Set read timeout with minus value
          */
        config.setReadTimeout(-200);
        assertEquals(DEFAULT_READ_TIMEOUT, config.getReadTimeout());
        assertEquals(TimeUnit.SECONDS, config.getHttpClientTimeUnit());

        /*
          Set read timeout with default TimeUnit SECONDS
          */
        config.setReadTimeout(READ_TIMEOUT);
        assertEquals(READ_TIMEOUT, config.getReadTimeout());
        assertEquals(TimeUnit.SECONDS, config.getHttpClientTimeUnit());

        /*
          Set read timeout with options TimeUnit MILLISECONDS
          */
        config.setReadTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        assertEquals(TimeUnit.MILLISECONDS, config.getHttpClientTimeUnit());
        assertEquals(READ_TIMEOUT, config.getReadTimeout());
    }

    @Test
    public void setWriteTimeOut() {
        /*
          Set write timeout with minus value
        */
        config.setWriteTimeout(-200);
        assertEquals(DEFAULT_WRITE_TIMEOUT, config.getWriteTimeout());
        assertEquals(TimeUnit.SECONDS, config.getHttpClientTimeUnit());

        /*
         Set write timeout with default TimeUnit SECONDS
        */
        config.setWriteTimeout(WRITE_TIMEOUT);
        assertEquals(WRITE_TIMEOUT, config.getWriteTimeout());
        assertEquals(TimeUnit.SECONDS, config.getHttpClientTimeUnit());

        /*
         Set write timeout with options TimeUnit MILLISECONDS
        */
        config.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        assertEquals(WRITE_TIMEOUT, config.getWriteTimeout());
        assertEquals(TimeUnit.MILLISECONDS, config.getHttpClientTimeUnit());
    }

    @Test
    public void setKeepAliveDuration() {
        /*
         Set keep alive duration with minus value
        */
        config.setKeepAliveDuration(-200);
        assertEquals(DEFAULT_KEEP_ALIVE_DURATION, config.getKeepAliveDuration());
        assertEquals(TimeUnit.SECONDS, config.getHttpClientTimeUnit());

        /*
         Set keep alive duration with default TimeUnit SECONDS
        */
        config.setKeepAliveDuration(KEEP_ALIVE_DURATION);
        assertEquals(KEEP_ALIVE_DURATION, config.getKeepAliveDuration());
        assertEquals(TimeUnit.SECONDS, config.getHttpClientTimeUnit());

        /*
         Set keep alive duration with options TimeUnit MILLISECONDS
        */
        config.setKeepAliveDuration(KEEP_ALIVE_DURATION, TimeUnit.MILLISECONDS);
        assertEquals(KEEP_ALIVE_DURATION, config.getKeepAliveDuration());
        assertEquals(TimeUnit.MILLISECONDS, config.getHttpClientTimeUnit());

    }


    @Test
    public void setMaxConnectionPool() {
        /*
         Set max connection pool with minus value
        */
        config.setMaxConnectionPool(-10);
        assertEquals(DEFAULT_MAX_CONNECTION_POOL_SIZE, config.getMaxConnectionPool());

        /*
         Set max connection pool
        */
        config.setMaxConnectionPool(CONNECTION_POOL_SIZE);
        assertEquals(CONNECTION_POOL_SIZE, config.getMaxConnectionPool());
    }

    @Test
    public void setProxyConfig() {
        ProxyConfig proxyConfig = new ProxyConfig(PROXY_HOTS, PROXY_PORT, PROXY_USERNAME, PROXY_PASSWORD);
        config.setProxyConfig(proxyConfig);
        assertEquals(PROXY_HOTS, config.getProxyConfig().getHost());
        assertEquals(PROXY_PORT, config.getProxyConfig().getPort());
        assertEquals(PROXY_USERNAME, config.getProxyConfig().getUsername());
        assertEquals(PROXY_PASSWORD, config.getProxyConfig().getPassword());
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
        config.paymentAppendNotification(APPEND_NOTIFICATION);
        assertEquals(APPEND_NOTIFICATION, config.getPaymentAppendNotification());
    }

    @Test
    public void paymentOverrideNotification() {
        config.paymentOverrideNotification(OVERRIDE_NOTIFICATION);
        assertEquals(OVERRIDE_NOTIFICATION, config.getPaymentOverrideNotification());
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
        if (isProduction) {
            assertEquals(COREAPI_PRODUCTION_BASE_URL, config.getCoreApiURL());
        } else {
            assertEquals(COREAPI_SANDBOX_BASE_URL, config.getCoreApiURL());
        }
    }

    @Test
    public void getSnapApiURL() {
        if (isProduction) {
            assertEquals(SNAP_PRODUCTION_BASE_URL, config.getSnapApiURL());
        } else {
            assertEquals(SNAP_SANDBOX_BASE_URL, config.getSnapApiURL());
        }
    }

    @Test
    public void getIrisApiURL() {
        if (isProduction) {
            assertEquals(IRIS_PRODUCTION_BASE_URL, config.getIrisApiURL());
        } else {
            assertEquals(IRIS_SANDBOX_BASE_URL, config.getIrisApiURL());
        }
    }
}
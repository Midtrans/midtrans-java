package com.midtrans;

import com.midtrans.proxy.ProxyConfig;

import java.util.concurrent.TimeUnit;

public class Midtrans {

    public static volatile String serverKey;
    public static volatile String clientKey;

    public static volatile boolean isProduction;
    public static volatile boolean enableLog;

    private static volatile String paymentAppendNotification;
    private static volatile String paymentOverrideNotification;

    private static volatile int connectTimeout = ConfigBuilder.DEFAULT_CONNECT_TIMEOUT;
    private static volatile int readTimeout = ConfigBuilder.DEFAULT_READ_TIMEOUT;
    private static volatile int maxConnectionPool = ConfigBuilder.DEFAULT_MAX_CONNECTION_POOL_SIZE;
    private static volatile int keepAliveDuration = ConfigBuilder.DEFAULT_KEEP_ALIVE_DURATION;
    private static volatile int writeTimeout = ConfigBuilder.DEFAULT_WRITE_TIMEOUT;
    private static volatile TimeUnit httpClientTimeUnit;

    private static final String SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/";
    private static final String PRODUCTION_BASE_URL = "https://api.midtrans.com/";

    private static final String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/snap/v1/transactions";
    private static final String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/snap/v1/transactions";

    private static final String IRIS_PRODUCTION_BASE_URL = "https://app.midtrans.com/iris/api/v1/";
    private static final String IRIS_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/iris/api/v1/";

    private static volatile ProxyConfig proxyConfig;

    public static boolean isProduction() {
        return isProduction;
    }

    public static String getSandboxBaseUrl() {
        return SANDBOX_BASE_URL;
    }

    public static String getProductionBaseUrl() {
        return PRODUCTION_BASE_URL;
    }

    public static String getSnapProductionBaseUrl() {
        return SNAP_PRODUCTION_BASE_URL;
    }

    public static String getSnapSandboxBaseUrl() {
        return SNAP_SANDBOX_BASE_URL;
    }

    public static String getIrisProductionBaseUrl() {
        return IRIS_PRODUCTION_BASE_URL;
    }

    public static String getIrisSandboxBaseUrl() {
        return IRIS_SANDBOX_BASE_URL;
    }

    public static String getPaymentAppendNotification() {
        return paymentAppendNotification;
    }

    public static String getPaymentOverrideNotification() {
        return paymentOverrideNotification;
    }

    public static int getConnectTimeout() {
        return (connectTimeout < -1) ? ConfigBuilder.DEFAULT_CONNECT_TIMEOUT : connectTimeout;
    }

    public static int getReadTimeout() {
        return (readTimeout < -1) ? ConfigBuilder.DEFAULT_READ_TIMEOUT : readTimeout;
    }

    public static int getMaxConnectionPool() {
        return (maxConnectionPool < -1) ? ConfigBuilder.DEFAULT_MAX_CONNECTION_POOL_SIZE : maxConnectionPool;
    }

    public static int getKeepAliveDuration() {
        return (keepAliveDuration < -1) ? ConfigBuilder.DEFAULT_KEEP_ALIVE_DURATION : keepAliveDuration;
    }

    public static int getWriteTimeout() {
        return (writeTimeout < -1) ? ConfigBuilder.DEFAULT_WRITE_TIMEOUT : writeTimeout;
    }

    public static TimeUnit getHttpClientTimeUnit() {
        return (httpClientTimeUnit != null) ? httpClientTimeUnit : TimeUnit.SECONDS;
    }

    public static String getServerKey() {
        return serverKey;
    }

    public static String getClientKey() {
        return clientKey;
    }

    public static void paymentAppendNotification(String xAppendNotification) {
        paymentAppendNotification = xAppendNotification;
    }

    public static void paymentOverrideNotification(String xOverrideNotification) {
        paymentOverrideNotification = xOverrideNotification;
    }

    public static ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public static void setConnectTimeout(int timeout) {
        connectTimeout = timeout;
    }

    public static void setReadTimeout(int timeout) {
        readTimeout = timeout;
    }

    public static void setMaxConnectionPool(int connectionPool) {
        maxConnectionPool = connectionPool;
    }

    public static void setKeepAliveDuration(int aliveDuration) {
        keepAliveDuration = aliveDuration;
    }

    public static void setWriteTimeout(int timeout) {
        writeTimeout = timeout;
    }

    public static void setHttpClientTimeUnit(TimeUnit timeUnit) {
        httpClientTimeUnit = timeUnit;
    }

    public static boolean enableLog() {
        return enableLog;
    }

    public static void setProxyConfig(ProxyConfig proxyConfig) {
        Midtrans.proxyConfig = proxyConfig;
    }
}

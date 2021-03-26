package com.midtrans.v2;

import com.midtrans.proxy.ProxyConfig;

public class Midtrans {

    public static volatile String serverKey;
    public static volatile String clientKey;

    public static volatile boolean isProduction;
    public static volatile boolean enableLog;

    private static volatile String paymentAppendNotification;
    private static volatile String paymentOverrideNotification;

    private static volatile int connectTimeout = Midtrans.DEFAULT_CONNECT_TIMEOUT;
    private static volatile int readTimeout = Midtrans.DEFAULT_READ_TIMEOUT;

    private static int maxConnectionPool = Midtrans.DEFAULT_MAX_CONNECTION_POOL_SIZE;
    private static int keepAliveDuration = Midtrans.DEFAULT_KEEP_ALIVE_DURATION;
    private static int writeTimeout = Midtrans.DEFAULT_WRITE_TIMEOUT;

    public static final int DEFAULT_CONNECT_TIMEOUT = 10; //SECOND
    public static final int DEFAULT_READ_TIMEOUT = 10; //SECOND

    public static final int DEFAULT_WRITE_TIMEOUT = 10; //SECOND
    public static final int DEFAULT_MAX_CONNECTION_POOL_SIZE = 16;
    public static final int DEFAULT_KEEP_ALIVE_DURATION = 300; //SECOND

    private static final String SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/";
    private static final String PRODUCTION_BASE_URL = "https://api.midtrans.com/";

    private static final String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/";
    private static final String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/";

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

    public static String getPaymentAppendNotification() { return paymentAppendNotification; }

    public static String getPaymentOverrideNotification() { return paymentOverrideNotification; }

    public static int getConnectTimeout() {
        return (connectTimeout < -1) ? DEFAULT_CONNECT_TIMEOUT : connectTimeout;
    }

    public static int getReadTimeout() { return (readTimeout < -1) ? DEFAULT_READ_TIMEOUT : readTimeout; }

    public static int getMaxConnectionPool() { return ( maxConnectionPool < -1) ? DEFAULT_MAX_CONNECTION_POOL_SIZE : maxConnectionPool; }

    public static int getKeepAliveDuration() { return ( keepAliveDuration < -1) ? DEFAULT_KEEP_ALIVE_DURATION : keepAliveDuration; }

    public static int getWriteTimeout() { return ( writeTimeout < -1) ? DEFAULT_WRITE_TIMEOUT : writeTimeout; }

    public static String getServerKey() {
        return serverKey;
    }

    public static String getClientKey() {
        return clientKey;
    }

    public static void paymentAppendNotification(String xAppendNotification) {
        Midtrans.paymentAppendNotification = xAppendNotification;
    }

    public static void paymentOverrideNotification(String xOverrideNotification) {
        Midtrans.paymentOverrideNotification = xOverrideNotification;
    }

    public static ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public static void setConnectTimeout(int connectTimeout) {
        Midtrans.connectTimeout = connectTimeout;
    }

    public static void setReadTimeout(int readTimeout) {
        Midtrans.readTimeout = readTimeout;
    }

    public static void setMaxConnectionPool(int maxConnectionPool) {
        Midtrans.maxConnectionPool = maxConnectionPool;
    }

    public static void setKeepAliveDuration(int keepAliveDuration) {
        Midtrans.keepAliveDuration = keepAliveDuration;
    }

    public static void setWriteTimeout(int writeTimeout) {
        Midtrans.writeTimeout = writeTimeout;
    }

    public static boolean enableLog() { return enableLog; }

    public static void setProxyConfig(ProxyConfig proxyConfig) {
        Midtrans.proxyConfig = proxyConfig;
    }
}

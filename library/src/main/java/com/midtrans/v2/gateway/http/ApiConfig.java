package com.midtrans.v2.gateway.http;

import com.midtrans.proxy.ProxyConfig;
import com.midtrans.v2.Midtrans;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * Midtrans Api Config class v2,
 *
 */
@EqualsAndHashCode(callSuper = false)
public class ApiConfig {
    private final String serverKey;
    private final String clientKey;

    private final boolean isProduction;
    private final boolean enableLog;

    private final String paymentIdempotencyKey;
    private final String irisIdempotencykey;

    private final String paymentAppendNotification;
    private final String paymentOverrideNotification;

    private final int connectTimeout;
    private final int readTimeout;
    private final int maxConnectionPool;
    private int keepAliveDuration;
    private final int writeTimeout;

    private final Map<String, String> customHeaders;
    private final ProxyConfig proxyConfig;


    public static ApiConfig getDefaultConfig() {
        return new ApiConfig(
                Midtrans.getServerKey(),
                Midtrans.getClientKey(),
                Midtrans.isProduction,
                Midtrans.enableLog,
                null,
                null,
                Midtrans.getPaymentAppendNotification(),
                Midtrans.getPaymentOverrideNotification(),
                Midtrans.getConnectTimeout(),
                Midtrans.getReadTimeout(),
                Midtrans.getMaxConnectionPool(),
                Midtrans.getKeepAliveDuration(),
                Midtrans.getWriteTimeout(),
                null,
                null
        );
    }

    public ApiConfig(
            String serverKey,
            String clientKey,
            boolean isProduction,
            boolean enableLog,
            String paymentIdempotencyKey,
            String irisIdempotencykey,
            String paymentAppendNotification,
            String paymentOverrideNotification,
            int connectTimeout,
            int readTimeout,
            int maxConnectionPool,
            int keepAliveDuration,
            int writeTimeout,
            Map<String, String> customHeaders,
            ProxyConfig proxyConfig
    ) {
        this.serverKey = serverKey;
        this.clientKey = clientKey;
        this.isProduction = isProduction;
        this.enableLog = enableLog;
        this.paymentIdempotencyKey = paymentIdempotencyKey;
        this.irisIdempotencykey = irisIdempotencykey;
        this.paymentAppendNotification = paymentAppendNotification;
        this.paymentOverrideNotification = paymentOverrideNotification;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxConnectionPool = maxConnectionPool;
        this.keepAliveDuration = keepAliveDuration;
        this.writeTimeout = writeTimeout;
        this.customHeaders = customHeaders;
        this.proxyConfig = proxyConfig;
    }

    public int getConnectTimeout() {
        return (connectTimeout < -1) ? Midtrans.DEFAULT_CONNECT_TIMEOUT : connectTimeout;
    }

    public int getReadTimeout() {
        return (readTimeout < -1) ? Midtrans.DEFAULT_READ_TIMEOUT : readTimeout;
    }

    public int getMaxConnectionPool() {
        return (maxConnectionPool < -1) ? Midtrans.DEFAULT_MAX_CONNECTION_POOL_SIZE : maxConnectionPool;
    }

    public int getKeepAliveDuration() {
        return (keepAliveDuration < -1) ? Midtrans.DEFAULT_KEEP_ALIVE_DURATION : keepAliveDuration;
    }

    public int getWriteTimeout() {
        return (writeTimeout < -1) ? Midtrans.DEFAULT_WRITE_TIMEOUT : writeTimeout;
    }

    public String getServerKey() {
        return serverKey;
    }

    public String getClientKey() {
        return clientKey;
    }

    public boolean isProduction() {
        return isProduction;
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public String getPaymentIdempotencyKey() {
        return paymentIdempotencyKey;
    }

    public String getIrisIdempotencykey() {
        return irisIdempotencykey;
    }

    public String getPaymentAppendNotification() {
        return paymentAppendNotification;
    }

    public String getPaymentOverrideNotification() {
        return paymentOverrideNotification;
    }

    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public static ApiConfigBuilder builder() {
        return new ApiConfigBuilder();
    }

    public static class ApiConfigBuilder {

        private String serverKey;
        private String clientKey;

        private boolean isProduction;
        private boolean enableLog;

        private String paymentIdempotencyKey;
        private String irisIdempotencykey;

        private String paymentAppendNotification;
        private String paymentOverrideNotification;

        private Integer connectTimeout;
        private Integer readTimeout;
        private Integer maxConnectionPool;
        private Integer keepAliveDuration;
        private Integer writeTimeout;

        private Map<String, String> customHeaders;
        private ProxyConfig proxyConfig;

        public ApiConfigBuilder() {
            this.serverKey = Midtrans.getServerKey();
            this.clientKey = Midtrans.getClientKey();
            this.isProduction = Midtrans.isProduction();
            this.paymentIdempotencyKey = null;
            this.irisIdempotencykey = null;
            this.paymentAppendNotification = Midtrans.getPaymentAppendNotification();
            this.paymentOverrideNotification = Midtrans.getPaymentOverrideNotification();
            this.connectTimeout = Midtrans.getConnectTimeout();
            this.readTimeout = Midtrans.getReadTimeout();
            this.maxConnectionPool = Midtrans.getMaxConnectionPool();
            this.keepAliveDuration = Midtrans.getKeepAliveDuration();
            this.writeTimeout = Midtrans.getWriteTimeout();
            this.customHeaders = null;
            this.proxyConfig = null;
        }

        public ApiConfigBuilder setServerKey(String serverKey) {
            this.serverKey = serverKey;
            return this;
        }

        public ApiConfigBuilder setClientKey(String clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        public ApiConfigBuilder isProduction(boolean isProduction) {
            this.isProduction = isProduction;
            return this;
        }

        public ApiConfigBuilder enableLog(boolean enableLog) {
            this.enableLog = enableLog;
            return this;
        }

        public ApiConfigBuilder setPaymentIdempotencyKey(String paymentIdempotencyKey) {
            this.paymentIdempotencyKey = paymentIdempotencyKey;
            return this;
        }

        public ApiConfigBuilder setIrisIdempotencykey(String irisIdempotencykey) {
            this.irisIdempotencykey = irisIdempotencykey;
            return this;
        }

        public ApiConfigBuilder setPaymentOverrideNotification(String xOverrideNotification) {
            this.paymentOverrideNotification = xOverrideNotification;
            return this;
        }

        public ApiConfigBuilder setPaymentAppendNotification(String xAppendNotification) {
            this.paymentAppendNotification = xAppendNotification;
            return this;
        }

        public ApiConfigBuilder setConnectTimeout(Integer connectionTimeout) {
                this.connectTimeout = connectionTimeout;
            return this;
        }

        public ApiConfigBuilder setReadTimeout(Integer readTimeout) {
                this.readTimeout = readTimeout;
            return this;
        }

        public ApiConfigBuilder setMaxConnectionPool(Integer maxConnectionPool) {
            this.maxConnectionPool = maxConnectionPool;
            return this;
        }

        public ApiConfigBuilder setKeepAliveDuration(Integer keepAliveDuration) {
            this.keepAliveDuration = keepAliveDuration;
            return this;
        }

        public ApiConfigBuilder setWriteTimeout(Integer writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public ApiConfigBuilder setCustomHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public ApiConfigBuilder setProxyConfig(ProxyConfig proxyConfig) {
            this.proxyConfig = proxyConfig;
            return this;
        }

        public ApiConfig build() {
            return new ApiConfig(
                    serverKey,
                    clientKey,
                    isProduction,
                    enableLog,
                    paymentIdempotencyKey,
                    irisIdempotencykey,
                    paymentAppendNotification,
                    paymentOverrideNotification,
                    connectTimeout,
                    readTimeout,
                    maxConnectionPool,
                    keepAliveDuration,
                    writeTimeout,
                    customHeaders,
                    proxyConfig
            );
        }
    }
}

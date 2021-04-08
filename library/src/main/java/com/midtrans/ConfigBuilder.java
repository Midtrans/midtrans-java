package com.midtrans;

import com.midtrans.proxy.ProxyConfig;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * {@link Config Config} builder class
 */
@EqualsAndHashCode(callSuper = false)
public class ConfigBuilder {

    private String serverKey;
    private String clientKey;
    private Boolean isProduction;
    private boolean enableLog;

    private int connectionTimeout;
    private int readTimeout;
    private int writeTimeout;
    private int maxConnectionPoolSize;
    private int keepAliveDuration;
    private TimeUnit httpClientTimeUnit;

    private String irisIdempotencyKey;
    private String paymentIdempotencyKey;

    private String xAppendNotification;
    private String xOverrideNotification;

    private ProxyConfig proxyConfig;
    private Map<String, String> customHeaders;


    public static final int DEFAULT_CONNECT_TIMEOUT = 10; //SECOND
    public static final int DEFAULT_READ_TIMEOUT = 10; //SECOND
    public static final int DEFAULT_WRITE_TIMEOUT = 10; //SECOND
    public static final int DEFAULT_MAX_CONNECTION_POOL_SIZE = 16;
    public static final int DEFAULT_KEEP_ALIVE_DURATION = 300; //SECOND


    /**
     * Default constructor ConfigBuilder
     */
    public ConfigBuilder() {
        this.serverKey = Midtrans.getServerKey();
        this.clientKey = Midtrans.getClientKey();
        this.isProduction = Midtrans.isProduction();
        this.enableLog = Midtrans.enableLog();
        this.paymentIdempotencyKey = null;
        this.irisIdempotencyKey = null;
        this.xAppendNotification = Midtrans.getPaymentAppendNotification();
        this.xOverrideNotification = Midtrans.getPaymentOverrideNotification();
        this.connectionTimeout = Midtrans.getConnectTimeout();
        this.readTimeout = Midtrans.getReadTimeout();
        this.maxConnectionPoolSize = Midtrans.getMaxConnectionPool();
        this.keepAliveDuration = Midtrans.getKeepAliveDuration();
        this.httpClientTimeUnit = Midtrans.getHttpClientTimeUnit();
        this.writeTimeout = Midtrans.getWriteTimeout();
        this.customHeaders = null;
        this.proxyConfig = Midtrans.getProxyConfig();
    }

    /**
     * set merchant server key with config builder
     *
     * @deprecated
     * This method will delete soon on the next major release.
     * <p> Please use {@link ConfigBuilder#setServerKey(String)} instead.
     *
     * @param SERVER_KEY merchant server key
     * @return {@link ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setSERVER_KEY(String SERVER_KEY) {
        return setServerKey(SERVER_KEY);
    }

    /**
     * Set merchant server key with config builder
     *
     * @param serverKey merchant server key
     * @return {@link ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setServerKey(String serverKey) {
        this.serverKey = serverKey;
        return this;
    }


    /**
     * Set merchant client key configuration
     *
     * @deprecated
     * This method will delete soon on the next major release.
     * <p> Please use {@link ConfigBuilder#setClientKey(String)} instead.
     *
     * @param CLIENT_KEY merchant client key
     * @return {@link ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setCLIENT_KEY(String CLIENT_KEY) {
        return setClientKey(CLIENT_KEY);
    }

    /**
     * Set merchant client key configuration
     *
     * @param clientKey merchant client key
     * @return {@link ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    /**
     * Set environment type configuration
     *
     * @param isProduction true or false
     * @return              {@link ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setIsProduction(Boolean isProduction) {
        this.isProduction = isProduction;
        return this;
    }

    /**
     * Set http client connect timeout with time unit
     *
     * @param connectionTimeout
     * @param timeUnit
     * @return
     */
    public ConfigBuilder setConnectionTimeout(final int connectionTimeout, TimeUnit timeUnit) {
        this.connectionTimeout = connectionTimeout;
        this.httpClientTimeUnit = timeUnit;
        return this;
    }

    /**
     * Set http client connect timeout
     *
     * @param connectionTimeout Http client connect timeout
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setConnectionTimeout(final int connectionTimeout) {
        return setConnectionTimeout(connectionTimeout, null);
    }


    /**
     * Set http client read timeout with time unit
     *
     * @param readTimeout
     * @param timeUnit
     * @return
     */
    public ConfigBuilder setReadTimeout(final int readTimeout, TimeUnit timeUnit) {
        this.readTimeout = readTimeout;
        this.httpClientTimeUnit = timeUnit;
        return this;
    }

    /**
     * Set http client read timeout
     *
     * @param readTimeout Http client read timeout
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setReadTimeout(final int readTimeout) {
        return setReadTimeout(readTimeout, null);
    }

    /**
     * Set http client write timeout with time unit
     *
     * @param writeTimeout
     * @param timeUnit
     * @return
     */
    public ConfigBuilder setWriteTimeout(final int writeTimeout, TimeUnit timeUnit) {
        this.writeTimeout = writeTimeout;
        this.httpClientTimeUnit = timeUnit;
        return this;
    }

    /**
     * Set http client write timeout
     *
     * @param writeTimeout Http client write timeout
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setWriteTimeout(final int writeTimeout) {
        return setWriteTimeout(writeTimeout, null);
    }

    /**
     * Set Midtrans merchant proxy configuration
     * @param proxyConfig   {@link com.midtrans.proxy.ProxyConfig Proxy config}
     * @return              {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
        return this;
    }

    /**
     * Set Midtrans max connection pool size
     * @param connectionPoolSize Max http client connection pool size
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setMaxConnectionPoolSize(final int connectionPoolSize) {
        this.maxConnectionPoolSize = connectionPoolSize;
        return this;
    }

    /**
     * Set Midtrans keep alive durations with time unit
     *
     * @param keepAliveDuration
     * @param timeUnit
     * @return
     */
    public ConfigBuilder setKeepAliveDuration(final int keepAliveDuration, TimeUnit timeUnit) {
        this.keepAliveDuration = keepAliveDuration;
        this.httpClientTimeUnit = timeUnit;
        return this;
    }

    /**
     * Set Midtrans keep alive durations
     *
     * @param keepAliveDuration http client keep alive durations
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setKeepAliveDuration(final int keepAliveDuration) {
        return setKeepAliveDuration(keepAliveDuration, null);
    }

    /**
     * Set Midtrans Iris idempotent key
     *
     * @param irisIdempotencyKey
     * @return
     */
    public ConfigBuilder setIrisIdempotencyKey(final String irisIdempotencyKey) {
        this.irisIdempotencyKey = irisIdempotencyKey;
        return this;
    }

    public ConfigBuilder setPaymentIdempotencyKey(String paymentIdempotencyKey) {
        this.paymentIdempotencyKey = paymentIdempotencyKey;
        return this;
    }

    /**
     * Set Midtrans Payment append notification
     *
     * @param xAppendNotification
     * @return
     */
    public ConfigBuilder setPaymentAppendNotification(final String xAppendNotification) {
        this.xAppendNotification = xAppendNotification;
        return this;
    }

    /**
     * Set Midtrans Payment override notification
     *
     * @param xOverrideNotification
     * @return
     */
    public ConfigBuilder setPaymentOverrideNotification(final String xOverrideNotification) {
        this.xOverrideNotification = xOverrideNotification;
        return this;
    }

    /**
     * Set Custom headers for API Request
     *
     * @param customHeaders custom http client headers
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setCustomHeaders(Map<String, String> customHeaders) {
        this.customHeaders = customHeaders;
        return this;
    }

    /**
     * @param enableLog
     * @return
     */
    public ConfigBuilder enableLog(boolean enableLog) {
        this.enableLog = enableLog;
        return this;
    }

    /**
     * Build Config object from builder
     *
     * @return {@link Config Config}
     */
    public Config build() {
        return new Config(
                serverKey,
                clientKey,
                isProduction,
                enableLog,
                connectionTimeout,
                readTimeout,
                writeTimeout,
                maxConnectionPoolSize,
                keepAliveDuration,
                httpClientTimeUnit,
                irisIdempotencyKey,
                paymentIdempotencyKey,
                xAppendNotification,
                xOverrideNotification,
                proxyConfig,
                customHeaders
        );
    }
}

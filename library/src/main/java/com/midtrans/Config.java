package com.midtrans;

import com.midtrans.proxy.ProxyConfig;

import java.util.Map;

/**
 * Midtrans configuration
 */
public class Config {

    private String SERVER_KEY;
    private String CLIENT_KEY;
    private String IRIS_MERCHANT_KEY;
    private boolean isProduction;

    private String BASE_URL;

    private int connectionTimeout;
    private int readTimeout;
    private int writeTimeout;

    private int maxConnectionPool;
    private int keepAliveDuration;

    private String irisIdempotencyKey = "";
    private String paymentIdempotencyKey = "";

    private String xAppendNotification = "";
    private String xOverrideNotification = "";

    private ProxyConfig proxyConfig;

    private boolean enabledLog;

    private Map<String, String> customHeaders;

    /**
     * Midtrans configuration constructor
     *
     * @param SERVER_KEY   Merchant server-key
     * @param CLIENT_KEY   Merchant client-key
     * @param isProduction Merchant Environment Sandbox or Production
     */
    public Config(String SERVER_KEY, String CLIENT_KEY, boolean isProduction) {
        this.SERVER_KEY = SERVER_KEY;
        this.CLIENT_KEY = CLIENT_KEY;
        this.isProduction = isProduction;
        this.readTimeout = ConfigBuilder.DEFAULT_READ_TIMEOUT;
        this.writeTimeout = ConfigBuilder.DEFAULT_WRITE_TIMEOUT;
        this.connectionTimeout = ConfigBuilder.DEFAULT_CONNECT_TIMEOUT;
        this.maxConnectionPool = ConfigBuilder.DEFAULT_MAX_CONNECTION_POOL_SIZE;
        this.keepAliveDuration = ConfigBuilder.DEFAULT_KEEP_ALIVE_DURATION;
    }

    /**
     * Midtrans configuration constructor
     *
     * @param SERVER_KEY   Merchant server-key
     * @param CLIENT_KEY   Merchant client-key
     * @param isProduction Merchant Environment Sandbox or Production
     * @param connectionTimeout Config for connection timeout
     * @param readTimeout Config for read timeout
     * @param writeTimeout Config for write timeout
     * @param maxConnectionPool value max for connection pool
     * @param keepAliveDuration Durations for Keep alive connection
     */
    public Config(String SERVER_KEY, String CLIENT_KEY, boolean isProduction, int connectionTimeout, int readTimeout, int writeTimeout, int maxConnectionPool, int keepAliveDuration, Map<String, String> customHeaders) {
        this.SERVER_KEY = SERVER_KEY;
        this.CLIENT_KEY = CLIENT_KEY;
        this.isProduction = isProduction;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.maxConnectionPool = maxConnectionPool;
        this.keepAliveDuration = keepAliveDuration;
        this.customHeaders = customHeaders;
    }

    /**
     * Midtrans configuration constructor
     * @param SERVER_KEY   Merchant server-key
     * @param CLIENT_KEY   Merchant client-key
     * @param isProduction Merchant Environment Sandbox or Production
     * @param connectionTimeout Config for connection timeout
     * @param readTimeout Config for read timeout
     * @param writeTimeout Config for write timeout
     * @param proxyConfig
     */
    public Config(String SERVER_KEY, String CLIENT_KEY, boolean isProduction, int connectionTimeout, int readTimeout, int writeTimeout, int maxConnectionPool, int keepAliveDuration ,Map<String, String> customHeaders, ProxyConfig proxyConfig) {
        this.SERVER_KEY = SERVER_KEY;
        this.CLIENT_KEY = CLIENT_KEY;
        this.isProduction = isProduction;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.maxConnectionPool = maxConnectionPool;
        this.keepAliveDuration = keepAliveDuration;
        this.customHeaders = customHeaders;
        this.proxyConfig = proxyConfig;
    }

    /**
     * Get environment type
     *
     * @return boolean
     */
    public boolean isProduction() {
        return isProduction;
    }

    private void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    /**
     * Get BASE_URL
     * @return BASE_URL
     */
    public String getBASE_URL() {
        return BASE_URL;
    }

    /**
     * Get merchant server key
     * @return Merchant server key
     */
    public String getSERVER_KEY() {
        return SERVER_KEY;
    }

    /**
     * Get merchant client key
     * @return Merchant client key
     */
    public String getCLIENT_KEY() {
        return CLIENT_KEY;
    }

    /**
     * Get http client connection timeout
     * @return connection timeout
     */
    public int getConnectionTimeout() {
        if (connectionTimeout < -1) {
            return ConfigBuilder.DEFAULT_CONNECT_TIMEOUT;
        }
        return connectionTimeout;
    }

    /**
     * Get http client read timeout
     * @return read timeout
     */
    public int getReadTimeout() {
        if (readTimeout < -1) {
            return ConfigBuilder.DEFAULT_READ_TIMEOUT;
        }
        return readTimeout;
    }

    /**
     * Get http client write timeout
     * @return write timeout
     */
    public int getWriteTimeout() {
        if (writeTimeout < -1) {
            return ConfigBuilder.DEFAULT_WRITE_TIMEOUT;
        }
        return writeTimeout;
    }

    /**
     * Get http client max connection pool
     * @return max connection pool
     */
    public int getMaxConnectionPool() {
        if (maxConnectionPool < -1) {
            return ConfigBuilder.DEFAULT_MAX_CONNECTION_POOL_SIZE;
        }
        return maxConnectionPool;
    }

    /**
     * Get http client keep alive durations
     * @return keep alive durations
     */
    public int getKeepAliveDuration() {
        if (keepAliveDuration < -1) {
            return ConfigBuilder.DEFAULT_KEEP_ALIVE_DURATION;
        }
        return keepAliveDuration;
    }

    /**
     * Get http client proxy configuration
     * @return Http client {@link com.midtrans.proxy.ProxyConfig proxy configuration}
     */
    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    /**
     * Get enableLog is enabled
     * @return Http client enabledLog
     */
    public boolean isEnabledLog() {
        return enabledLog;
    }

    /**
     * set BASE_URL to CoreAPI_BASE_URL in accordance with the environment type
     */
    public void getCoreApiURL() {
        if (isProduction()) {
            enabledLog = false;
            String COREAPI_PRODUCTION_BASE_URL = "https://api.midtrans.com/";
            setBASE_URL(COREAPI_PRODUCTION_BASE_URL);
        } else {
            enabledLog = true;
            String COREAPI_SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/";
            setBASE_URL(COREAPI_SANDBOX_BASE_URL);
        }
    }

    /**
     * set BASE_URL to SnapAPI_BASE_URL in accordance with the environment type
     */
    public void getSnapApiURL() {
        if (isProduction()) {
            enabledLog = false;
            String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/";
            setBASE_URL(SNAP_PRODUCTION_BASE_URL);
        } else {
            enabledLog = true;
            String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/";
            setBASE_URL(SNAP_SANDBOX_BASE_URL);
        }
    }

    /**
     * set BASE_URL to IRIS_API_BASE_URL in accordance with the environment type
     */
    public void getIrisApiURL() {
        if (isProduction()) {
            enabledLog = false;
            String IRIS_PRODUCTION_BASE_URL = "https://app.midtrans.com/iris/";
            setBASE_URL(IRIS_PRODUCTION_BASE_URL);
        } else {
            enabledLog = true;
            String IRIS_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/iris/";
            setBASE_URL(IRIS_SANDBOX_BASE_URL);
        }
    }

    /**
     * Get Iris Idempotent Key
     *
     * @return iris idempotent key
     */
    public String getIrisIdempotencyKey() {
        return irisIdempotencyKey;
    }

    /**
     * Get Payment Idempotent Key
     *
     * @return payment idempotent key
     */
    public String getPaymentIdempotencyKey() {
        return paymentIdempotencyKey;
    }

    /**
     * Get Payment append notification URL
     *
     * @return URL append notification
     */
    public String getPaymentAppendNotification() {
        return xAppendNotification;
    }

    /**
     * Get Payment override notification URL
     *
     * @return URL override notification
     */
    public String getPaymentOverrideNotification() {
        return xOverrideNotification;
    }


    /**
     * Get Iris merchant key
     *
     * @return String iris merchant key
     */
    public String getIRIS_MERCHANT_KEY() { return IRIS_MERCHANT_KEY; }

    /**
     * Get custom headers for API Request
     *
     * @return Map String custom headers
     */
    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }

    /**
     * set server-key for Basic Authentication while calling Midtrans API from backend.
     *
     * @param SERVER_KEY merchant server key
     */
    public void setSERVER_KEY(final String SERVER_KEY) {
        this.SERVER_KEY = SERVER_KEY;
    }

    /**
     * set client-key used for authorization on frontend API request/configuration.
     *
     * @param CLIENT_KEY merchant client key
     */
    public void setCLIENT_KEY(final String CLIENT_KEY) {
        this.CLIENT_KEY = CLIENT_KEY;
    }

    /**
     * set environment sandbox/production
     *
     * @param production merchant environment type
     */
    public void setProduction(boolean production) {
        isProduction = production;
    }

    /**
     * set enable log for debugging
     *
     * @param enabledLog boolean to turn off/on LOG
     */
    public void setEnabledLog(boolean enabledLog) {
        this.enabledLog = enabledLog;
    }

    /**
     * set connection time out HttpClient
     *
     * @param connectionTimeout int connection time out unit durations is seconds
     */
    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Set read time out HttpClient
     *
     * @param readTimeout int read time out unit durations is seconds
     */
    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * Set write time out HttpClient
     *
     * @param writeTimeout int write time out unit durations is seconds
     */
    public void setWriteTimeout(final int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    /**
     * Set proxy configuration
     *
     * @param proxyConfig Object from ProxyConfig
     */
    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    /**
     * Set max connection pool
     *
     * @param maxConnectionPool int max connection pool
     */
    public void setMaxConnectionPool(int maxConnectionPool) {
        this.maxConnectionPool = maxConnectionPool;
    }

    /**
     * Set keep alive durations
     *
     * @param keepAliveDuration int keep alive duration, unit is seconds
     */
    public void setKeepAliveDuration(int keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

    /**
     * Set Iris idempotent key
     *
     * @param irisIdempotencyKey String for idempotent key
     */
    public void setIrisIdempotencyKey(String irisIdempotencyKey) {
        this.irisIdempotencyKey = irisIdempotencyKey;
    }

    /**
     * Set Payment idempotent key
     *
     * @param paymentIdempotencyKey String for idempotent key
     */
    public void setPaymentIdempotencyKey(String paymentIdempotencyKey) {
        this.paymentIdempotencyKey = paymentIdempotencyKey;
    }

    /**
     * Set Payment append URL notification
     *
     * @param xAppendNotification String URL for append notification, multiple URL can separate with commas
     */
    public void paymentAppendNotification(String xAppendNotification) {
        this.xAppendNotification = xAppendNotification;
    }

    /**
     * Set Override URL notification
     *
     * @param xOverrideNotification String URL for override notification, multiple URL can separate with commas
     */
    public void paymentOverrideNotification(String xOverrideNotification) {
        this.xOverrideNotification = xOverrideNotification;
    }

    /**
     * Set Iris Merchant key
     *
     * @param IRIS_MERCHANT_KEY String iris merchant key
     */
    public void setIRIS_MERCHANT_KEY(String IRIS_MERCHANT_KEY) {
        this.IRIS_MERCHANT_KEY = IRIS_MERCHANT_KEY;
    }


    /**
     * Set Custom headers for API Request
     *
     * @param customHeaders Map string for custom headers
     */
    public void setCustomHeaders(Map<String, String> customHeaders) {
        this.customHeaders = customHeaders;
    }
}
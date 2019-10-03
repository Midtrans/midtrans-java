package com.midtrans;

import com.midtrans.proxy.ProxyConfig;

/**
 * Midtrans configuration
 */
public class Config {

    private String SERVER_KEY;

    private String CLIENT_KEY;

    private boolean isProduction;

    private String BASE_URL;

    private int connectionTimeout;

    private int readTimeout;

    private int writeTimeout;

    private ProxyConfig proxyConfig;

    private boolean enabledLog;

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
        this.connectionTimeout = ConfigBuilder.DEFAULT_CONNECT_TIMEOUT;
        this.readTimeout = ConfigBuilder.DEFAULT_READ_TIMEOUT;
        this.writeTimeout = ConfigBuilder.DEFAULT_WRITE_TIMEOUT;
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
     * @param proxyConfig Config for use http proxy
     */
    public Config(String SERVER_KEY, String CLIENT_KEY, boolean isProduction, int connectionTimeout, int readTimeout, int writeTimeout, ProxyConfig proxyConfig) {
        this.SERVER_KEY = SERVER_KEY;
        this.CLIENT_KEY = CLIENT_KEY;
        this.isProduction = isProduction;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
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
     * Get http client connect timeout
     * @return Http client connect timeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
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
            String COREAPI_PRODUCTION_BASE_URL = "https://api.midtrans.com/v2/";
            setBASE_URL(COREAPI_PRODUCTION_BASE_URL);
        } else {
            enabledLog = true;
            String COREAPI_SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/v2/";
            setBASE_URL(COREAPI_SANDBOX_BASE_URL);
        }
    }

    /**
     * set BASE_URL to SnapAPI_BASE_URL in accordance with the environment type
     */
    public void getSnapApiURL() {
        if (isProduction()) {
            enabledLog = false;
            String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/snap/v1/";
            setBASE_URL(SNAP_PRODUCTION_BASE_URL);
        } else {
            enabledLog = true;
            String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/snap/v1/";
            setBASE_URL(SNAP_SANDBOX_BASE_URL);
        }
    }

    /**
     * @param SERVER_KEY merchant server key
     */
    public void setSERVER_KEY(String SERVER_KEY) {
        this.SERVER_KEY = SERVER_KEY;
    }

    /**
     * @param CLIENT_KEY merchant client key
     */
    public void setCLIENT_KEY(String CLIENT_KEY) {
        this.CLIENT_KEY = CLIENT_KEY;
    }

    /**
     * @param production merchant environment type
     */
    public void setProduction(boolean production) {
        isProduction = production;
    }

    /**
     * @param enabledLog boolean to turn off or on LOG
     */
    public void setEnabledLog(boolean enabledLog) {
        this.enabledLog = enabledLog;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }
}

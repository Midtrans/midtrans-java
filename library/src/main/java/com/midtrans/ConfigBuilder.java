package com.midtrans;

import com.midtrans.proxy.ProxyConfig;

/**
 * {@link Config Config} builder class
 */
public class ConfigBuilder {
    private String SERVER_KEY;
    private String CLIENT_KEY;
    private Boolean isProduction;
    private ProxyConfig proxyConfig;
    private int connectionTimeout = DEFAULT_CONNECT_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;
    private int writeTimeout = DEFAULT_WRITE_TIMEOUT;
    static final int DEFAULT_CONNECT_TIMEOUT = 5;
    static final int DEFAULT_READ_TIMEOUT = 5;
    static final int DEFAULT_WRITE_TIMEOUT = 5;


    /**
     * Default constructor ConfigBuilder
     */
    public ConfigBuilder() {}

    /**
     * Set merchant client key configuration
     *
     * @param SERVER_KEY merchant server key
     * @return {@link ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setSERVER_KEY(String SERVER_KEY) {
        this.SERVER_KEY = SERVER_KEY;
        return this;
    }

    /**
     * Set merchant client key configuration
     *
     * @param CLIENT_KEY merchant client key
     * @return {@link ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setCLIENT_KEY(String CLIENT_KEY) {
        this.CLIENT_KEY = CLIENT_KEY;
        return this;
    }

    /**
     * Set environment type configuration
     * @param isProduction true or false
     * @return              {@link ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setIsProduction(Boolean isProduction) {
        this.isProduction = isProduction;
        return this;
    }

    /**
     * Set http client connect timeout
     * @param connectionTimeout Http client connect timeout
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    /**
     * Set http client socket timeout
     * @param readTimeout Http client socket timeout
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * Set Midtrans max connection pool size
     * @param writeTimeout Max http client connection pool size
     * @return                      {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
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
     * Build Config object from builder
     * @return {@link Config Config}
     */
    public Config build() {
        return new Config(SERVER_KEY, CLIENT_KEY, isProduction, connectionTimeout, readTimeout, writeTimeout, proxyConfig);
    }
}

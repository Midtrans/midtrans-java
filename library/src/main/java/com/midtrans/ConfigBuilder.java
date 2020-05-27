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
    private int maxConnectionPoolSize = DEFAULT_MAX_CONNECTION_POOL_SIZE;
    private int keepAliveDuration = DEFAULT_KEEP_ALIVE_DURATION;

    static final int DEFAULT_CONNECT_TIMEOUT = 10; //SECOND
    static final int DEFAULT_READ_TIMEOUT = 10; //SECOND
    static final int DEFAULT_WRITE_TIMEOUT = 10; //SECOND
    static final int DEFAULT_MAX_CONNECTION_POOL_SIZE = 16;
    static final int DEFAULT_KEEP_ALIVE_DURATION = 300; //SECOND


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
    public ConfigBuilder setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    /**
     * Set http client read timeout
     * @param readTimeout Http client read timeout
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }


    /**
     * Set http client write timeout
     * @param writeTimeout Http client write timeout
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setWriteTimeout(final int writeTimeout) {
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
     * Set Midtrans max connection pool size
     * @param connectionPoolSize Max http client connection pool size
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setMaxConnectionPoolSize(final int connectionPoolSize) {
        this.maxConnectionPoolSize = connectionPoolSize;
        return this;
    }

    /**
     * Set Midtrans keep alive durtions
     * @param keepAliveDuration http client keep alive durations
     * @return {@link com.midtrans.ConfigBuilder ConfigBuilder}
     */
    public ConfigBuilder setKeepAliveDuration(final int keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
        return this;
    }

    /**
     * Build Config object from builder
     * @return {@link Config Config}
     */
    public Config build() {
        return new Config(SERVER_KEY, CLIENT_KEY, isProduction, connectionTimeout, readTimeout, writeTimeout, maxConnectionPoolSize, keepAliveDuration, proxyConfig);
    }
}

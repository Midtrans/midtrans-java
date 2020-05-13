package com.midtrans.proxy;

/**
 * Proxy configuration builder
 */
public final class ProxyConfigBuilder {
    private String host;
    private int port;
    private String username;
    private String password;

    /**
     * Set proxy host config
     * @param host Proxy host
     * @return ProxyConfigBuilder
     */
    public ProxyConfigBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Set proxy port config
     * @param port Proxy port
     * @return ProxyConfigBuilder
     */
    public ProxyConfigBuilder setPort(final int port) {
        this.port = port;
        return this;
    }

    /**
     * Set proxy username config
     * @param username Proxy username config
     * @return ProxyConfigBuilder
     */
    public ProxyConfigBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Set proxy password config
     * @param password Proxy password config
     * @return ProxyConfigBuilder
     */
    public ProxyConfigBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Create proxy config
     * @return {@link com.midtrans.proxy.ProxyConfig Proxy configuration}
     */
    public ProxyConfig build() {
        return new ProxyConfig(host, port, username, password);
    }
}

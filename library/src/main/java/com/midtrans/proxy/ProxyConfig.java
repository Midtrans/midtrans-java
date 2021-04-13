package com.midtrans.proxy;

/**
 * Merchant proxy configuration
 */
public class ProxyConfig {

    private String host;
    private int port;
    private String username;
    private String password;

    /**
     * Proxy config constructor.
     *
     * @param host the proxy server host address.
     * @param port the proxy server listening port.
     * @param username the username used to authenticate against the proxy server.
     * @param password the password used to authenticate against the proxy server.
     */
    public ProxyConfig(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Get merchant proxy host configuration.
     *
     * @return Merchant proxy host config.
     */
    public String getHost() {
        return host;
    }

    /**
     * Get merchant proxy port configuration
     *
     * @return Merchant proxy port config
     */
    public int getPort() {
        return port;
    }

    /**
     * Get proxy username to connect to Veritrans API
     *
     * @return Merchant proxy username config
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get proxy password to connect to Veritrans API
     *
     * @return Merchant proxy password config
     */
    public String getPassword() {
        return password;
    }

    /**
     * Initialize proxy with builder
     *
     * @return ProxyConfigBuilder
     */
    public static ProxyConfigBuilder builder() {
        return new ProxyConfigBuilder();
    }
}

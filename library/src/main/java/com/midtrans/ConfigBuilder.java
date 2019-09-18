package com.midtrans;

/**
 * {@link Config Config} builder class
 */
public final class ConfigBuilder {
    private String SERVER_KEY;
    private String CLIENT_KEY;
    private Boolean isProduction;

    /**
     * Default constructor ConfigBuilder
     */
    public ConfigBuilder() {
    }

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
     * Build Config object from builder
     * @return {@link Config Config}
     */
    public Config build() {
        return new Config(SERVER_KEY, CLIENT_KEY, isProduction);
    }
}

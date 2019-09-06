package com.midtrans.api;

import org.springframework.stereotype.Component;

@Component
public final class ConfigBuilder {
    private String SERVER_KEY;
    private String CLIENT_KEY;
    private Boolean isProduction;

    public ConfigBuilder() {
    }

    public static ConfigBuilder aConfig() {
        return new ConfigBuilder();
    }

    public ConfigBuilder setSERVER_KEY(String SERVER_KEY) {
        this.SERVER_KEY = SERVER_KEY;
        return this;
    }

    public ConfigBuilder setCLIENT_KEY(String CLIENT_KEY) {
        this.CLIENT_KEY = CLIENT_KEY;
        return this;
    }

    public ConfigBuilder setIsProduction(Boolean isProduction) {
        this.isProduction = isProduction;
        return this;
    }

    public Config build() {
        return new Config(SERVER_KEY, CLIENT_KEY, isProduction);
    }
}

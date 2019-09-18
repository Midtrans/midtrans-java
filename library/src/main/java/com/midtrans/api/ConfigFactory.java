package com.midtrans.api;

import com.midtrans.api.service.impl.MidtransCoreApiImpl;
import com.midtrans.api.service.impl.MidtransSnapApiImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Convenience "factory" class to facilitate setup for connection to Midtrans API using SnapAPI or CoreAPI.
 */
public class ConfigFactory {

    private Config config;

    /**
     * ConfigFactory constructor
     *
     * @param config Midtrans {@link com.midtrans.api.Config configuration} (not null)
     */
    public ConfigFactory(Config config) {
        if (config == null) {
            throw new NullPointerException("Config is null");
        }
        this.config = config;
    }

    /**
     * Get CoreAPI gateway
     *
     * @return {@link com.midtrans.api.service.impl.MidtransCoreApiImpl MidtransCoreApi} to connecting midtrans gateway CoreAPI
     */
    public MidtransCoreApiImpl getCoreApi() {
        config.getCoreApiURL();
        return new MidtransCoreApiImpl(config);
    }

    /**
     * Get SnapAPI gateway
     *
     * @return {@link com.midtrans.api.service.impl.MidtransSnapApiImpl MidtransSnapApi} to connecting midtrans gateway SnapAPI
     */
    public MidtransSnapApiImpl getSnapApi() {
        config.getSnapApiURL();
        return new MidtransSnapApiImpl(config);
    }

    /**
     * Get midtrans config.
     *
     * @return {@link com.midtrans.api.Config configuration} when connectng to midtrans API
     */
    public Config getConfig() {
        return config;
    }

}

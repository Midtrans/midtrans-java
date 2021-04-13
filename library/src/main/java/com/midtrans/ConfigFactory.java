package com.midtrans;

import com.midtrans.service.MidtransIrisApi;
import com.midtrans.service.impl.MidtransCoreApiImpl;
import com.midtrans.service.impl.MidtransIrisApiImpl;
import com.midtrans.service.impl.MidtransSnapApiImpl;
import lombok.EqualsAndHashCode;

/**
 * Convenience "factory" class to facilitate setup for connection to Midtrans API using SnapAPI or CoreAPI.
 */
public class ConfigFactory {

    private Config config;

    /**
     * ConfigFactory constructor
     *
     * @param config Midtrans {@link Config configuration} (not null)
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
     * @return {@link MidtransCoreApiImpl MidtransCoreApi} to connecting midtrans gateway CoreAPI
     */
    public MidtransCoreApiImpl getCoreApi() {
        return new MidtransCoreApiImpl(config);
    }

    /**
     * Get SnapAPI gateway
     *
     * @return {@link MidtransSnapApiImpl MidtransSnapApi} to connecting midtrans gateway SnapAPI
     */
    public MidtransSnapApiImpl getSnapApi() {
        return new MidtransSnapApiImpl(config);
    }

    public MidtransIrisApi getIrisApi() {
        return new MidtransIrisApiImpl(config);
    }

    /**
     * Get midtrans config.
     *
     * @return {@link Config configuration} that will be used by the HTTP Client when sending request to Midtrans API
     */
    public Config getConfig() {
        return config;
    }

}

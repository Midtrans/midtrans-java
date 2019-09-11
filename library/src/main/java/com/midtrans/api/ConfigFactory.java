package com.midtrans.api;

import com.midtrans.api.service.impl.MidtransCoreApiImpl;
import com.midtrans.api.service.impl.MidtransSnapApiImpl;
import org.springframework.stereotype.Service;

@Service
public class ConfigFactory {

    private Config config;

    public ConfigFactory(Config config) {
        if (config == null) {
            throw new NullPointerException("Config is null");
        }
        this.config = config;
    }

    public MidtransCoreApiImpl getCoreApi() {
        config.getCoreApiURL();
        return new MidtransCoreApiImpl(config);
    }

    public MidtransSnapApiImpl getSnapApi() {
        config.getSnapApiURL();
        return new MidtransSnapApiImpl(config);
    }

    public Config getConfig() {
        return config;
    }

}

package com.midtrans.sample;

import com.midtrans.api.ConfigBuilder;
import com.midtrans.api.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MidtransConfig {

    @Autowired
    private Credentials credentials;

    // Midtrans library class to get
    private ConfigFactory configFactory;

    @PostConstruct
    public void setup() {
        configFactory = new ConfigFactory(new ConfigBuilder()
                .setCLIENT_KEY(credentials.getClientKey())
                .setSERVER_KEY(credentials.getServerKey())
                .setIsProduction(credentials.isProduction())
                .build());
    }

    public ConfigFactory getConfigFactory() {
        return configFactory;
    }

}

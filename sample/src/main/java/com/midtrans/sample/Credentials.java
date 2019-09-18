package com.midtrans.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Credentials {

    @Value("${com.midtrans.clientkey}")
    private String clientKey;

    @Value("${com.midtrans.serverkey}")
    private String serverKey;

    @Value("${com.midtrans.production}")
    private Boolean isProduction;

    public String getClientKey() {
        return clientKey;
    }

    public String getServerKey() {
        return serverKey;
    }

    public Boolean isProduction() {
        return isProduction;
    }

}

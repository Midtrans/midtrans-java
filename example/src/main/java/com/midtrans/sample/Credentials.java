package com.midtrans.sample;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
public class Credentials {

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

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

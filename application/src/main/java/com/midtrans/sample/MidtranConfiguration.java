package com.midtrans.sample;

import com.midtrans.api.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class MidtranConfiguration extends Config {

    private String clientKey = "SB-Mid-client-N7N5b2n_ZUQOOVba";
    private String serverKey = "SB-Mid-server-Wh2cEDd4H661g4lrcig8sQMf";
    private boolean isProduction = true;

    @Override
    public void setCLIENT_KEY(String CLIENT_KEY) {
        super.setCLIENT_KEY(clientKey);
    }

    @Override
    public void setSERVER_KEY(String SERVER_KEY) {
        super.setSERVER_KEY(serverKey);
    }

    @Override
    public void setProduction(boolean production) {
        super.setProduction(isProduction);
    }

}

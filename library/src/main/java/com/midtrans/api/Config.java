package com.midtrans.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    private final String COREAPI_PRODUCTION_BASE_URL = "https://api.midtrans.com/v2";
    private final String COREAPI_SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/v2";
    private final String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/snap/v1";
    private final String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/snap/v1";

    @Value("${com.midtrans.serverkey}")
    private String SERVER_KEY;

    @Value("${com.midtrans.clientkey}")
    private String CLIENT_KEY;

    @Value("${com.midtrans.production}")
    private boolean isProduction;

    private String BASE_URL;

    public Config() {
    }

    public void setProduction(boolean production) {
        isProduction = production;
    }

    private boolean isProduction() {
        return isProduction;
    }

    private void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public String getSERVER_KEY() {
        return SERVER_KEY;
    }

    public String getCLIENT_KEY() {
        return CLIENT_KEY;
    }

    public String getCoreApi() {
        if (isProduction()) {
            setBASE_URL(COREAPI_PRODUCTION_BASE_URL);
        } else {
            setBASE_URL(COREAPI_SANDBOX_BASE_URL);
        }
        return BASE_URL;
    }

    public String getSnapApi() {
        if (isProduction()) {
            setBASE_URL(SNAP_PRODUCTION_BASE_URL);
        } else {
            setBASE_URL(SNAP_SANDBOX_BASE_URL);
        }
        return BASE_URL;
    }
}

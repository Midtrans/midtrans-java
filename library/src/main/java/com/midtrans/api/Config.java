package com.midtrans.api;

import org.springframework.stereotype.Component;

@Component
public class Config {

    private String SERVER_KEY;

    private String CLIENT_KEY;

    private boolean isProduction;

    private String BASE_URL;

    public Config() {
    }

    public Config(String SERVER_KEY, String CLIENT_KEY, boolean isProduction) {
        this.SERVER_KEY = SERVER_KEY;
        this.CLIENT_KEY = CLIENT_KEY;
        this.isProduction = isProduction;
    }

    public boolean isProduction() {
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

    public void getCoreApiURL() {
        if (isProduction()) {
            String COREAPI_PRODUCTION_BASE_URL = "https://api.midtrans.com/v2/";
            setBASE_URL(COREAPI_PRODUCTION_BASE_URL);
        } else {
            String COREAPI_SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/v2/";
            setBASE_URL(COREAPI_SANDBOX_BASE_URL);
        }
    }

    public void getSnapApiURL() {
        if (isProduction()) {
            String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/snap/v1/";
            setBASE_URL(SNAP_PRODUCTION_BASE_URL);
        } else {
            String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/snap/v1/";
            setBASE_URL(SNAP_SANDBOX_BASE_URL);
        }
    }

    public void setSERVER_KEY(String SERVER_KEY) {
        this.SERVER_KEY = SERVER_KEY;
    }

    public void setCLIENT_KEY(String CLIENT_KEY) {
        this.CLIENT_KEY = CLIENT_KEY;
    }

    public void setProduction(boolean production) {
        isProduction = production;
    }


}

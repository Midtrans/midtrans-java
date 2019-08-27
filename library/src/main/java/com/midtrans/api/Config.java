package com.midtrans.api;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Config {
    private final String COREAPI_PRODUCTION_BASE_URL = "https://api.midtrans.com/v2";
    private final String COREAPI_SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/v2";
    private final String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/snap/v1";
    private final String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/snap/v1";

    private String SERVER_KEY;
    private String CLIENT_KEY;
    private boolean isProduction;

    private String BASE_URL;

//    public Config(String SERVER_KEY, String CLIENT_KEY, boolean isProduction) {
//        this.SERVER_KEY = SERVER_KEY;
//        this.CLIENT_KEY = CLIENT_KEY;
//        this.isProduction = isProduction;
//    }

    public void setSERVER_KEY(String SERVER_KEY) {
        this.SERVER_KEY = SERVER_KEY;
    }

    public void setCLIENT_KEY(String CLIENT_KEY) {
        this.CLIENT_KEY = CLIENT_KEY;
    }

    public void setProduction(boolean production) {
        isProduction = production;
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

    public String getCoreApi() {
        if (isProduction) {
            setBASE_URL(COREAPI_PRODUCTION_BASE_URL);
        } else {
            setBASE_URL(COREAPI_SANDBOX_BASE_URL);
        }
        return BASE_URL;
    }

    public String getSnapApi() {
        if (isProduction) {
            setBASE_URL(SNAP_PRODUCTION_BASE_URL);
            //BASE_URL = SNAP_PRODUCTION_BASE_URL;
        } else {
            setBASE_URL(SNAP_SANDBOX_BASE_URL);
            //BASE_URL = SNAP_SANDBOX_BASE_URL;
        }
        return BASE_URL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return isProduction == config.isProduction &&
                Objects.equals(COREAPI_PRODUCTION_BASE_URL, config.COREAPI_PRODUCTION_BASE_URL) &&
                Objects.equals(COREAPI_SANDBOX_BASE_URL, config.COREAPI_SANDBOX_BASE_URL) &&
                Objects.equals(SNAP_PRODUCTION_BASE_URL, config.SNAP_PRODUCTION_BASE_URL) &&
                Objects.equals(SNAP_SANDBOX_BASE_URL, config.SNAP_SANDBOX_BASE_URL) &&
                Objects.equals(SERVER_KEY, config.SERVER_KEY) &&
                Objects.equals(CLIENT_KEY, config.CLIENT_KEY) &&
                Objects.equals(BASE_URL, config.BASE_URL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(COREAPI_PRODUCTION_BASE_URL, COREAPI_SANDBOX_BASE_URL, SNAP_PRODUCTION_BASE_URL, SNAP_SANDBOX_BASE_URL, SERVER_KEY, CLIENT_KEY, isProduction, BASE_URL);
    }
}

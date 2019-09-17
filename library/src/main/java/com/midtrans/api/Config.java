package com.midtrans.api;


/**
 * Midtrans configuration
 */
public class Config {

    private String SERVER_KEY;

    private String CLIENT_KEY;

    private boolean isProduction;

    private String BASE_URL;

    /**
     * Midtrans configuration constructor
     */
    public Config() {
    }

    /**
     * Midtrans configuration constructor
     *
     * @param SERVER_KEY   Merchant server-key
     * @param CLIENT_KEY   Merchant client-key
     * @param isProduction Merchant Environment Sandbox or Production
     */
    public Config(String SERVER_KEY, String CLIENT_KEY, boolean isProduction) {
        this.SERVER_KEY = SERVER_KEY;
        this.CLIENT_KEY = CLIENT_KEY;
        this.isProduction = isProduction;
    }

    /**
     * Get environment type
     *
     * @return boolean
     */
    public boolean isProduction() {
        return isProduction;
    }

    private void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    /**
     * Get BASE_URL
     * @return BASE_URL
     */
    public String getBASE_URL() {
        return BASE_URL;
    }

    /**
     * Get merchant server key
     * @return Merchant server key
     */
    public String getSERVER_KEY() {
        return SERVER_KEY;
    }

    /**
     * Get merchant client key
     * @return Merchant client key
     */
    public String getCLIENT_KEY() {
        return CLIENT_KEY;
    }

    /**
     * set BASE_URL to CoreAPI_BASE_URL in accordance with the environment type
     */
    public void getCoreApiURL() {
        if (isProduction()) {
            String COREAPI_PRODUCTION_BASE_URL = "https://api.midtrans.com/v2/";
            setBASE_URL(COREAPI_PRODUCTION_BASE_URL);
        } else {
            String COREAPI_SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/v2/";
            setBASE_URL(COREAPI_SANDBOX_BASE_URL);
        }
    }

    /**
     * set BASE_URL to SnapAPI_BASE_URL in accordance with the environment type
     */
    public void getSnapApiURL() {
        if (isProduction()) {
            String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/snap/v1/";
            setBASE_URL(SNAP_PRODUCTION_BASE_URL);
        } else {
            String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/snap/v1/";
            setBASE_URL(SNAP_SANDBOX_BASE_URL);
        }
    }

    /**
     * @param SERVER_KEY merchant server key
     */
    public void setSERVER_KEY(String SERVER_KEY) {
        this.SERVER_KEY = SERVER_KEY;
    }

    /**
     * @param CLIENT_KEY merchant client key
     */
    public void setCLIENT_KEY(String CLIENT_KEY) {
        this.CLIENT_KEY = CLIENT_KEY;
    }

    /**
     * @param production merchant environment type
     */
    public void setProduction(boolean production) {
        isProduction = production;
    }


}

package com.midtrans.snapbi;


/**
 * Midtrans Configuration for SnapBi
 */
public class SnapBiConfig {
    private static boolean isProduction = false;
    private static String snapBiClientId;
    private static String snapBiPrivateKey;
    private static String snapBiClientSecret;
    private static String snapBiPartnerId;
    private static String snapBiChannelId;
    private static boolean enableLogging = false;

    public static final String SNAP_BI_SANDBOX_BASE_URL = "https://merchants.sbx.midtrans.com";
    public static final String SNAP_BI_PRODUCTION_BASE_URL = "https://merchants.midtrans.com";

    public static boolean isProduction() {
        return isProduction;
    }

    public static void setProduction(boolean isProduction) {
        SnapBiConfig.isProduction = isProduction;
    }

    public static String getSnapBiClientId() {
        return snapBiClientId;
    }
    public static void setSnapBiClientId(String snapBiClientId) {
        SnapBiConfig.snapBiClientId = snapBiClientId;
    }
    public static String getSnapBiPrivateKey() {
        return snapBiPrivateKey;
    }

    public static void setSnapBiPrivateKey(String snapBiPrivateKey) {
        SnapBiConfig.snapBiPrivateKey = snapBiPrivateKey;
    }
    public static String getSnapBiClientSecret() {
        return snapBiClientSecret;
    }

    public static void setSnapBiClientSecret(String snapBiClientSecret) {
        SnapBiConfig.snapBiClientSecret = snapBiClientSecret;
    }
    public static String getSnapBiPartnerId() {
        return snapBiPartnerId;
    }

    public static void setSnapBiPartnerId(String snapBiPartnerId) {
        SnapBiConfig.snapBiPartnerId = snapBiPartnerId;
    }
    public static String getSnapBiChannelId() {
        return snapBiChannelId;
    }

    public static void setSnapBiChannelId(String snapBiChannelId) {
        SnapBiConfig.snapBiChannelId = snapBiChannelId;
    }
    public static boolean isEnableLogging() {
        return enableLogging;
    }

    public static void setEnableLogging(boolean enableLogging) {
        SnapBiConfig.enableLogging = enableLogging;
    }
    public static String getSnapBiTransactionBaseUrl() {
        return isProduction ? SNAP_BI_PRODUCTION_BASE_URL : SNAP_BI_SANDBOX_BASE_URL;
    }


}


package com.midtrans.java.mockupdata;

public class Constant {
    public static final String mainServerKey = "SB-Mid-server-LDMQH0mOAUofApUWMmXxUIFj";
    public static final String mainClientKey = "SB-Mid-client-_CIibU2GiWPARC2l";
    public static final String merchantId1 = "G331392016";
    public static final String secondServerKey = "SB-Mid-server-9Nm5c-HJE65AjLNtTX-bRjqm";
    public static final String secondClientKey = "SB-Mid-client-GtbzK39rvs5El-bC";
    public static final String merchantId2 = "G902985977";

    public static boolean isProduction = false;

    public static final String COREAPI_PRODUCTION_BASE_URL = "https://api.midtrans.com/";
    public static final String COREAPI_SANDBOX_BASE_URL = "https://api.sandbox.midtrans.com/";
    public static final String SNAP_PRODUCTION_BASE_URL = "https://app.midtrans.com/snap/v1/transactions";
    public static final String SNAP_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/snap/v1/transactions";

    public static final String creatorKey = "IRIS-330198f0-e49d-493f-baae-585cfded355d";
    public static final String approverKey = "IRIS-1595c12b-6814-4e5a-bbbb-9bc18193f47b";

    public static final String IRIS_SANDBOX_BASE_URL = "https://app.sandbox.midtrans.com/iris/api/v1/";
    public static final String IRIS_PRODUCTION_BASE_URL = "https://app.midtrans.com/iris/api/v1/";

    public static final String cardNumberAccept = "4811111111111114";
    public static final String bniCardNumber = "4105 0586 8948 1467";
    public static final String cardNumberFDS = "5510 1111 1111 1115";

    public static final int DEFAULT_CONNECT_TIMEOUT = 10; //SECOND
    public static final int DEFAULT_READ_TIMEOUT = 10; //SECOND
    public static final int DEFAULT_WRITE_TIMEOUT = 10; //SECOND
    public static final int DEFAULT_MAX_CONNECTION_POOL_SIZE = 16;
    public static final int DEFAULT_KEEP_ALIVE_DURATION = 300; //SECOND

    // Dummy for test
    public static final int CONNECT_TIMEOUT = 100;
    public static final int READ_TIMEOUT = 200;
    public static final int WRITE_TIMEOUT = 300;
    public static final int CONNECTION_POOL_SIZE = 20;
    public static final int KEEP_ALIVE_DURATION = 400;

    public static final String APPEND_NOTIFICATION = "https://example.com/";
    public static final String OVERRIDE_NOTIFICATION = "https://example.com/";

    public static final String PROXY_HOTS = "http://example.com";
    public static final int PROXY_PORT = 8080;
    public static final String PROXY_USERNAME = "user";
    public static final String PROXY_PASSWORD = "password";

}
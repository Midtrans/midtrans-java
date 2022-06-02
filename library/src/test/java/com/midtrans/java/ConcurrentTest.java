package com.midtrans.java;

import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.java.mockupdata.DataMockup;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.sql.Timestamp;

import static com.midtrans.java.mockupdata.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is concurrent test to make sure each request is a thread safe, perform multiple request the same action
 * at the same time.
 */
@Execution(ExecutionMode.CONCURRENT)
public class ConcurrentTest {

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private final static String WEBHOOK_ENDPOINT = "https://example.com";

    @BeforeAll
    public static void setUp() {

        /*
          Set credential-key globally that will be used by {@link #firstRequest() firstRequest} method for initiate
          request.
         */
        Midtrans.serverKey = mainServerKey;
        Midtrans.clientKey = mainClientKey;
        Midtrans.isProduction = false;
    }

    @Test
    public void firstRequest() throws Exception{
        System.out.println("ParallelUnitTest first() start => " + Thread.currentThread().getName());

        //1. Create order-id with prefix 1
        String orderId = "CONCURRENT-TEST-1-" +timestamp.getTime();

        //2. Create object config as configOptions1 will be used for the first request. We don't set credential-key
        //   in the config object, will be used credential-key from global config (Midtrans.serverKey)
        Config configOptions1 = Config.builder()
                .setPaymentOverrideNotification(WEBHOOK_ENDPOINT + "?id=" + orderId)
                .build();

        //3. set thread sleep to interfere the first request
        Thread.sleep(500);

        //3. Charge transaction to Midtrans CoreAPI using configOptions1
        JSONObject result = CoreApi.chargeTransaction(DataMockup.simpleDataMock(orderId, "gopay"), configOptions1);

        //4. Compare the response to check the value of merchant_id it's expected or not. the expected value merchant_id
        //   should with merchantId1
        assertEquals(merchantId1, result.getString("merchant_id"));
        assertEquals(orderId, result.getString("order_id"));

        assertEquals("201", result.getString("status_code"));
        assertEquals("gopay", result.getString("payment_type"));

        System.out.println("ParallelUnitTest first() end => " + Thread.currentThread().getName());
    }

    @Test
    public void secondRequest() throws Exception {
        System.out.println("ParallelUnitTest second() start => " + Thread.currentThread().getName());

        //1. Create order-id with prefix 2
        String orderId = "CONCURRENT-TEST-2-" +timestamp.getTime();

        //2. Create object config as configOptions2 will be used for the first request. We set the credential-key
        //   in this config object to simulate the request using multiple Midtrans account
        Config configOptions2 = Config.builder()
                .setServerKey(secondServerKey)
                .setPaymentOverrideNotification(WEBHOOK_ENDPOINT + "?id=" + orderId)
                .build();

        //3. Charge transaction to Midtrans CoreAPI using configOptions2
        JSONObject result = CoreApi.chargeTransaction(DataMockup.simpleDataMock(orderId, "gopay"), configOptions2);

        //4. Compare the response to check the value of merchant_id it's expected or not. the expected value merchant_id
        //   should with merchantId2
        assertEquals(merchantId2, result.getString("merchant_id"));
        assertEquals(orderId, result.getString("order_id"));

        assertEquals("201", result.getString("status_code"));
        assertEquals("gopay", result.getString("payment_type"));

        System.out.println("ParallelUnitTest second() end => " + Thread.currentThread().getName());
    }
}
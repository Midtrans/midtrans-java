package com.midtrans.sample.controller;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.v2.gateway.SnapApi;
import com.midtrans.v2.gateway.TransactionApi;
import com.midtrans.v2.gateway.http.ApiConfig;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.midtrans.sample.data.Constant.sandboxServerKey;

@RestController
public class MobileSdkController {

    /**
     * Midtrans java v2 sample use `com.midtrans.v2` : Using static method and Midtrans ApiConfig {@link ApiConfig}.
     * This is a new static method, the config will use for request Snap token on Midtrans API static (SnapApi class).
     * {@link SnapApi}
     * Sample use on mobile sdk Controller @line 52
     *
     * also you can put others config, like enableLog, timeConnect, proxy, idempotent-key, etc.
     */
    private ApiConfig midtransApiConfig = ApiConfig.builder()
            .setServerKey(sandboxServerKey)
            .isProduction(false)
            .setPaymentOverrideNotification("https://midtrans-java.herokuapp.com/mobile-notification")
            .build();


    // API `/charge` for mobile SDK to get Snap Token
    @PostMapping(value = "/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createTokeSnap(@RequestBody Map<String, Object> body) {
        try {
            JSONObject result = SnapApi.createTransaction(midtransApiConfig, body);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/mobile-notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mobileHandleNotification(@RequestBody Map<String, Object> body) {
        String notifResponse = null;
        if (!(body.isEmpty())) {
            //Get Midtrans transaction ID from notification body
            String orderId = (String) body.get("transaction_id");

            // Get status transaction to api with transaction id
            JSONObject transactionResult = null;
            try {
                transactionResult = TransactionApi.checkTransaction(midtransApiConfig, orderId);
            } catch (MidtransError midtransError) {
                midtransError.printStackTrace();
            }

            String transactionStatus = (String) transactionResult.get("transaction_status");
            String fraudStatus = (String) transactionResult.get("fraud_status");

            notifResponse = "Transaction notification received. Order ID: " + orderId + ". Transaction status: " + transactionStatus + ". Fraud status: " + fraudStatus;
            System.out.println(notifResponse);

            if (transactionStatus.equals("capture")) {
                if (fraudStatus.equals("challenge")) {
                    // TODO set transaction status on your database to 'challenge' e.g: 'Payment status challenged. Please take action on your Merchant Administration Portal
                } else if (fraudStatus.equals("accept")) {
                    // TODO set transaction status on your database to 'success'
                }
            } else if (transactionStatus.equals("cancel") || transactionStatus.equals("deny") || transactionStatus.equals("expire")) {
                // TODO set transaction status on your database to 'failure'
            } else if (transactionStatus.equals("pending")) {
                // TODO set transaction status on your database to 'pending' / waiting payment
            }
        }
        return new ResponseEntity<>(notifResponse, HttpStatus.OK);
    }
}

package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.TransactionApi;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.midtrans.sample.data.Constant.sandboxServerKey;

/**
 * This is sample springboot controller for implement Midtrans mobile SDK.
 * You need to setup Frontend (Midtrans SDK Config) into your mobile app that using Midtrans mobile SDK.
 *
 * more details: https://mobile-docs.midtrans.com/#getting-started
 */
@RestController
public class MobileSdkBackendController {

    /**
     * Midtrans java sample use, Using static method and Midtrans Config {@link Config}.
     * This is a new static method, the config will use for request Snap token on Midtrans API static (SnapApi class).
     * {@link SnapApi}
     * Sample use on mobile sdk backend Controller @line 47
     *
     * also you can put others config, like enableLog, timeConnect, proxy, idempotent-key, etc.
     */
    private Config configOptions = Config.builder()
            .setServerKey(sandboxServerKey)
            .setIsProduction(false)
            .setPaymentOverrideNotification("https://midtrans-java.herokuapp.com/payment-status-notification-handler")
            .build();


    // API `/charge` for mobile SDK to get Snap Token
    @PostMapping(value = "/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createTokeSnap(@RequestBody Map<String, Object> body) {
        try {
            JSONObject result = SnapApi.createTransaction(body, configOptions);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/payment-status-notification-handler", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mobileHandleNotification(@RequestBody Map<String, Object> body) {
        String notifResponse = null;
        if (!(body.isEmpty())) {
            //Get Midtrans transaction ID from notification body
            String orderId = (String) body.get("transaction_id");

            // Get status transaction to api with transaction id
            JSONObject transactionResult = null;
            try {
                transactionResult = TransactionApi.checkTransaction(orderId, configOptions);
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

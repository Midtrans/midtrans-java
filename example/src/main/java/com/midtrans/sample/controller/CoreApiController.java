package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.midtrans.sample.data.DataMockup;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.midtrans.sample.data.Constant.*;

@RestController
public class CoreApiController {

    Config configOptions = Config.builder()
            .enableLog(true)
            .setIsProduction(isProduction)
            .setServerKey(sandboxServerKey)
            .setClientKey(sandboxClientKey)
            .build();


    /**
     * Midtrans java sample use `com.midtrans`: Using Midtrans Config class {@link Config}.
     * The config will use method from Object MidtransCoreAPI class
     * {@link MidtransCoreApi}
     * Sample use on Charge Controller @line 59
     */
    private MidtransCoreApi coreApi = new ConfigFactory(configOptions).getCoreApi();


    @Autowired
    private DataMockup dataMockup;

    // Core API Controller for fetch credit card transaction
    @PostMapping(value = "/cards/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> charge(@RequestBody Map<String, String> cc) throws MidtransError {
        dataMockup.setPaymentType("credit_card");
        Map<String, String> creditCard = new HashMap<>(cc);
        creditCard.put("authentication", "true");
        dataMockup.creditCard(creditCard);
        Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());

        coreApi.apiConfig().paymentAppendNotification("http://midtrans-java.herokuapp.com/notif/append1,http://midtrans-java.herokuapp.com/notif/append2");
        JSONObject object = coreApi.chargeTransaction(body);

        String result = object.toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Core API Controller for fetch Gopay transaction
    @PostMapping(value = "/gopay/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> goPay() throws MidtransError {
        dataMockup.setPaymentType("gopay");

        Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());

        coreApi.apiConfig().paymentOverrideNotification("http://midtrans-java.herokuapp.com/notif/override1,http://midtrans-java.herokuapp.com/notif/override2");
        JSONObject object = coreApi.chargeTransaction(body);
        String result = object.toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/transactions/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkTransaction(@RequestBody Map<String, String> transaction) throws MidtransError {
        JSONObject result = coreApi.checkTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/transactions/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> approveTransaction(@RequestBody Map<String, String> transaction) throws MidtransError {
        JSONObject result = coreApi.approveTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/transactions/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelTransaction(@RequestBody Map<String, String> transaction) throws MidtransError {
        JSONObject result = coreApi.cancelTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/transactions/expire", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> expireTransaction(@RequestBody Map<String, String> transaction) throws MidtransError {
        JSONObject result = coreApi.expireTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    // Midtrans Handling Notification
    @PostMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> response) throws MidtransError {
        String notifResponse = null;
        if (!(response.isEmpty())) {
            //Get Order ID from notification body
            String orderId = (String) response.get("order_id");

            // Get status transaction to api with order id
            JSONObject transactionResult = coreApi.checkTransaction(orderId);

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


    /*
     * Sample for append / override notifications
     */
    @PostMapping(value = "/notif/append1", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> appendNotif1(@RequestBody Map<String, Object> response) throws MidtransError {
        String append1 = "################# TEST - Received Append Notification 1 ###################";
        System.out.println(append1);
        return new ResponseEntity<>(append1, HttpStatus.OK);
    }

    @PostMapping(value = "/notif/append2", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> appendNotif2(@RequestBody Map<String, Object> response) throws MidtransError {
        String append2 = "################# TEST - Received Append Notification 2 ###################";
        System.out.println(append2);
        return new ResponseEntity<>(append2, HttpStatus.OK);
    }

    @PostMapping(value = "/notif/override1", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> overrideNotif1(@RequestBody Map<String, Object> response) throws MidtransError {
        String append1 = "################# TEST - Received Override Notification 1 ###################";
        System.out.println(append1);
        return new ResponseEntity<>(append1, HttpStatus.OK);
    }

    @PostMapping(value = "/notif/override2", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> overrideNotif2(@RequestBody Map<String, Object> response) throws MidtransError {
        String append2 = "################# TEST - Received Override Notification 2 ###################";
        System.out.println(append2);
        return new ResponseEntity<>(append2, HttpStatus.OK);
    }
}
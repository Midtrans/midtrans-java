package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.midtrans.sample.data.DataMockup;
import com.midtrans.service.MidtransSnapApi;
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

@RestController
public class CoreApiController {

    // ProxyConfig object
    //private ProxyConfig proxyConfig = new ProxyConfigBuilder().setHost("36.92.108.150").setPort(3128).setUsername("").setPassword("").build();

    // Midtrans CoreApi Class library with proxy config
    //private MidtransCoreApi coreApi = new ConfigFactory(new Config("SB-Mid-server-zPtluafD-kgcvOMVtsNYhXVD", "SB-Mid-client-I4ekVNAD4Cr4KJ1V", false, 10, 10,10,proxyConfig)).getCoreApi();

    // Midtrans CoreApi Class library without proxy config
    private MidtransCoreApi coreApi = new ConfigFactory(new Config("SB-Mid-server-TOq1a2AVuiyhhOjvfs3U_KeO", "SB-Mid-client-nKsqvar5cn60u2Lv", false)).getCoreApi();

    private MidtransSnapApi snapApi = new ConfigFactory(new Config("SB-Mid-server-TOq1a2AVuiyhhOjvfs3U_KeO", "SB-Mid-client-nKsqvar5cn60u2Lv", false)).getSnapApi();



    @Autowired
    private DataMockup dataMockup;

    // API `/charge` for mobile SDK to get Snap Token
    @PostMapping(value = "/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createTokeSnap(@RequestBody Map<String, Object> body) throws MidtransError {
        //Disable log when development
        // snapApi.apiConfig().setEnabledLog(false);
        JSONObject token = snapApi.createTransaction(body);
        return token.toString();
    }

    // Core API Controller for fetch credit card transaction
    @PostMapping(value = "/charge-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> charge(@RequestBody Map<String, String> cc) throws MidtransError {
        dataMockup.setPaymentType("credit_card");
        Map<String, String> creditCard = new HashMap<>(cc);
        dataMockup.creditCard(creditCard);
        Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());

        JSONObject object = coreApi.chargeTransaction(body);

        String result = object.toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Core API Controller for fetch Gopay transaction
    @PostMapping(value = "/gopay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> goPay() throws MidtransError {
        dataMockup.setPaymentType("gopay");

        Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());

        JSONObject object = coreApi.chargeTransaction(body);
        String result = object.toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/check-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkTransaction(@RequestBody Map<String, String> transaction) throws MidtransError {
        JSONObject result = coreApi.checkTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/approve-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> approveTransaction(@RequestBody Map<String, String> transaction) throws MidtransError {
        JSONObject result = coreApi.approveTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/cancel-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelTransaction(@RequestBody Map<String, String> transaction) throws MidtransError {
        JSONObject result = coreApi.cancelTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/expire-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
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
}



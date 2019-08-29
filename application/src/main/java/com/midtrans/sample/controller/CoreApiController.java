package com.midtrans.sample.controller;

import com.midtrans.api.service.MidtransCoreApi;
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


@RestController
public class CoreApiController {
    @Autowired
    private MidtransCoreApi coreApi;

    @Autowired
    private DataMockup dataMockup;

    @PostMapping(value = "/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> carge(@RequestBody Map<String, String> cc) {
        dataMockup.setPaymentType("credit_card");
        Map<String, String> creditCard = new HashMap<>(cc);
        dataMockup.creditCard(creditCard);

        Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());
        JSONObject object = coreApi.chargeTransaction(body);
        String result = object.toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/gopay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> goPay() {
        dataMockup.setPaymentType("gopay");

        Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());
        JSONObject object = coreApi.chargeTransaction(body);
        String result = object.toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/check-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkTransaction(@RequestBody Map<String, String> transaction) {
        JSONObject result = coreApi.checkTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/approve-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> approveTransaction(@RequestBody Map<String, String> transaction) {
        JSONObject result = coreApi.approveTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/cancel-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelTransaction(@RequestBody Map<String, String> transaction) {
        JSONObject result = coreApi.cancelTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/expire-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> expireTransaction(@RequestBody Map<String, String> transaction) {
        JSONObject result = coreApi.expireTransaction(transaction.get("transaction_id"));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}



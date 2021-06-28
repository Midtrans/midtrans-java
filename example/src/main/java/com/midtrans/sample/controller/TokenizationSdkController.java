package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.midtrans.sample.data.Constant.sandboxClientKey;
import static com.midtrans.sample.data.Constant.sandboxServerKey;

@RestController
@RequestMapping("/tokenization")
public class TokenizationSdkController {

    /**
     * Midtrans Config library java sample,
     * also you can put others config, like enableLog, timeConnect, proxy, etc.
     */
    Config configOptions = Config.builder()
            .setServerKey(sandboxServerKey)
            .setClientKey(sandboxClientKey)
            .setIsProduction(false)
            .setPaymentOverrideNotification("https://midtrans-java.herokuapp.com/payment-status-notification-handler")
            .build();

    /**
     * Midtrans java sample use `com.midtrans`: Using Midtrans Config class {@link Config}.
     * The config will use method from Object MidtransCoreAPI class library
     * {@link MidtransCoreApi}
     * Sample use on Charge Controller @line 59
     */
    MidtransCoreApi coreApi = new ConfigFactory(configOptions).getCoreApi();


    // API `/v2/pay/account` for Tokenization SDK to linking payment
    @PostMapping(value = "/v2/pay/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> linkPaymentAccount(@RequestBody Map<String, Object> body) {
        try {
            JSONObject result = coreApi.linkPaymentAccount(body);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
            return new ResponseEntity<>(midtransError.getResponseBody(), HttpStatus.valueOf(midtransError.getStatusCode()));
        }
    }

    // API `/v2/pay/account/{accountId}` for Tokenization SDK to get account details
    @GetMapping(value = "/v2/pay/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPaymentAccount(@PathVariable String accountId) {
        try {
            JSONObject result = coreApi.getPaymentAccount(accountId);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
            return new ResponseEntity<>(midtransError.getResponseBody(), HttpStatus.valueOf(midtransError.getStatusCode()));
        }
    }

    // API `/v2/charge` for Tokenization SDK request charge
    @PostMapping(value = "/v2/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> charge(@RequestBody Map<String, Object> body) {
        //uncomment this if you want set idempotency-key
        //coreApi.apiConfig().setPaymentIdempotencyKey("123321123312");
        try {
            JSONObject result = coreApi.chargeTransaction(body);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
            return new ResponseEntity<>(midtransError.getResponseBody(), HttpStatus.valueOf(midtransError.getStatusCode()));
        }
    }

    // API `/v2/pay/account/{accountId}` for Tokenization SDK to get account details
    @GetMapping(value = "/v2/pay/account/{accountId}/unbind", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> unlinkPaymentAccount(@PathVariable String accountId) {
        try {
            JSONObject result = coreApi.unlinkPaymentAccount(accountId);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (MidtransError midtransError) {
            midtransError.printStackTrace();
            return new ResponseEntity<>(midtransError.getResponseBody(), HttpStatus.valueOf(midtransError.getStatusCode()));
        }
    }
}

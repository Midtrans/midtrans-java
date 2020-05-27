package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MobileSdkController {

    private MidtransSnapApi snapApi = new ConfigFactory(
            new Config("SB-Mid-server-TOq1a2AVuiyhhOjvfs3U_KeO",
                    "SB-Mid-client-nKsqvar5cn60u2Lv",
                    false))
            .getSnapApi();

    // API `/charge` for mobile SDK to get Snap Token
    @PostMapping(value = "/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createTokeSnap(@RequestBody Map<String, Object> body) throws MidtransError {
        JSONObject result = snapApi.createTransaction(body);
        return result.toString();
    }
}

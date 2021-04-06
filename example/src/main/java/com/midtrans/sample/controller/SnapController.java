package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import com.midtrans.sample.data.DataMockup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.midtrans.sample.data.Constant.sandboxClientKey;
import static com.midtrans.sample.data.Constant.sandboxServerKey;

@Controller
public class SnapController {

    //Data transaction Mockup
    @Autowired
    private DataMockup dataMockup;

    @RequestMapping(value = "/snap", method = RequestMethod.GET)
    public String snap(Model model) {
        Map<String, Object> objectMap = dataMockup.initDataMock();
        model.addAttribute("data", objectMap);
        return "snap/snap";
    }

    @RequestMapping(value = "/snap/check-out", method = RequestMethod.POST)
    public String checkout(@RequestParam(value = "enablePay", required = false) List<String> listPay,
                           @RequestParam(value = "snapType") String snapType,
                           Model model) throws MidtransError {

        Midtrans.clientKey = sandboxClientKey;
        Midtrans.serverKey = sandboxServerKey;
        // Get ClientKey from Midtrans Configuration class
        String clientKey = Midtrans.getClientKey();

        // New Map Object for JSON raw request body
        Map<String, Object> requestBody = new HashMap<>();

        // Add enablePayment from @RequestParam to dataMockup
        List<String> paymentList = new ArrayList<>();
        if (listPay != null) {
            paymentList.addAll(listPay);
        }
        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("secure", "true");
        dataMockup = new DataMockup();
        dataMockup.creditCard(creditCard);
        dataMockup.enablePayments(paymentList);

        // PutAll data mockUp to requestBody
        requestBody.putAll(dataMockup.initDataMock());

        // send data to frontEnd snapPopUp
        if (snapType.equals("snap")) {
            model.addAttribute("result", requestBody);
            model.addAttribute("clientKey", clientKey);
            // token object getData token to API with createTransactionToken() method return String token
            model.addAttribute("transactionToken", SnapApi.createTransactionToken(requestBody));
            return "snap/check-out";

            // send data to frontEnd redirect-url
        } else {
            model.addAttribute("result", requestBody);
            // redirectURL get url redirect to API with createTransactionRedirectUrl() method, with return String url redirect
            model.addAttribute("redirectURL", SnapApi.createTransactionRedirectUrl(requestBody));
            return "snap/check-out";
        }
    }
}

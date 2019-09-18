package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransSnapApi;
import com.midtrans.sample.Credentials;
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

@Controller
public class SnapController {

    @Autowired
    private Credentials config;

    //Data transaction Mockup
    @Autowired
    private DataMockup dataMockup;

    private MidtransSnapApi snapApi = new ConfigFactory(new Config("SB-Mid-server-Wh2cEDd4H661g4lrcig8sQMf", "SB-Mid-client-N7N5b2n_ZUQOOVba", false)).getSnapApi();

    @RequestMapping(value = "/snap", method = RequestMethod.GET)
    public String snap(Model model) {
        Map<String, Object> objectMap = dataMockup.initDataMock();
        model.addAttribute("data", objectMap);
        return "snap/snap";
    }

    @RequestMapping(value = "snap/check-out", method = RequestMethod.POST)
    public String checkout(@RequestParam(value = "enablePay", required = false) List<String> listPay,
                           @RequestParam(value = "snapType") String snapType,
                           Model model) {
        // Get ClientKey from Midtrans Configuration class
        String clientKey = config.getClientKey();

        // New Map Object for JSON raw request body
        Map<String, Object> requestBody = new HashMap<>();

        // Add enablePayment from @RequestParam to dataMockup
        List<String> paymentList = new ArrayList<>();
        if (listPay != null) {
            paymentList.addAll(listPay);
        }
        dataMockup = new DataMockup();
        dataMockup.enablePayments(paymentList);

        // PutAll data mockUp to requestBody
        requestBody.putAll(dataMockup.initDataMock());

        /*
        If you want snap method return json raw object, you can use method
        createTransaction() on MidtransSnapApi class.
         */

        // send data to frontEnd snapPopUp
        if (snapType.equals("snap")) {
            model.addAttribute("result", requestBody);
            model.addAttribute("clientKey", clientKey);
            // token object getData token to API with createTransactionToken() method return String token
            model.addAttribute("transactionToken", snapApi.createTransactionToken(requestBody));
            return "snap/check-out";

            // send data to frontEnd redirect-url
        } else {
            model.addAttribute("result", requestBody);
            // redirectURL get url redirect to API with createTransactionRedirectUrl() method, with return String url redirect
            model.addAttribute("redirectURL", snapApi.createTransactionRedirectUrl(requestBody));
            return "snap/check-out";
        }
    }


}

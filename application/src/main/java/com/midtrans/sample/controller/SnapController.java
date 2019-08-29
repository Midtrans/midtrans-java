package com.midtrans.sample.controller;

import com.midtrans.api.Config;
import com.midtrans.api.service.MidtransSnapApi;
import com.midtrans.sample.data.DataMockup;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

@Controller
public class SnapController {

    /*
     * This Controller Class For Midtrans Snap Implementation
     */

    private Map<String, Object> customBody;


    //Midtrans HTTP SNAP Library Class
    @Autowired
    private MidtransSnapApi midtransSnapApi;

    /*
    Midtrans Configuration class for get ClientKey or ServerKey
    and setup Environment production/sandbox, you can set Key
    on application.properties file, or with setter method on Config class
     */
    @Autowired
    private Config config;

    //Data transaction Mockup
    @Autowired
    private DataMockup dataMockup;

    @RequestMapping(value = "/snap", method = RequestMethod.GET)
    public String snap(Model model) {
        Map<String, Object> objectMap = dataMockup.initDataMock();
        model.addAttribute("data", objectMap);
        return "snap/snap";
    }

    @RequestMapping(value = "/check-out", method = RequestMethod.POST)
    public String checkout(@RequestParam(value = "enablePay", required = false) List<String> listPay,
                           @RequestParam(value = "snapType") String snapType,
                           Model model) {
        String clientKey = config.getCLIENT_KEY();
        customBody = new HashMap<>();
        List<String> paymentList = new ArrayList<>();
        if (listPay != null) {
            paymentList.addAll(listPay);
        }
        dataMockup = new DataMockup();
        dataMockup.enablePayments(paymentList);
        customBody.putAll(dataMockup.initDataMock());
        Map<String, Object> dataCheckout = dataMockup.initDataMock();
        if (snapType.equals("snap")) {
            model.addAttribute("result", customBody);
            model.addAttribute("clientKey", clientKey);
            model.addAttribute("token", midtransSnapApi.generateSnapToken(dataCheckout));
            return "snap/check-out";
        } else {
            String redirectURL = midtransSnapApi.snapRedirect(dataCheckout);
            model.addAttribute("result", customBody);
            model.addAttribute("redirectURL", redirectURL);
            return "snap/check-out";
        }
    }

}

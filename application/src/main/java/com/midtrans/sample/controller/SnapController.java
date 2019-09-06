package com.midtrans.sample.controller;

import com.midtrans.api.service.MidtransSnapApi;
import com.midtrans.sample.Credentials;
import com.midtrans.sample.MidtransConfig;
import com.midtrans.sample.data.DataMockup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
public class SnapController {

    /*
     * This Controller Class For Midtrans Snap Implementation
     */

    private Map<String, Object> requestBody;

    @Autowired
    private MidtransConfig midtransConfig;


    //Midtrans HTTP SNAP API Class
    private MidtransSnapApi midtransSnapApi;

    @PostConstruct
    public void setup() {
        midtransSnapApi = midtransConfig.getConfigFactory().getSnapApi();
    }

    /*
    Midtrans Configuration class for get ClientKey or ServerKey
    and setup Environment production/sandbox, you can set Key
    on application.properties file, or with setter method on Config class
     */
    @Autowired
    private Credentials config;

    //Data transaction Mockup
    @Autowired
    private DataMockup dataMockup;


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
        requestBody = new HashMap<>();

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
        createTransaction() on MidtransSnapApi interface class.
         */

        // send data to frontEnd snapPopUp
        if (snapType.equals("snap")) {
            model.addAttribute("result", requestBody);
            model.addAttribute("clientKey", clientKey);
            // token object getData token to API with createTransactionToken() method return String token
            model.addAttribute("token", midtransSnapApi.createTransactionToken(requestBody));
            return "snap/check-out";
            // send data to frontEnd redirect-url
        } else {
            model.addAttribute("result", requestBody);
            // redirectURL get url redirect to API with createTransactionRedirectUrl() method, with return String url redirect
            model.addAttribute("redirectURL", midtransSnapApi.createTransactionRedirectUrl(requestBody));
            return "snap/check-out";
        }
    }

}

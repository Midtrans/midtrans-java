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

import java.util.*;

@Controller
public class SnapController {

    /*
     * This Controller Class For Snap Implementation
     */

    private Map<String, Object> customBody;

    @Autowired
    private MidtransSnapApi midtransSnapApi;

    @Autowired
    private Config config;

    @Autowired
    private DataMockup dataMockup;

    @RequestMapping(value = "/snap", method = RequestMethod.GET)
    public String snap(Model model) {
        Map<String, Object> objectMap = dataMockup.initDataMock();
        model.addAttribute("data", objectMap);
        return "snap/snap";
    }

    @RequestMapping(value = "/check-out", method = RequestMethod.POST)
    public String checkout(@RequestParam("enablePay") List<String> listPay, Model model) {
        config.getSnapApi();
        String clientKey = config.getCLIENT_KEY();

        customBody = new HashMap<>();
        List<String> paymentList = new ArrayList<>();
        if (listPay != null) {
            for (String listPays : listPay) {
                paymentList.add(listPays);
            }
        }

        dataMockup.enablePayments(paymentList);
        customBody.putAll(dataMockup.initDataMock());

        Map<String, Object> dataCheckout = dataMockup.initDataMock();
        model.addAttribute("result", customBody);
        model.addAttribute("clientKey", clientKey);
        model.addAttribute("token", midtransSnapApi.generateToken(dataCheckout));
        return "snap/check-out";
    }

    @RequestMapping(value = "/snap-redirect", method = RequestMethod.POST)
    public String index() {
        config.getSnapApi();
        Map<String, Object> body = dataMockup.initDataMock();
        System.out.println(body.toString());
        JSONObject response = midtransSnapApi.tokenTransaction(body);
        String redirectURL = response.getString("redirect_url");
        return "redirect:"+redirectURL;
    }

}

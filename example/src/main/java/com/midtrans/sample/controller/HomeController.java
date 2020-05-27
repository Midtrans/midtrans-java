package com.midtrans.sample.controller;

import com.midtrans.sample.data.DataMockup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
public class HomeController {

    String clientKey = "SB-Mid-client-nKsqvar5cn60u2Lv";

    @Autowired
    private DataMockup dataMockup;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return index();
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    private String index() {
        return "index";
    }

    @RequestMapping(value = "/mobile-sdk", method = RequestMethod.GET)
    private String mobileSdk() {
        return "mobile/mobile-sdk";
    }

    @RequestMapping(value = "/api/core-api", method = RequestMethod.GET)
    public String coreApi(Model model) {
        Map<String, Object> objectMap = dataMockup.initDataMock();
        model.addAttribute("data", objectMap);
        return "coreapi/core-api";
    }

    @RequestMapping(value = "/api/checkout", method = RequestMethod.GET)
    public String checkOut(@RequestParam(value = "paymentType") String typePayment,
                           Model model) {
        Map<String, Object> result = dataMockup.initDataMock();
        model.addAttribute("result", result);
        model.addAttribute("clientKey", clientKey);
        if (typePayment.equals("cc")) {
            return "coreapi/credit-card";
        } else if (typePayment.equals("gopay")) {
            return "coreapi/gopay";
        }
        return "redirect:/api/core-api";
    }
}

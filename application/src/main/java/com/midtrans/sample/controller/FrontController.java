package com.midtrans.sample.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.api.Config;
import com.midtrans.api.service.MidtransCoreApi;
import com.midtrans.api.service.SnapApi;
import com.midtrans.sample.data.DataMockup;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.*;

@Controller
public class FrontController {

    @Autowired
    private SnapApi snapApi;

    @Autowired
    private Config config;

    @Autowired
    private DataMockup dataMockup;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model){
        Map<String, Object> objectMap = dataMockup.initDataMock();
        model.addAttribute("data", objectMap);
        return "index";
    }

    @RequestMapping(value = "/check-out", method = RequestMethod.GET)
    public String checkout(Model model) {
        config.getSnapApi();
        String clientKey = config.getCLIENT_KEY();
        Map<String, Object> dataCheckout = dataMockup.initDataMock();
        model.addAttribute("result", dataCheckout);
        model.addAttribute("clientKey", clientKey);
        model.addAttribute("token", snapApi.generateToken(dataCheckout));
        return "check-out";
    }

    @RequestMapping(value = "/snap-redirect", method = RequestMethod.POST)
    public String index() {
        config.getSnapApi();
        Map<String, Object> body = dataMockup.initDataMock();
        System.out.println(body.toString());
        JSONObject response = snapApi.tokenTransaction(body);
        String redirectURL = response.getString("redirect_url");
        return "redirect:"+redirectURL;
    }

}

package com.midtrans.sample.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.midtrans.api.Config;
import com.midtrans.api.service.MidtransCoreApi;
import com.midtrans.sample.data.DataMockup;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CoreApiController {

    @Autowired
    private MidtransCoreApi coreApi;

    @Autowired
    private Config config;

    @Autowired
    private DataMockup dataMockup;

    private Map<String, String> cc;

    @RequestMapping(value = "/core-api", method = RequestMethod.GET)
    public String coreApi(Model model) {
        Map<String, Object> objectMap = dataMockup.initDataMock();
        model.addAttribute("data", objectMap);
        return "coreapi/core-api";
    }

    @RequestMapping(value = "/pay-cc", method = RequestMethod.POST, produces = "application/json")
    public String checkoutCC(@RequestParam("cardNumber") String cardNumber,
                             @RequestParam("cardMonth") String cardMonth,
                             @RequestParam("cardYear") String cardYear,
                             @RequestParam("cardCvv") String cvv,
                             @RequestParam(value = "secure", required = false) String secure,
                             Model model) {
        config.getCoreApi();
        cc = new HashMap<>();
        cc.put("client_key", config.getCLIENT_KEY());
        cc.put("card_number", cardNumber);
        cc.put("card_exp_month", cardMonth);
        cc.put("card_exp_year", cardYear);
        cc.put("card_cvv", cvv);

        JSONObject resToken = coreApi.getToken(cc);
        String token = resToken.getString("token_id");

        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("token_id", token);
        if (secure == null) {
            creditCard.put("authentication", "false");
        } else {
            creditCard.put("authentication", "true");
        }
        dataMockup = new DataMockup();
        dataMockup.setPaymentType("credit_card");
        dataMockup.creditCard(creditCard);

        Map<String, Object> newObject = dataMockup.initDataMock();

        JSONObject resTrans = coreApi.cargeCC(newObject);
        if (resTrans.has("redirect_url")) {
            String redirectURL = resTrans.getString("redirect_url");
            return "redirect:" + redirectURL;
        } else {
            model.addAttribute("data", newObject);
            return "coreapi/core-api";
        }
    }



}

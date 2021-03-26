package com.midtrans.sample.controller;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.sample.data.DataMockup;
import com.midtrans.v2.Midtrans;
import com.midtrans.v2.gateway.SnapApi;
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

        /**
         * Midtrans simple sample use : Using Midtrans Global Config class {@link Midtrans}.
         * The Global Config will be use only for Midtrans API static Method {@link com.midtrans.v2}
         * Sample use on Snap transaction request on this Controller @line 84 & 106
         *
         * Also you can setup global configuration for connectTimeout, enableLog, proxyConfig, etc.
         */
        Midtrans.serverKey = sandboxServerKey;
        Midtrans.clientKey = sandboxClientKey;
        Midtrans.isProduction = false;

        // send data to frontEnd snapPopUp
        if (snapType.equals("snap")) {
            model.addAttribute("result", requestBody);

            /**
             * Get ClientKey from Midtrans Global Config {@link Midtrans}
             * */
            String clientKey = Midtrans.getClientKey();
            model.addAttribute("clientKey", clientKey);

            /**
             * Request Snap token to Midtrans API with static Method
             * */
            String snapToken = SnapApi.createTransactionToken(requestBody);

            model.addAttribute("transactionToken", snapToken);
            return "snap/check-out";
        } // send data to frontEnd redirect-url
        else {
            model.addAttribute("result", requestBody);

            // Setup custom headers for Midtrans API request
            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("X-Client-User-Agent", "MidJAVACustomHeader");

            /**
             * Request Snap redirect_url to Midtrans API with static Method
             * */
            String redirectUrl = SnapApi.createTransactionRedirectUrl(requestBody);

            model.addAttribute("redirectURL", redirectUrl);
            return "snap/check-out";
        }
    }
}

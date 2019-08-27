package com.midtrans.sample.controller;

import com.midtrans.api.Config;
import com.midtrans.api.service.SnapApi;
import com.midtrans.sample.data.DataMockup;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.Map;

@RestController
public class SnapController {
    @Autowired
    private SnapApi snapApi;

    @Autowired
    private Config config;

    @Autowired
    private DataMockup dataMockup;

    @PostMapping(value = "/snap-token", produces = "application/json")
    public String snap() {
        config.setSERVER_KEY("SB-Mid-server-Wh2cEDd4H661g4lrcig8sQMf");
        config.setProduction(false);
        config.getSnapApi();

        Map<String, Object> dataMock = dataMockup.initDataMock();
        JSONObject respones = snapApi.tokenTransaction(dataMock);
        String token = respones.getString("token");
        return token;
    }
}

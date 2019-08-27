package com.midtrans.sample.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.midtrans.api.Config;
import com.midtrans.api.service.MidtransCoreApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CoreApiController {

    @Autowired
    private MidtransCoreApi coreApi;

    @Autowired
    private Config config;

    @GetMapping(value = "/card-token", produces = "application/json")
    public String cardToken() {
        //Midtrans Configuration
        config.getCoreApi();

        //Set Params ClientKey and Card Detail Number
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("client_key",config.getCLIENT_KEY());
        params.set("card_number", "4811111111111114");
        params.set("card_exp_month", "11");
        params.set("card_exp_year", "22");
        params.set("card_cvv", "123");

        //Send Params to Midtrans Core API Library
        JSONObject object = coreApi.getToken(params);
        return object.toString();
    }

    @GetMapping(value = "/carge-cc", produces = "application/json")
    public Map<String, Object> cargeCreditCardTransaction() throws IOException {
        //Midtrans Configuration
        config.getCoreApi();

        String json = "{\n" +
                "  \"payment_type\": \"credit_card\",\n" +
                "  \"transaction_details\": {\n" +
                "    \"order_id\": \"order-101a-1122332212\",\n" +
                "    \"gross_amount\": 100000\n" +
                "  },\n" +
                "  \"credit_card\": {\n" +
                "    \"token_id\": \"481111-1114-e37ebac7-61c8-4ed2-a83a-939ceaf5ecd9\",\n" +
                "    \"authentication\": true\n" +
                "  },\n" +
                "  \"item_details\": [{\n" +
                "      \"id\": \"a1\",\n" +
                "      \"price\": 50000,\n" +
                "      \"quantity\": 2,\n" +
                "      \"name\": \"Apel\",\n" +
                "      \"brand\": \"Fuji Apple\",\n" +
                "      \"category\": \"Fruit\",\n" +
                "      \"merchant_name\": \"Fruit-store\"\n" +
                "    }],\n" +
                "    \"customer_details\": {\n" +
                "      \"first_name\": \"BUDI\",\n" +
                "      \"last_name\": \"UTOMO\",\n" +
                "      \"email\": \"noreply@example.com\",\n" +
                "      \"phone\": \"+628123456\",\n" +
                "      \"billing_address\": {\n" +
                "        \"first_name\": \"BUDI\",\n" +
                "        \"last_name\": \"UTOMO\",\n" +
                "        \"email\": \"noreply@example.com\",\n" +
                "        \"phone\": \"081 2233 44-55\",\n" +
                "        \"address\": \"Sudirman\",\n" +
                "        \"city\": \"Jakarta\",\n" +
                "        \"postal_code\": \"12190\",\n" +
                "        \"country_code\": \"IDN\"\n" +
                "      },\n" +
                "      \"shipping_address\": {\n" +
                "        \"first_name\": \"BUDI\",\n" +
                "        \"last_name\": \"UTOMO\",\n" +
                "        \"email\": \"noreply@example.com\",\n" +
                "        \"phone\": \"0 8128-75 7-9338\",\n" +
                "        \"address\": \"Sudirman\",\n" +
                "        \"city\": \"Jakarta\",\n" +
                "        \"postal_code\": \"12190\",\n" +
                "        \"country_code\": \"IDN\"\n" +
                "      }\n" +
                "    }\n" +
                "}";

       Map<String, Object> object = jsonArrayToObject(json);
       JSONObject response = coreApi.cargeCC(object);
       System.out.println(response.toString());
        return object;
    }



    private Map<String , Object> jsonArrayToObject(String json) throws IOException {
        Map<String, Object> body;
        ObjectMapper mapper = new ObjectMapper();
        body = mapper.readValue(json, new TypeReference<HashMap>(){});
        return body;
    }

}

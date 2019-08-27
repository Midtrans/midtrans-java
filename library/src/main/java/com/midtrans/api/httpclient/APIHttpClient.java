package com.midtrans.api.httpclient;

import com.midtrans.api.Config;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class APIHttpClient {

    @Autowired
    private Config config;

    private String encodeServerKey(){
        String serverKey = config.getSERVER_KEY() + ":";
        String encodedAuth = Base64.getEncoder().encodeToString((serverKey).getBytes(StandardCharsets.UTF_8));
        String auth = "Basic " + encodedAuth;
        return auth;
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", encodeServerKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());
        return headers;
    }


    public JSONObject getMethod(MultiValueMap<String, String> params, String URL) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getBASE_URL()+URL)
                .queryParams(params);

        HttpEntity<?> entity = new HttpEntity(setHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

        JSONObject object = new JSONObject(response.getBody());
        if(response.getStatusCode().is2xxSuccessful()){
            System.out.println("Response from Midtrans Backend :" +object.toString());

        } else if (response.getStatusCode().is5xxServerError()) {
            System.out.println("Fail, send data to Midtrans");
        }
        return object;
    }

    public JSONObject postMethod(Map<String, Object> body, String URL) {
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, setHeaders());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(config.getBASE_URL()+URL, request, String.class);
        JSONObject object = new JSONObject(response.getBody());

        if(response.getStatusCode().is2xxSuccessful()){
            System.out.println("Response from Midtrans Backend :" +object.toString());
        } else if (response.getStatusCode().is5xxServerError()) {
            System.out.println("Fail, send data to Midtrans");
        }
        return object;
    }

}



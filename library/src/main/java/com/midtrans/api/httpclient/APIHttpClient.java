package com.midtrans.api.httpclient;

import com.midtrans.api.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class APIHttpClient {
    private static final Logger LOGGER = Logger.getLogger(APIHttpClient.class.getName());

    @Autowired
    private Config config;

    public APIHttpClient() {
    }

    private String encodeServerKey(){
        return "Basic " + Base64.getEncoder().encodeToString((config.getSERVER_KEY() + ":").getBytes(StandardCharsets.UTF_8));
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", encodeServerKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());
        return headers;
    }


    public JSONObject getMethod(MultiValueMap<String, String> params, String URL) throws UnknownHttpStatusCodeException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getBASE_URL()+URL)
                .queryParams(params);

        HttpEntity<?> entity = new HttpEntity(setHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

        JSONObject object = new JSONObject(response.getBody());
        if(response.getStatusCode().is2xxSuccessful()){
            LOGGER.info("Midtrans Info :" + object.toString());

        } else if (response.getStatusCode().is5xxServerError()) {
            LOGGER.warning("Failed send request to Midtrans!");
        }
        return object;
    }

    public JSONObject postMethod(Map<String, Object> body, String URL) throws UnknownHttpStatusCodeException {
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, setHeaders());
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(config.getBASE_URL() + URL, request, String.class);
        JSONObject object = new JSONObject(response.getBody());

        if (response.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Midtrans Info :" + object.toString());
        } else if (response.getStatusCode().is5xxServerError()) {
            LOGGER.warning("Failed send request to Midtrans!");
        }
        return object;
    }

}



package com.midtrans.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.ConfigBuilder;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static com.midtrans.java.mockupdata.Constant.*;

public class SnapApiTest {

    private MidtransSnapApi snapApi;

    @Before
    public void setUp() {
        ConfigFactory configFactory = new ConfigFactory(new ConfigBuilder()
                .setSERVER_KEY(serverKey)
                .setCLIENT_KEY(clientKey)
                .setIsProduction(isProduction)
                .build());
        snapApi = configFactory.getSnapApi();
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[10][0];
    }

    @Test
    public void createTransactionSimpleParam() throws MidtransError {
        JSONObject result = snapApi.createTransaction(miniDataMockUp());
        assert result.has("token");
        assert result.has("redirect_url");
    }

    @Test
    public void createTransactionMaxParam() throws IOException, MidtransError {
        JSONObject result = snapApi.createTransaction(maxDataMockUp());

        assert result.has("token");
        assert result.has("redirect_url");
    }

    @Test
    public void createTransactionToken() throws MidtransError {
        String token = snapApi.createTransactionToken(miniDataMockUp());
        assert token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }

    @Test
    public void createTransactionRedirectUrl() throws MidtransError {
        String redirectURL = snapApi.createTransactionRedirectUrl(miniDataMockUp());
        String expected = "https://app.sandbox.midtrans.com/snap/v2/vtweb/";
        assertEquals(expected, redirectURL.substring(0, 47));
    }

    @Test
    public void badRequestBodyOnSnapTrans() throws MidtransError {
        JSONObject result = snapApi.createTransaction(badDataMockUp());
        assert result.toString().contentEquals("{}");
        assert result.length() == 0;
    }

    @Test
    public void errorServerKey() throws MidtransError {
        snapApi.apiConfig().setSERVER_KEY("");
        JSONObject result = snapApi.createTransaction(miniDataMockUp());
        assert result.toString().contentEquals("{}");
        assert result.length() == 0;

    }

    //Minimal data mockUp
    private Map<String, Object> miniDataMockUp() {
        UUID idRand = UUID.randomUUID();

        Map<String, String> transDetail = new HashMap<>();
        transDetail.put("order_id", idRand.toString());
        transDetail.put("gross_amount", "265000");

        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("secure", "true");

        Map<String, Object> body = new HashMap<>();
        body.put("credit_card", creditCard);
        body.put("transaction_details", transDetail);

        return body;
    }

    //BAD Data Request
    private Map<String, Object> badDataMockUp() {
        UUID idRand = UUID.randomUUID();

        Map<String, String> transDetail = new HashMap<>();
        transDetail.put("order_id", idRand.toString());
        transDetail.put("gross_amount", "265000");

        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("secure", "true");

        Map<String, Object> body = new HashMap<>();
        body.put("credit_card", creditCard);
        body.put("items_detail", transDetail);

        return body;
    }

    //Max data MockUp
    private Map<String, Object> maxDataMockUp() throws IOException {
        UUID idRand = UUID.randomUUID();
        String json = "{\n" +
                "  \"transaction_details\": {\n" +
                "    \"order_id\": \"java-" + idRand + "\",\n" +
                "    \"gross_amount\": 100000\n" +
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
                "    },\n" +
                "   \"credit_card\": {\n" +
                "       \"secure\": true,\n" +
                "       \"channel\": \"migs\",\n" +
                "       \"bank\": \"bca\",\n" +
                "       \"installment\": {\n" +
                "           \"required\": false,\n" +
                "           \"terms\": {\n" +
                "               \"bni\": [3, 6, 12],\n" +
                "               \"mandiri\": [3, 6, 12],\n" +
                "               \"cimb\": [3],\n" +
                "               \"bca\": [3, 6, 12],\n" +
                "               \"offline\": [6, 12]\n" +
                "           }\n" +
                "      },\n" +
                "       \"whitelist_bins\": [\n" +
                "        \"48111111\",\n" +
                "        \"41111111\"\n" +
                "      ]\n" +
                "    },\n" +
                "  \"bca_va\": {\n" +
                "      \"va_number\": \"12345678911\",\n" +
                "      \"free_text\": {\n" +
                "        \"inquiry\": [\n" +
                "          {\n" +
                "            \"en\": \"text in English\",\n" +
                "            \"id\": \"text in Bahasa Indonesia\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"payment\": [\n" +
                "          {\n" +
                "            \"en\": \"text in English\",\n" +
                "            \"id\": \"text in Bahasa Indonesia\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"bni_va\": {\n" +
                "      \"va_number\": \"12345678\"\n" +
                "    },\n" +
                "    \"permata_va\": {\n" +
                "      \"va_number\": \"1234567890\",\n" +
                "      \"recipient_name\": \"SUDARSONO\"\n" +
                "    },\n" +
                "    \"callbacks\": {\n" +
                "      \"finish\": \"https://demo.midtrans.com\"\n" +
                "    },\n" +
                "    \"expiry\": {\n" +
                "      \"start_time\": \"2025-12-20 18:11:08 +0700\",\n" +
                "      \"unit\": \"minutes\",\n" +
                "      \"duration\": 1\n" +
                "    },\n" +
                "    \"custom_field1\": \"custom field 1 content\",\n" +
                "    \"custom_field2\": \"custom field 2 content\",\n" +
                "    \"custom_field3\": \"custom field 3 content\",\n" +
                "    \"enabled_payments\": [\"credit_card\", \"mandiri_clickpay\", \"cimb_clicks\",\"bca_klikbca\", \"bca_klikpay\", \"bri_epay\", \"echannel\", \"indosat_dompetku\",\"mandiri_ecash\", \"permata_va\", \"bca_va\", \"bni_va\", \"other_va\", \"gopay\",\"kioson\", \"indomaret\", \"gci\", \"danamon_online\"]\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        return body;
    }
}
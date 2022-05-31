package com.midtrans.java.mockupdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.Config;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static com.midtrans.java.mockupdata.Constant.cardNumberFDS;

public class DataMockup {

    private List<String> listedPayment;
    private Map<String, String> creditCard;
    private String paymentType = "";
    private String bank = "";

    public static final String refNumber = "032b1bae14578690bb";

    public void enablePayments(List<String> listPayment) {
        listedPayment = new ArrayList<>();
        listedPayment.addAll(listPayment);
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void creditCard(Map<String, String> params) {
        creditCard = new HashMap<>();
        creditCard.putAll(params);
    }

    public void setBank(String bank) {
        this.bank = bank;
    }


    public Map<String, Object> initDataMock() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Map<String, String> transDetail = new HashMap<>();
        transDetail.put("order_id", "java-unittest-"+timestamp.getTime());
        transDetail.put("gross_amount", "265000");

        List<Map<String, String>> items = new ArrayList<>();
        Map<String, String> item1 = new HashMap<>();
        item1.put("id", "ID001");
        item1.put("price", "15000");
        item1.put("quantity", "1");
        item1.put("name", "Sendal Karet Rumahan");
        item1.put("brand", "Suwaslow");
        item1.put("category", "Sanitasi");
        item1.put("merchant_name", "SnowlID");

        Map<String, String> item2 = new HashMap<>();
        item2.put("id", "ID002");
        item2.put("price", "200000");
        item2.put("quantity", "1");
        item2.put("name", "Mantel Hujan");
        item2.put("brand", "Excel");
        item2.put("category", "Sanitasi");
        item2.put("merchant_name", "SnowlID");

        Map<String, String> item3 = new HashMap<>();
        item3.put("id", "ID003");
        item3.put("price", "50000");
        item3.put("quantity", "1");
        item3.put("name", "Sarung Tangan Karet");
        item3.put("brand", "Cap Anti Sobek");
        item3.put("category", "Sanitasi");
        item3.put("merchant_name", "SnowlID");

        items.add(item1);
        items.add(item2);
        items.add(item3);

        Map<String, Object> billingAddres = new HashMap<>();
        billingAddres.put("first_name", "Zaki");
        billingAddres.put("last_name", "Ibrhim");
        billingAddres.put("email", "zaki@mailnesia.com");
        billingAddres.put("phone", "0928282828");
        billingAddres.put("address", "Jalan Iskandarsyah II");
        billingAddres.put("city", "Jakarta Selatan");
        billingAddres.put("postal_code", "10120");
        billingAddres.put("country_code", "IDN");

        Map<String, Object> custDetail = new HashMap<>();
        custDetail.put("first_name", "Zaki");
        custDetail.put("last_name", "ibrahim");
        custDetail.put("email", "zaki@mailnesia.com");
        custDetail.put("phone", "62783737373");
        custDetail.put("billing_address", billingAddres);

        Map<String, Object> body = new HashMap<>();
        if (creditCard != null) {
            body.put("credit_card", creditCard);
        }
        body.put("transaction_details", transDetail);
        body.put("item_details", items);
        body.put("customer_details", custDetail);
        if (!paymentType.isEmpty()) {
            body.put("payment_type", paymentType);
        }
        if (listedPayment != null) {
            body.put("enabled_payments", listedPayment);
        }

        if (paymentType.equals("bank_transfer")) {
            Map<String, Object> bankTransfer = new HashMap<>();
            bankTransfer.put("bank", bank);
            body.put("bank_transfer", bankTransfer);
        }

        return body;
    }

    public Map<String, String> initDataBeneficiaries() {
        int prefix = random();
        String ptName = "JavaUnitTest " +prefix;
        String email = "midjava"+prefix+"@mail.com";

        Map<String, String> beneficiarie = new HashMap<>();
        beneficiarie.put("bank", "bca");
        beneficiarie.put("name", ptName);
        beneficiarie.put("alias_name", ptName.toLowerCase().replaceAll("\\s+", ""));
        beneficiarie.put("account", String.valueOf(prefix)+ prefix);
        beneficiarie.put("email", email.trim());
        return beneficiarie;
    }

    public int random() {
        Random r = new Random( System.currentTimeMillis() );
        return 10000 + r.nextInt(20000);
    }

    public Map<String, Object> initDataRequestPayout(JSONObject beneficiaries) {
        /*
        Initialize beneficiaries for request payout
         */
        Map<String, String> payoutObject = new HashMap<>();
        payoutObject.put("beneficiary_name", beneficiaries.getString("name"));
        payoutObject.put("beneficiary_account", beneficiaries.getString("account"));
        payoutObject.put("beneficiary_bank", beneficiaries.getString("bank"));
        payoutObject.put("beneficiary_email", beneficiaries.getString("email"));
        payoutObject.put("amount", String.valueOf(random()*2));
        payoutObject.put("notes", "Payout Unit Test "+ random());

        ArrayList<Map<String,String>> payoutBeneficiaries = new ArrayList<>();
        payoutBeneficiaries.add(payoutObject);

        Map<String, Object> payouts = new HashMap<>();
        payouts.put("payouts", payoutBeneficiaries);

        return payouts;
    }

    //Minimal data mockUp
    public Map<String, Object> miniDataMockUp() {
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
    public Map<String, Object> badDataMockUp() {
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
    public Map<String, Object> maxDataMockUp() throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String json = "{\n" +
                "  \"transaction_details\": {\n" +
                "    \"order_id\": \"java-unittest" + timestamp.getTime() + "\",\n" +
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

    public Map<String, Object> subscriptionRequest(String subscriptionName, int interval, String unit) {
        HashMap<String, Object> map = new HashMap<>();
        String json = "{\n" +
                "    \"name\": \"" + subscriptionName + "\",\n" +
                "    \"amount\": \"14000\",\n" +
                "    \"currency\": \"IDR\",\n" +
                "    \"payment_type\": \"credit_card\",\n" +
                "    \"token\": \"dummy\",\n" +
                "    \"schedule\": {\n" +
                "      \"interval\": " + interval + ",\n" +
                "      \"interval_unit\": \"" + unit + "\",\n" +
                "      \"max_interval\": 12\n" +
                "    },\n" +
                "    \"metadata\": {\n" +
                "      \"description\": \"Recurring payment for A\"\n" +
                "    },\n" +
                "    \"customer_details\": {\n" +
                "      \"first_name\": \"John\",\n" +
                "      \"last_name\": \"Doe\",\n" +
                "      \"email\": \"midtrans-java@mailnesia.com\",\n" +
                "      \"phone\": \"+62812345678\"\n" +
                "    }\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        try {
            //Convert Map to JSON
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    // Make dummy transaction for get orderId
    public static String makeTransaction(Config config) throws MidtransError {
        DataMockup dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");
        JSONObject result;

        if (config == null) {
            result = CoreApi.chargeTransaction(dataMockup.initDataMock());
        } else {
            result = CoreApi.chargeTransaction(dataMockup.initDataMock(), config);
        }
        return result.getString("order_id");
    }

    // MockUp Transaction FDS Challenge
    public static String makeFDSTransaction(Config config) throws MidtransError {
        DataMockup dataMockup = new DataMockup();
        dataMockup.setPaymentType("credit_card");
        Map<String, String> cc = new HashMap<>();
        cc.put("token_id", genCardToken(cardNumberFDS, config));
        dataMockup.creditCard(cc);

        JSONObject result = CoreApi.chargeTransaction(dataMockup.initDataMock(), config);
        return result.getString("order_id");
    }

    // Mock CreditCard Data
    public static Map<String, String> creditCard(String cardNumber) {
        Map<String, String> cardParams = new HashMap<>();
        cardParams.put("card_number", cardNumber);
        cardParams.put("card_exp_month", "12");
        cardParams.put("card_exp_year", "2022");
        cardParams.put("card_cvv", "123");
        return cardParams;
    }

    //For generate tokenCard
    public static String genCardToken(String cardNumber, Config config) throws MidtransError {
        JSONObject result = CoreApi.cardToken(creditCard(cardNumber), config);
        return result.getString("token_id");
    }

    public Map<String, Object> jsonToMap(String json) {
        HashMap<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Object> simpleDataMock(String orderId, String paymentType) {
        Map<String, String> transDetail = new HashMap<>();
        transDetail.put("order_id", orderId);
        transDetail.put("gross_amount", "265000");

        Map<String, Object> body = new HashMap<>();
        body.put("transaction_details", transDetail);
        body.put("payment_type", paymentType);

        return body;
    }

}

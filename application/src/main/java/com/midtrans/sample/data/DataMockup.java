package com.midtrans.sample.data;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataMockup {

    public Map<String, Object> initDataMock(){
        UUID idRand = UUID.randomUUID();

        Map<String, String> transDetail = new HashMap<>();
        transDetail.put("order_id", idRand.toString());
        transDetail.put("gross_amount", "265000");

        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("secure", "true");

        List<Map<String, String>> items = new ArrayList<>();
        Map<String,String> item1 = new HashMap<>();
        item1.put("id","ID001");
        item1.put("price", "15000");
        item1.put("quantity", "1");
        item1.put("name", "Sendal Karet Rumahan");
        item1.put("brand", "Suwaslow");
        item1.put("category", "Sanitasi");
        item1.put("merchant_name", "SnowlID");

        Map<String, String> item2 = new HashMap<>();
        item2.put("id","ID002");
        item2.put("price", "200000");
        item2.put("quantity", "1");
        item2.put("name", "Mantel Hujan");
        item2.put("brand", "Excel");
        item2.put("category", "Sanitasi");
        item2.put("merchant_name", "SnowlID");

        Map<String, String> item3 = new HashMap<>();
        item3.put("id","ID003");
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
        body.put("credit_card", creditCard);
        body.put("transaction_details", transDetail);
        body.put("item_details", items);
        body.put("customer_details", custDetail);

        return body;
    }
}

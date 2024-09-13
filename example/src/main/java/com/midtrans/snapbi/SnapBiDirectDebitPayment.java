package com.midtrans.snapbi;

import org.json.JSONObject;

import java.util.*;

public class SnapBiDirectDebitPayment {

    static String externalId = UUID.randomUUID().toString();
    static String clientId = "Zabcdefg-MIDTRANS-CLIENT-SNAP";
    static String privateKey = "-----BEGIN PRIVATE KEY-----"
            +"ABcDeFGHijklMnOpQrsU/6osrLPS6TXpoqo0ozgfjDD1B1TzfkJZ0AF/5oLYEVCdsMufKLcaVKBZDC32ZVSQ3z7sa85rTOiYfm+Cje/S6FpvnC0ovknjshJhPXRBooitL2kF1o3yhuhak2V99Mvxyy79AnF1xuYLpfnlTYk5yhVuvnToBlflS3twp5NTivl143T/K38T42FAfl4uABNsxQHnvA7ZQApyNW5wZiOx41PyXWmf2Xp/YyqZ5x56Yui+ZLtqxZpZqyvBX7idjhBZnnUtg7d7IVDdRtXNn7XJVAgMBAAECggEAcu5amQOjUFBzNhnXrv+tNFQT0oL77CP83r63o7k5xnNzgLLBVw6x+uOvFqV6un4TlswUOSvoeYCNxHFlbxj7rfhaBUYyBEPV87j/LCADVr/VaSnB9cfefkyv4fnl+b/nyNT9kiduVlvcvGsNrlaf0braAJxEkmoFccpr/vE7fXwPfO8X7Y9dFvlo+HrzVBRYGcKpzKR7NQltV+mGaDc4tnAOHfZ4SOZveqGFudZ46bTlQ55OkRS1aRpj7GvOSETHpWQwiMv3jJdVfQTCUKOYpdkWiKa+xVzUbVrXVsFd2x1yDxcc7Aj+WcZWlQKBgQDMyifSua/xOtbGaqb7ukKWk1LalXB2Xmwcmg20PK2/UAjGsxCY8sHuBtDEeU5V6v8AT5M+g9z4bFwWBmOmc1kMVh/FZp1jPB783LNKSuK/9fcWjINEI6vAUoKvTApS2rP/1qLg/EokO/luPWVwKBgQCm+RN92x889jmZtfddeOLX3p8+Up+c58h8n0yOSLyWm3vZ8OtlfVWBG/wkLwopQAN8pcISVKTzdemPnjRRJ/GXQdhrXGOBbGOBOm/TpV2snJWEOrJs3oTBQtD64FPT41zzecdPG70YRjwTSe9hvolHVp/HIhTAP2q65e4uGrbZMwKBgQCPWZbEsAxTrN84puFaZghEhL3DmkXN5cyBvOiI2MyRfZKpwcwsL4455U/NYS/R5w60Sb8/cgt6mPlqZfas/qKAvIoqV7Xzhv0zfzgU54cGKC2k1kEElPEcRr+WKrmAW8pGzS+GbMlvEv2lRYXKlz8aNsWgETvffny1JOyNiR7VIwKBgGf6FTw7dsF0pzvkB81qWQCKfmnF8+U2pS+N15OHEgFLg52dHElP+i3zKlmx/UbguQsnAap+kVWWqMIph3S+RgCmHUoF92UCBRjVNgv7H1E9FRKJptCK5OqgXBjOFwWMwTZRMF8/68ow+nocgXhBuaT1+Xt86qUpGKKYSnDla9XnAoGAD5Tt/NGy5tZid9+NBXXU7yNSmJ5t6KLsN7VWlSq30ezJFuPSCE+rWkXyPfhmY/zMDbPmGUCGK+iE5vgefm6QgtaR38f4EfNl1z6o4KlDRUllJjNqdw+894gjd+Aj4f3FmgSZa4BlZTdQcHWh/abcdeFg1234="
            +"-----END PRIVATE KEY-----";
    static String clientSecret = "ABcdefghiSrLJPgKRXqdjaSAuj5WDAbeaXAX8Vn7CWGHuBCfFgABCDVqRLvNZf8BaqPGKaksMjrZDrZqzZEbaA1AYFwBewIWCqLZr4PuvuLBqfTmYIzAbCakHKejABCa";
    static String partnerId = "partner-id";
    static String merchantId = "M001234";
    static String channelId = "12345";
    public static void main(String[] args) throws Exception {

        SnapBiConfig.setSnapBiClientId(clientId);
        SnapBiConfig.setSnapBiPrivateKey(privateKey);
        SnapBiConfig.setSnapBiClientSecret(clientSecret);
        SnapBiConfig.setSnapBiPartnerId(partnerId);
        SnapBiConfig.setSnapBiChannelId(channelId);
        SnapBiConfig.setEnableLogging(true);

        /*
         * Example code for Direct Debit (gopay/ dana/ shopeepay) using Snap Bi, you can uncomment and run the code.
         * Below are example code to create the payment
         */

        /*
         * Basic example
         * To change the payment method, you can change the value of the request body on the `payOptionDetails`
         */
        JSONObject snapBiResponse1 = SnapBi.directDebit()
                .withBody(createDirectDebitRequestBody())
                .createPayment(externalId);

        /*
         * Example of using existing access token to create payment. You can uncomment and run the code
         * To change the payment method, you can change the value of the request body on the `payOptionDetails`
         */
        JSONObject snapBiResponse2 = SnapBi.directDebit()
                .withBody(createDirectDebitRequestBody())
                .withAccessToken("")
                .createPayment(externalId);

        /*
         * Example of using additional header on access token and when doing transaction  header.
         * To change the payment method, you can change the value of the request body on the `payOptionDetails`
         */
        JSONObject snapBiResponse3 = SnapBi.directDebit()
                .withBody(createDirectDebitRequestBody())
                .withAccessTokenHeader(createAdditionalHeader())
                .withTransactionHeader(createAdditionalHeader())
                .createPayment(externalId);

        System.out.println("Snap Bi response3 : " + snapBiResponse3);
    }
    public static Map<String, Object> createDirectDebitRequestBody() {
        // Create the top-level map
        Map<String, Object> requestBody = new HashMap<>();

        // Add partnerReferenceNo and other fields
        requestBody.put("partnerReferenceNo", externalId);
        requestBody.put("chargeToken", "");
        requestBody.put("merchantId", merchantId);
        requestBody.put("validUpTo", "2030-07-20T20:34:15.452305Z");

        // Create and add urlParam map
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("url", "https://midtrans-test.com/api/notification");
        urlParam.put("type", "PAY_RETURN");
        urlParam.put("isDeeplink", "N");
        requestBody.put("urlParam", urlParam);

        // Create and add payOptionDetails list
        List<Map<String, Object>> payOptionDetails = new ArrayList<>();
        Map<String, Object> payOptionDetail = new HashMap<>();
        payOptionDetail.put("payMethod", "GOPAY");
        payOptionDetail.put("payOption", "GOPAY_WALLET");

        // Create and add transAmount map
        Map<String, String> transAmount = new HashMap<>();
        transAmount.put("value", "1500");
        transAmount.put("currency", "IDR");
        payOptionDetail.put("transAmount", transAmount);

        payOptionDetails.add(payOptionDetail);
        requestBody.put("payOptionDetails", payOptionDetails);

        // Create and add additionalInfo map
        Map<String, Object> additionalInfo = new HashMap<>();

        // Create and add customerDetails map
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("firstName", "Merchant");
        customerDetails.put("lastName", "Operation");
        customerDetails.put("email", "merchant-ops@midtrans.com");
        customerDetails.put("phone", "+6281932358123");

        // Create and add billingAddress map
        Map<String, String> billingAddress = new HashMap<>();
        billingAddress.put("firstName", "Merchant");
        billingAddress.put("lastName", "Operation");
        billingAddress.put("phone", "+6281932358123");
        billingAddress.put("address", "Pasaraya Blok M");
        billingAddress.put("city", "Jakarta");
        billingAddress.put("postalCode", "12160");
        billingAddress.put("countryCode", "IDN");
        customerDetails.put("billingAddress", billingAddress);

        // Create and add shippingAddress map
        Map<String, String> shippingAddress = new HashMap<>();
        shippingAddress.put("firstName", "Merchant");
        shippingAddress.put("lastName", "Operation");
        shippingAddress.put("phone", "+6281932358123");
        shippingAddress.put("address", "Pasaraya Blok M");
        shippingAddress.put("city", "Jakarta");
        shippingAddress.put("postalCode", "12160");
        shippingAddress.put("countryCode", "IDN");
        customerDetails.put("shippingAddress", shippingAddress);

        additionalInfo.put("customerDetails", customerDetails);

        // Create and add items list
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("id", "8143fc4f-ec05-4c55-92fb-620c212f401e");

        // Create and add price map
        Map<String, String> price = new HashMap<>();
        price.put("value", "1500.00");
        price.put("currency", "IDR");
        item.put("price", price);

        item.put("quantity", 1);
        item.put("name", "test item name");
        item.put("brand", "test item brand");
        item.put("category", "test item category");
        item.put("merchantName", "Merchant Operation");

        items.add(item);
        additionalInfo.put("items", items);

        requestBody.put("additionalInfo", additionalInfo);

        return requestBody;
    }
    public static  Map<String, String > createAdditionalHeader(){
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Device-id", "device id");
        headers.put("debug-id", "debug id");
        return headers;
    }

}

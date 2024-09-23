package com.midtrans.snapbi;

import org.json.JSONObject;

import java.util.*;

public class SnapBiQris {

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
         * Example code for Qris using Snap Bi, you can uncomment and run the code.
         * Below are example code to create the payment
         */

        /*
         * Basic example to create Qris transaction
         */
        JSONObject snapBiResponse1 = SnapBi.qris()
                .withBody(createQrisRequestBody())
                .createPayment(externalId);

        /*
         * Example of Qris using existing access token to create payment.
         */
        JSONObject snapBiResponse2 = SnapBi.qris()
                .withBody(createQrisRequestBody())
                .withAccessToken("")
                .createPayment(externalId);

        /*
         * Example of Qris using additional header on access token and when doing transaction  header.
         */
        JSONObject snapBiResponse3 = SnapBi.qris()
                .withBody(createQrisRequestBody())
                .withAccessTokenHeader(createAdditionalHeader())
                .withTransactionHeader(createAdditionalHeader())
                .createPayment(externalId);

        System.out.println("Snap Bi response3 : " + snapBiResponse3);
    }
    public static Map<String, Object> createQrisRequestBody() {
        // Create the top-level map
        Map<String, Object> requestBody = new HashMap<>();

        // Add partnerReferenceNo and other fields
        requestBody.put("partnerReferenceNo", externalId);
        requestBody.put("merchantId", merchantId);
        requestBody.put("validityPeriod", "2030-07-03T12:08:56-07:00");

        // Create and add amount map
        Map<String, String> amount = new HashMap<>();
        amount.put("value", "1500.00");
        amount.put("currency", "IDR");
        requestBody.put("amount", amount);

        // Create and add additionalInfo map
        Map<String, Object> additionalInfo = new HashMap<>();

        // Create and add customerDetails map
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("firstName", "Merchant");
        customerDetails.put("lastName", "Operation");
        customerDetails.put("email", "merchant-ops@midtrans.com");
        customerDetails.put("phone", "+6281932358123");

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
        additionalInfo.put("acquirer", "gopay");
        additionalInfo.put("customerDetails", customerDetails);
        additionalInfo.put("countryCode", "ID");
        additionalInfo.put("locale", "id_ID");

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

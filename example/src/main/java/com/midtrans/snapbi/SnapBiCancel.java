package com.midtrans.snapbi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SnapBiCancel {

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
         * Example code for SnapBI cancel.
         * The difference is based on the request body/ payload.
         * For Direct Debit you can refer to the method createDirectDebitCancelByExternalIdBody() or createDirectCancelByReferenceNoBody() to see the value.
         * For VA (Bank Transfer) you can refer to the variable $vaStatusBody to see the value.
         * For qris, you can refer to the variable $qrisStatusBody.
         */

        /*
         * Example code for Direct Debit cancel using externalId
         */
        JSONObject snapBiResponse1 = SnapBi.directDebit()
                .withBody(createDirectCancelByReferenceNoBody())
                .cancel(externalId);

        /*
         * Example code for Direct Debit cancel using externalId by re-using access token
         */
        JSONObject snapBiResponse2 = SnapBi.directDebit()
                .withBody(createDirectCancelByReferenceNoBody())
                .withAccessToken("")
                .cancel(externalId);

        /*
         *  Example code for Direct Debit cancel using externalId by adding additional header
         */
        JSONObject snapBiResponse3 = SnapBi.directDebit()
                .withBody(createDirectCancelByReferenceNoBody())
                .withAccessTokenHeader(createAdditionalHeader())
                .withTransactionHeader(createAdditionalHeader())
                .cancel(externalId);

        /*
         * Example code for Direct Debit cancel using ReferenceNo
         */
        JSONObject snapBiResponse4 = SnapBi.directDebit()
                .withBody(createDirectDebitCancelByExternalIdBody())
                .cancel(externalId);

        /*
         * Example code for Direct Debit cancel using ReferenceNo by re-using access token
         */
        JSONObject snapBiResponse5 = SnapBi.directDebit()
                .withBody(createDirectDebitCancelByExternalIdBody())
                .withAccessToken("")
                .cancel(externalId);

        /*
         *  Example code for Direct Debit cancel using ReferenceNo by adding additional header
         */
        JSONObject snapBiResponse6 = SnapBi.directDebit()
                .withBody(createDirectDebitCancelByExternalIdBody())
                .withAccessTokenHeader(createAdditionalHeader())
                .withTransactionHeader(createAdditionalHeader())
                .cancel(externalId);


        System.out.println("Snap Bi response : " + snapBiResponse6);

    }
    public static Map<String, Object> createDirectDebitCancelByExternalIdBody
            () {
        // Create the top-level map
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("originalExternalId", "67be74c3-8cf5-4af3-b62b-0715899fd713");
        return requestBody;
    }

    public static Map<String, Object> createDirectCancelByReferenceNoBody
            () {
        // Create the top-level map
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("originalReferenceNo", "A120240913100313Zmpy1tA0MtID");
        return requestBody;
    }
    public static  Map<String, String > createAdditionalHeader(){
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Device-id", "device id");
        headers.put("debug-id", "debug id");
        return headers;
    }
}

package com.midtrans.snapbi;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.security.spec.PKCS8EncodedKeySpec;

import org.json.JSONObject;

public class SnapBi {

    public static final String ACCESS_TOKEN = "/v1.0/access-token/b2b";
    public static final String PAYMENT_HOST_TO_HOST = "/v1.0/debit/payment-host-to-host";
    public static final String CREATE_VA = "/v1.0/transfer-va/create-va";
    public static final String DEBIT_STATUS = "/v1.0/debit/status";
    public static final String DEBIT_REFUND = "/v1.0/debit/refund";
    public static final String DEBIT_CANCEL = "/v1.0/debit/cancel";
    public static final String VA_STATUS = "/v1.0/transfer-va/status";
    public static final String VA_CANCEL = "/v1.0/transfer-va/delete-va";
    public static final String QRIS_PAYMENT = "/v1.0/qr/qr-mpm-generate";
    public static final String QRIS_STATUS = "/v1.0/qr/qr-mpm-query";
    public static final String QRIS_REFUND = "/v1.0/qr/qr-mpm-refund";
    public static final String QRIS_CANCEL = "/v1.0/qr/qr-mpm-cancel";

    private String apiPath;
    private String paymentMethod;
    private Map<String, String> accessTokenHeader = new HashMap<>();
    private Map<String, String> transactionHeader = new HashMap<>();
    private String accessToken;
    private Map<String, Object> body;
    private String privateKey;
    private String clientId;
    private String partnerId;
    private String channelId;
    private String clientSecret;
    private String deviceId;
    private String debugId;
    private String timeStamp;
    private String signature;
    private String notificationUrlPath;
    private String notificationPayload;


    public SnapBi(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.timeStamp = Instant.now().toString();
    }

    public static SnapBi directDebit() {
        return new SnapBi("directDebit");
    }

    public static SnapBi va() {
        return new SnapBi("va");
    }

    public static SnapBi qris() {
        return new SnapBi("qris");
    }

    public static SnapBi notification() {
        return new SnapBi("");
    }
    
    public SnapBi withAccessTokenHeader(Map<String, String> headers) {
        this.accessTokenHeader.putAll(headers);
        return this;
    }

    public SnapBi withTransactionHeader(Map<String, String> headers) {
        this.transactionHeader.putAll(headers);
        return this;
    }

    public SnapBi withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public SnapBi withBody(Map<String, Object> body) {
        this.body = body;
        return this;
    }

    public SnapBi withPrivateKey(String privateKey) {
        SnapBiConfig.setSnapBiPrivateKey(privateKey);
        return this;
    }

    public SnapBi withClientId(String clientId) {
        SnapBiConfig.setSnapBiClientId(clientId);
        return this;
    }

    public SnapBi withClientSecret(String clientSecret) {
        SnapBiConfig.setSnapBiClientSecret(clientSecret);
        return this;
    }

    public SnapBi withPartnerId(String partnerId) {
        SnapBiConfig.setSnapBiPartnerId(partnerId);
        return this;
    }

    public SnapBi withChannelId(String channelId) {
        SnapBiConfig.setSnapBiChannelId(channelId);
        return this;
    }

    public SnapBi withDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public SnapBi withDebugId(String debugId) {
        this.debugId = debugId;
        return this;
    }

    public SnapBi withSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public SnapBi withTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public SnapBi withNotificationUrlPath(String notificationUrlPath) {
        this.notificationUrlPath = notificationUrlPath;
        return this;
    }

    public SnapBi withNotificationPayload(String notificationPayload) {
        this.notificationPayload = notificationPayload;
        return this;
    }

    public JSONObject createPayment(String externalId) throws Exception {
        this.apiPath = setupCreatePaymentApiPath(this.paymentMethod);
        return createConnection(externalId);
    }

    public JSONObject cancel(String externalId) throws Exception {
        this.apiPath = setupCancelApiPath(this.paymentMethod);
        return createConnection(externalId);
    }

    public JSONObject refund(String externalId) throws Exception {
        this.apiPath = setupRefundApiPath(this.paymentMethod);
        return createConnection(externalId);
    }

    public JSONObject getStatus(String externalId) throws Exception {
        this.apiPath = setupGetStatusApiPath(this.paymentMethod);
        return createConnection(externalId);
    }

    public Boolean isWebhookNotificationVerified() throws Exception {
        String minifiedBody = minifyJson(this.notificationPayload);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedNotificationBodyJsonString = digest.digest(minifiedBody.getBytes());
        String hashedNotificationBodyJsonStringHex = bytesToHex(hashedNotificationBodyJsonString)
                .toLowerCase();
        String rawStringDataToVerifyAgainstSignature = "POST" +
                ":" +
                this.notificationUrlPath +
                ":" +
                hashedNotificationBodyJsonStringHex +
                ":" +
                this.timeStamp;
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(getPublicKey(SnapBiConfig.getSnapBiPublicKey()));
        verifier.update(rawStringDataToVerifyAgainstSignature.getBytes(StandardCharsets.UTF_8));
        boolean isSignatureVerified = verifier.verify(Base64.getDecoder().decode(this.signature));

        return isSignatureVerified;
    }

    private static PublicKey getPublicKey(String publicKeyString) throws Exception {
        String publicKeyPEM = publicKeyString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public JSONObject getAccessToken() throws Exception {
        Map<String, String> snapBiAccessTokenHeader = buildAccessTokenHeader(this.timeStamp);
        Map<String, String> openApiPayload = new HashMap<>();
        openApiPayload.put("grant_type", "client_credentials");

        return SnapBiApiRequestor.remoteCall(
                SnapBiConfig.getSnapBiTransactionBaseUrl() + ACCESS_TOKEN,
                snapBiAccessTokenHeader, openApiPayload
        );
    }

    private JSONObject createConnection(String externalId) throws Exception {
        // Attempt to get the access token if it's not already set
        if (accessToken == null || accessToken.isEmpty()) {
            JSONObject accessTokenResponse = getAccessToken();

            // If getting the access token failed, return the response from getAccessToken
            if (!accessTokenResponse.has("accessToken")) {
                return accessTokenResponse;
            }
            // Set the access token if it was successfully retrieved
            accessToken = accessTokenResponse.getString("accessToken");
        }

        // Proceed with the payment creation if access token is available
        Map<String, String> snapBiTransactionHeader = buildSnapBiTransactionHeader(externalId, timeStamp);

        // Make the remote call and return the response
        String url = SnapBiConfig.getSnapBiTransactionBaseUrl() + apiPath;
        return SnapBiApiRequestor.remoteCall(url, snapBiTransactionHeader, body);
    }

    private Map<String, String> buildSnapBiTransactionHeader(String externalId, String timeStamp) {
        Map<String, String> snapBiTransactionHeader = new HashMap<>();
        snapBiTransactionHeader.put("Content-Type", "application/json");
        snapBiTransactionHeader.put("Accept", "application/json");
        snapBiTransactionHeader.put("X-PARTNER-ID", SnapBiConfig.getSnapBiPartnerId());
        snapBiTransactionHeader.put("X-EXTERNAL-ID", externalId);
        snapBiTransactionHeader.put("X-DEVICE-ID", this.deviceId != null ? this.deviceId : "");
        snapBiTransactionHeader.put("CHANNEL-ID", SnapBiConfig.getSnapBiChannelId());
        snapBiTransactionHeader.put("debug-id", this.debugId != null ? this.debugId : "");
        snapBiTransactionHeader.put("Authorization", "Bearer " + this.accessToken);
        snapBiTransactionHeader.put("X-TIMESTAMP", timeStamp);
        snapBiTransactionHeader.put("X-SIGNATURE", SnapBi.getSymmetricSignatureHmacSh512(
                this.accessToken, this.body, "POST", this.apiPath, SnapBiConfig.getSnapBiClientSecret(), timeStamp
        ));

        if (!this.transactionHeader.isEmpty()) {
            snapBiTransactionHeader.putAll(this.transactionHeader);
        }

        return snapBiTransactionHeader;
    }

    private Map<String, String> buildAccessTokenHeader(String timeStamp) throws Exception {
        Map<String, String> snapBiAccessTokenHeader = new HashMap<>();
        snapBiAccessTokenHeader.put("Content-Type", "application/json");
        snapBiAccessTokenHeader.put("Accept", "application/json");
        snapBiAccessTokenHeader.put("X-CLIENT-KEY", SnapBiConfig.getSnapBiClientId());
        snapBiAccessTokenHeader.put("X-SIGNATURE", SnapBi.getAsymmetricSignatureSha256WithRsa(
                SnapBiConfig.getSnapBiClientId(), timeStamp, SnapBiConfig.getSnapBiPrivateKey()
        ));
        snapBiAccessTokenHeader.put("X-TIMESTAMP", timeStamp);
        snapBiAccessTokenHeader.put("debug-id", this.debugId != null ? this.debugId : "");

        if (!this.accessTokenHeader.isEmpty()) {
            snapBiAccessTokenHeader.putAll(this.accessTokenHeader);
        }

        return snapBiAccessTokenHeader;
    }

    public static String getSymmetricSignatureHmacSh512(String accessToken, Map<String, Object> requestBody, String method, String path, String clientSecret, String timeStamp) {
        try {
            // Convert requestBody Map to JSON string using JSONObject
            JSONObject jsonBody = new JSONObject(requestBody);
            String minifiedBody = jsonBody.toString();

            // Generate SHA-256 hash of the minified request body
            byte[] hashedBody = hashSha256(minifiedBody);
            String lowercaseHexHash = bytesToHex(hashedBody).toLowerCase();

            // Construct the payload
            String payload = String.format("%s:%s:%s:%s:%s", method.toUpperCase(), path, accessToken, lowercaseHexHash, timeStamp);

            // Generate HMAC using SHA512
            byte[] hmac = hmacSha512(payload, clientSecret);

            // Encode the result to Base64
            return Base64.getEncoder().encodeToString(hmac);

        } catch (Exception e) {
            throw new RuntimeException("Error generating symmetric signature", e);
        }
    }

    private static String minifyJson(String json) {
        // Minify JSON by removing whitespace
        return json.replaceAll("\\s+", "");
    }

    private static byte[] hashSha256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] hmacSha512(String payload, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        mac.init(secretKeySpec);
        return mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String getAsymmetricSignatureSha256WithRsa(String clientId, String timeStamp, String privateKey) throws Exception {
        String stringToSign = clientId + "|" + timeStamp;

        // Convert the private key string (PEM format) into a PrivateKey object
        PrivateKey privateKeyObject = convertPrivateKey(privateKey);

        // Create the signature using the private key
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKeyObject);
        signature.update(stringToSign.getBytes(StandardCharsets.UTF_8));

        // Sign and encode to Base64
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    // Helper method to convert PEM string to PrivateKey object
    private static PrivateKey convertPrivateKey(String privateKeyPem) throws Exception {
        try {
            // Remove the PEM headers and decode the Base64
            String privateKeyPEM = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", ""); // Remove whitespace

            // Decode the Base64 encoded string into bytes
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);

            // Generate PrivateKey object from bytes
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

            // Use the appropriate algorithm based on the key type
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new Exception("Failed to convert private key", e);
        }
    }

    private String setupCreatePaymentApiPath(String paymentMethod) {
        switch (paymentMethod) {
            case "va":
                return CREATE_VA;
            case "qris":
                return QRIS_PAYMENT;
            case "directDebit":
                return PAYMENT_HOST_TO_HOST;
            default:
                throw new UnsupportedOperationException("Payment method not implemented: " + paymentMethod);
        }
    }

    private String setupRefundApiPath(String paymentMethod) {
        switch (paymentMethod) {
            case "qris":
                return QRIS_REFUND;
            case "directDebit":
                return DEBIT_REFUND;
            default:
                throw new UnsupportedOperationException("Payment method not implemented: " + paymentMethod);
        }
    }

    private String setupCancelApiPath(String paymentMethod) {
        switch (paymentMethod) {
            case "va":
                return VA_CANCEL;
            case "qris":
                return QRIS_CANCEL;
            case "directDebit":
                return DEBIT_CANCEL;
            default:
                throw new UnsupportedOperationException("Payment method not implemented: " + paymentMethod);
        }
    }

    private String setupGetStatusApiPath(String paymentMethod) {
        switch (paymentMethod) {
            case "va":
                return VA_STATUS;
            case "qris":
                return QRIS_STATUS;
            case "directDebit":
                return DEBIT_STATUS;
            default:
                throw new UnsupportedOperationException("Payment method not implemented: " + paymentMethod);
        }
    }
}

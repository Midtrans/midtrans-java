package com.midtrans.snapbi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@Tag("junit5")
public class SnapBiWebhookSignatureTest {

    // RSA key pair generated once for all tests
    private static final KeyPair KEY_PAIR = generateKeyPair();

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String publicKeyToPem(PublicKey publicKey) {
        String base64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" + base64 + "\n-----END PUBLIC KEY-----";
    }

    /**
     * Produces the same signature that Midtrans would produce:
     * sign(SHA256withRSA, "POST:<urlPath>:<sha256hex(minifiedBody)>:<timestamp>")
     */
    private static String signPayload(String notificationPayload, String urlPath, String timeStamp) throws Exception {
        String minified = SnapBi.minifyJson(notificationPayload);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(minified.getBytes());
        String hexHash = bytesToHex(hash).toLowerCase();

        String rawString = "POST:" + urlPath + ":" + hexHash + ":" + timeStamp;

        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(KEY_PAIR.getPrivate());
        signer.update(rawString.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signer.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @AfterEach
    void cleanUp() {
        // Reset static config to avoid leaking into other tests
        SnapBiConfig.setSnapBiPublicKey(null);
    }

    @Test
    void verificationSucceedsWithSpacesInStringValues() throws Exception {
        String urlPath = "/webhook/snap-bi/va";
        String timeStamp = "2024-09-23T15:11:08+07:00";
        String payload = "{\n" +
                "  \"originalReferenceNo\" : \"A1202409231511081IDnMTwbGpYp\",\n" +
                "  \"virtualAccountNo\" : \"7082214536759543\",\n" +
                "  \"virtualAccountName\" : \"Midtrans Yudhi\",\n" +
                "  \"paymentRequestId\" : \"test yudhi3\",\n" +
                "  \"paidAmount\" : {\n" +
                "    \"value\" : \"15000.00\",\n" +
                "    \"currency\" : \"IDR\"\n" +
                "  }\n" +
                "}";

        String signature = signPayload(payload, urlPath, timeStamp);
        SnapBiConfig.setSnapBiPublicKey(publicKeyToPem(KEY_PAIR.getPublic()));

        Boolean result = SnapBi.notification()
                .withNotificationPayload(payload)
                .withNotificationUrlPath(urlPath)
                .withTimeStamp(timeStamp)
                .withSignature(signature)
                .isWebhookNotificationVerified();

        assertTrue(result, "Signature verification should succeed for payload with spaces in string values");
    }

    @Test
    void verificationFailsWithTamperedPayload() throws Exception {
        String urlPath = "/webhook/snap-bi/va";
        String timeStamp = "2024-09-23T15:11:08+07:00";
        String originalPayload = "{\"name\" : \"Midtrans Yudhi\"}";

        // Sign the original payload
        String signature = signPayload(originalPayload, urlPath, timeStamp);
        SnapBiConfig.setSnapBiPublicKey(publicKeyToPem(KEY_PAIR.getPublic()));

        // Verify with tampered payload
        String tamperedPayload = "{\"name\" : \"Midtrans Hacker\"}";

        Boolean result = SnapBi.notification()
                .withNotificationPayload(tamperedPayload)
                .withNotificationUrlPath(urlPath)
                .withTimeStamp(timeStamp)
                .withSignature(signature)
                .isWebhookNotificationVerified();

        assertFalse(result, "Signature verification should fail for tampered payload");
    }

    @Test
    void verificationSucceedsWithAlreadyMinifiedPayload() throws Exception {
        String urlPath = "/api/notification";
        String timeStamp = "2024-01-01T00:00:00Z";
        // Already minified - no extra whitespace
        String payload = "{\"code\":\"ABC\",\"name\":\"John Doe\"}";

        String signature = signPayload(payload, urlPath, timeStamp);
        SnapBiConfig.setSnapBiPublicKey(publicKeyToPem(KEY_PAIR.getPublic()));

        Boolean result = SnapBi.notification()
                .withNotificationPayload(payload)
                .withNotificationUrlPath(urlPath)
                .withTimeStamp(timeStamp)
                .withSignature(signature)
                .isWebhookNotificationVerified();

        assertTrue(result, "Signature verification should succeed for already-minified payload with spaces in values");
    }

    @Test
    void verificationThrowsWhenPublicKeyNotSet() {
        SnapBiConfig.setSnapBiPublicKey(null);

        assertThrows(IllegalStateException.class, () ->
                SnapBi.notification()
                        .withNotificationPayload("{}")
                        .withNotificationUrlPath("/test")
                        .withTimeStamp("2024-01-01T00:00:00Z")
                        .withSignature("dummysig")
                        .isWebhookNotificationVerified()
        );
    }
}

package com.midtrans.snapbi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies SNAP-BI webhook signature verification across all payment methods.
 * <p>
 * Each test loads a realistic JSON fixture, signs it with a test RSA key pair
 * (simulating what Midtrans does server-side), then passes it through the
 * public {@code SnapBi.notification().isWebhookNotificationVerified()} API.
 * <p>
 * Fixtures live in {@code src/test/resources/snapbi/webhook/} and are
 * pretty-printed with spaces in string values to exercise the minifier.
 */
@Tag("junit5")
public class SnapBiWebhookSignatureTest {

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
     * Loads a JSON fixture from the classpath.
     */
    private static String loadFixture(String resourcePath) {
        try (InputStream is = SnapBiWebhookSignatureTest.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Fixture not found: " + resourcePath);
            }
            try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load fixture: " + resourcePath, e);
        }
    }

    /**
     * Minifies JSON the same way Midtrans would: strips whitespace outside of
     * quoted strings while preserving whitespace inside them.
     * <p>
     * This is intentionally a standalone copy — it simulates the server-side
     * behaviour so we can produce valid signatures for test payloads without
     * depending on the private {@code SnapBi.minifyJson()} method.
     */
    private static String testMinifyJson(String json) {
        if (json == null || json.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(json.length());
        boolean inString = false;
        boolean escaped = false;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escaped) {
                sb.append(c);
                escaped = false;
                continue;
            }
            if (c == '\\' && inString) {
                sb.append(c);
                escaped = true;
                continue;
            }
            if (c == '"') {
                inString = !inString;
                sb.append(c);
                continue;
            }
            if (!inString && Character.isWhitespace(c)) {
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Produces the same signature that Midtrans would produce:
     * sign(SHA256withRSA, "POST:&lt;urlPath&gt;:&lt;sha256hex(minifiedBody)&gt;:&lt;timestamp&gt;")
     */
    private static String signPayload(String notificationPayload, String urlPath, String timeStamp) throws Exception {
        String minified = testMinifyJson(notificationPayload);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(minified.getBytes(StandardCharsets.UTF_8));
        String hexHash = bytesToHex(hash);

        String rawString = "POST:" + urlPath + ":" + hexHash + ":" + timeStamp;

        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(KEY_PAIR.getPrivate());
        signer.update(rawString.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signer.sign());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Verifies a fixture payload through the public API and asserts success.
     */
    private void assertFixtureVerifies(String fixturePath, String urlPath, String timeStamp) throws Exception {
        String payload = loadFixture(fixturePath);
        String signature = signPayload(payload, urlPath, timeStamp);
        SnapBiConfig.setSnapBiPublicKey(publicKeyToPem(KEY_PAIR.getPublic()));

        Boolean result = SnapBi.notification()
                .withNotificationPayload(payload)
                .withNotificationUrlPath(urlPath)
                .withTimeStamp(timeStamp)
                .withSignature(signature)
                .isWebhookNotificationVerified();

        assertTrue(result, "Signature verification should succeed for fixture: " + fixturePath);
    }

    @AfterEach
    void cleanUp() {
        SnapBiConfig.setSnapBiPublicKey(null);
    }

    // ---- VA fixtures ----

    @Test
    void vaSettlement_spacesInStringValuesPreserved() throws Exception {
        // The original bug: virtualAccountName "Midtrans Yudhi" was mangled
        assertFixtureVerifies(
                "snapbi/webhook/va-settlement.json",
                "/webhook/snap-bi/va",
                "2024-09-23T15:15:30+07:00");
    }

    @Test
    void vaPending_emptyObjectPreserved() throws Exception {
        // customField: {} exercises empty-object handling
        assertFixtureVerifies(
                "snapbi/webhook/va-pending.json",
                "/webhook/snap-bi/va",
                "2024-09-23T15:11:08+07:00");
    }

    // ---- QRIS fixtures ----

    @Test
    void qrisPending_spacesInMerchantName() throws Exception {
        assertFixtureVerifies(
                "snapbi/webhook/qris-pending.json",
                "/webhook/snap-bi/qris",
                "2024-09-24T10:00:01+07:00");
    }

    @Test
    void qrisSuccess_escapedQuotesInStringValues() throws Exception {
        // buyerName "John \"JD\" Doe" exercises escaped-quote handling
        assertFixtureVerifies(
                "snapbi/webhook/qris-success.json",
                "/webhook/snap-bi/qris",
                "2024-09-24T10:05:30+07:00");
    }

    // ---- Direct Debit fixtures ----

    @Test
    void directDebitPending_nestedArraysPreserved() throws Exception {
        // lineItems array with objects exercises nested structure handling
        assertFixtureVerifies(
                "snapbi/webhook/direct-debit-pending.json",
                "/webhook/snap-bi/direct-debit",
                "2024-09-25T14:00:01+07:00");
    }

    @Test
    void directDebitSuccess_escapedQuotesInNestedValues() throws Exception {
        // paymentDescription contains escaped quotes inside a nested object
        assertFixtureVerifies(
                "snapbi/webhook/direct-debit-success.json",
                "/webhook/snap-bi/direct-debit",
                "2024-09-25T14:05:00+07:00");
    }

    // ---- Cross-cutting edge cases ----

    @Test
    void alreadyMinifiedVaPayloadVerifies() throws Exception {
        assertMinifiedFixtureVerifies("snapbi/webhook/va-settlement.json");
    }

    @Test
    void alreadyMinifiedQrisPayloadVerifies() throws Exception {
        assertMinifiedFixtureVerifies("snapbi/webhook/qris-success.json");
    }

    @Test
    void alreadyMinifiedDirectDebitPayloadVerifies() throws Exception {
        assertMinifiedFixtureVerifies("snapbi/webhook/direct-debit-success.json");
    }

    /**
     * Pre-minifies a fixture to simulate a payload that arrives without
     * insignificant whitespace, then verifies the signature still matches.
     */
    private void assertMinifiedFixtureVerifies(String fixturePath) throws Exception {
        String prettyPayload = loadFixture(fixturePath);
        String minifiedPayload = testMinifyJson(prettyPayload);

        String urlPath = "/webhook/snap-bi/minified-test";
        String timeStamp = "2024-01-01T00:00:00Z";
        String signature = signPayload(minifiedPayload, urlPath, timeStamp);
        SnapBiConfig.setSnapBiPublicKey(publicKeyToPem(KEY_PAIR.getPublic()));

        Boolean result = SnapBi.notification()
                .withNotificationPayload(minifiedPayload)
                .withNotificationUrlPath(urlPath)
                .withTimeStamp(timeStamp)
                .withSignature(signature)
                .isWebhookNotificationVerified();

        assertTrue(result, "Verification should succeed for pre-minified fixture: " + fixturePath);
    }

    @Test
    void verificationFailsWithTamperedPayload() throws Exception {
        String payload = loadFixture("snapbi/webhook/va-settlement.json");
        String urlPath = "/webhook/snap-bi/va";
        String timeStamp = "2024-09-23T15:15:30+07:00";

        // Sign the original payload
        String signature = signPayload(payload, urlPath, timeStamp);
        SnapBiConfig.setSnapBiPublicKey(publicKeyToPem(KEY_PAIR.getPublic()));

        // Tamper with the payload
        String tamperedPayload = payload.replace("Midtrans Yudhi", "Midtrans Hacker");

        Boolean result = SnapBi.notification()
                .withNotificationPayload(tamperedPayload)
                .withNotificationUrlPath(urlPath)
                .withTimeStamp(timeStamp)
                .withSignature(signature)
                .isWebhookNotificationVerified();

        assertFalse(result, "Signature verification should fail for tampered payload");
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

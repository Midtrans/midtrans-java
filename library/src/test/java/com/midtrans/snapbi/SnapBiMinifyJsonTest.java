package com.midtrans.snapbi;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("junit5")
public class SnapBiMinifyJsonTest {

    @Test
    void spacesInStringValuesArePreserved() {
        String input = "{\"name\" : \"Midtrans Yudhi\", \"amount\" : \"10000\"}";
        String expected = "{\"name\":\"Midtrans Yudhi\",\"amount\":\"10000\"}";
        assertEquals(expected, SnapBi.minifyJson(input));
    }

    @Test
    void noSpacesInStringValues() {
        String input = "{ \"code\" : \"ABC123\" , \"status\" : \"active\" }";
        String expected = "{\"code\":\"ABC123\",\"status\":\"active\"}";
        assertEquals(expected, SnapBi.minifyJson(input));
    }

    @Test
    void escapedQuotesInStringValues() {
        String input = "{\"msg\" : \"say \\\"hello world\\\"\"}";
        String expected = "{\"msg\":\"say \\\"hello world\\\"\"}";
        assertEquals(expected, SnapBi.minifyJson(input));
    }

    @Test
    void nestedObjectsAndArrays() {
        String input = "{ \"outer\" : { \"inner\" : \"hello world\" } , \"list\" : [ \"a b\" , \"c d\" ] }";
        String expected = "{\"outer\":{\"inner\":\"hello world\"},\"list\":[\"a b\",\"c d\"]}";
        assertEquals(expected, SnapBi.minifyJson(input));
    }

    @Test
    void alreadyMinifiedJsonUnchanged() {
        String input = "{\"name\":\"Midtrans Yudhi\",\"amount\":\"10000\"}";
        assertEquals(input, SnapBi.minifyJson(input));
    }

    @Test
    void keyOrderingPreserved() {
        String input = "{ \"z_key\" : 1 , \"a_key\" : 2 , \"m_key\" : 3 }";
        String expected = "{\"z_key\":1,\"a_key\":2,\"m_key\":3}";
        assertEquals(expected, SnapBi.minifyJson(input));
    }

    @Test
    void tabsAndNewlinesOutsideStringsRemoved() {
        String input = "{\n\t\"name\"\t:\t\"John Doe\"\n}";
        String expected = "{\"name\":\"John Doe\"}";
        assertEquals(expected, SnapBi.minifyJson(input));
    }

    @Test
    void emptyObjectAndArray() {
        assertEquals("{}", SnapBi.minifyJson("{ }"));
        assertEquals("[]", SnapBi.minifyJson("[ ]"));
    }

    @Test
    void nullInputReturnsEmptyString() {
        assertEquals("", SnapBi.minifyJson(null));
    }

    @Test
    void emptyInputReturnsEmptyString() {
        assertEquals("", SnapBi.minifyJson(""));
    }

    @Test
    void realisticSnapBiVaNotificationPayload() {
        String input = "{\n" +
                "  \"originalReferenceNo\" : \"A1202409231511081IDnMTwbGpYp\",\n" +
                "  \"virtualAccountNo\" : \"7082214536759543\",\n" +
                "  \"virtualAccountName\" : \"Midtrans Yudhi\",\n" +
                "  \"paymentRequestId\" : \"test yudhi3\",\n" +
                "  \"paidAmount\" : {\n" +
                "    \"value\" : \"15000.00\",\n" +
                "    \"currency\" : \"IDR\"\n" +
                "  },\n" +
                "  \"customField\" : { }\n" +
                "}";
        String result = SnapBi.minifyJson(input);
        // Spaces inside string values must be preserved
        assertTrue(result.contains("\"Midtrans Yudhi\""), "Space in virtualAccountName must be preserved");
        assertTrue(result.contains("\"test yudhi3\""), "Space in paymentRequestId must be preserved");
        // No insignificant whitespace should remain
        assertFalse(result.contains(" :"), "Whitespace around colons should be removed");
        assertFalse(result.contains(": "), "Whitespace around colons should be removed (except inside strings)");
        assertFalse(result.contains("\n"), "Newlines should be removed");
    }
}

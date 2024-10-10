package com.midtrans.snapbi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SnapBiWebhookServer {
    public static void main(String[] args) throws IOException {
        int port = 3000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/webhook", new WebhookHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port " + port);
    }

    static class WebhookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read the request body
                InputStream requestBody = exchange.getRequestBody();
                String body = new Scanner(requestBody, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
                Map<String, List<String>> headers = exchange.getRequestHeaders();

                // Get specific header values (x-signature and x-timestamp)
                String signature = headers.getOrDefault("X-Signature", java.util.Collections.emptyList()).stream().findFirst().orElse(null);
                String timestamp =  headers.getOrDefault("X-Timestamp", java.util.Collections.emptyList()).stream().findFirst().orElse(null);

                String publicKey = "-----BEGIN PUBLIC KEY-----"
                        +"ACBDefghijkklmn/fboOoctcthr8aJ5AOEpCFLrsCSgAtmtcHxBHq9miZyHFf4juNBpvvRrVlCLzyhNOkjKDNj9PO/MZabcdefGHIJKLMN"
                        +"-----END PUBLIC KEY-----";

                SnapBiConfig.setSnapBiPublicKey(publicKey);
                // get the url path that comes after `/webhook`
                String requestURI = exchange.getRequestURI().toString();
                String basePath = "/webhook";
                String remainingPath = requestURI.substring(basePath.length());

                Boolean isVerified = false;
                try {
                    isVerified =
                            SnapBi.notification()
                            .withNotificationPayload(body)
                            .withSignature(signature)
                            .withTimeStamp(timestamp)
                            .withNotificationUrlPath(remainingPath)
                            .isWebhookNotificationVerified();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                String response = "Webhook received successfully, its verified = " + isVerified;
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                // Method not allowed
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}

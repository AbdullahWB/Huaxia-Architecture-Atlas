package com.huaxia.atlas.ai.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@Component
public class GeminiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${app.gemini.api-key:}")
    private String apiKey;

    @Value("${app.gemini.model:gemini-2.0-flash}")
    private String model;

    public GeminiClient(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = objectMapper;
    }

    public String generateText(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            return "Gemini API key is not configured. Set app.gemini.api-key in application.yml (or via env).";
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model + ":generateContent";

            // Gemini request body: contents -> parts -> text
            Map<String, Object> payload = Map.of(
                    "contents", new Object[] {
                            Map.of("parts", new Object[] {
                                    Map.of("text", prompt)
                            })
                    });

            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() / 100 != 2) {
                return "Gemini error (" + response.statusCode() + "): " + safeTrim(response.body(), 500);
            }

            return extractText(response.body());

        } catch (Exception e) {
            return "Gemini request failed: " + e.getMessage();
        }
    }

    private String extractText(String json) throws Exception {
        JsonNode root = objectMapper.readTree(json);

        // candidates[0].content.parts[0].text
        JsonNode candidates = root.path("candidates");
        if (!candidates.isArray() || candidates.isEmpty())
            return "No response candidates returned by Gemini.";

        JsonNode textNode = candidates.get(0)
                .path("content")
                .path("parts");

        if (!textNode.isArray() || textNode.isEmpty())
            return "Gemini response was missing text parts.";

        String text = textNode.get(0).path("text").asText("");
        return text.isBlank() ? "Gemini returned an empty response." : text;
    }

    private String safeTrim(String s, int max) {
        if (s == null)
            return "";
        if (s.length() <= max)
            return s;
        return s.substring(0, max) + "...";
    }
}

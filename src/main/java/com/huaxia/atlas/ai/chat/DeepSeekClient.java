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
import java.util.List;
import java.util.Map;

@Component
public class DeepSeekClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${app.deepseek.api-key:}")
    private String apiKey;

    @Value("${app.deepseek.model:deepseek-chat}")
    private String model;

    @Value("${app.deepseek.base-url:https://api.deepseek.com/v1}")
    private String baseUrl;

    public DeepSeekClient(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = objectMapper;
    }

    public String generateText(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            return "DeepSeek API key is not configured. Set app.deepseek.api-key in application.yml (or via env).";
        }

        try {
            String url = baseUrl + "/chat/completions";

            Map<String, Object> payload = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    )
            );

            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() / 100 != 2) {
                return "DeepSeek error (" + response.statusCode() + "): " + safeTrim(response.body(), 500);
            }

            return extractText(response.body());
        } catch (Exception e) {
            return "DeepSeek request failed: " + e.getMessage();
        }
    }

    private String extractText(String json) throws Exception {
        JsonNode root = objectMapper.readTree(json);
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            return "No response choices returned by DeepSeek.";
        }

        String text = choices.get(0)
                .path("message")
                .path("content")
                .asText("");

        return text == null || text.isBlank()
                ? "DeepSeek returned an empty response."
                : text.trim();
    }

    private String safeTrim(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...";
    }
}

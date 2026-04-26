package com.agentic.ai.transactionagent.rest.Service.llm;

import com.agentic.ai.transactionagent.rest.model.AgentDecision;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LlmService {

    @Value("${groq.api-key}")
    private String apiKey ;

    @Value("${groq.url}")
    private String url ;

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AgentDecision analyze(Transaction tx) {

        String prompt = buildPrompt(tx);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "You are a strict fraud detection AI. Respond ONLY in valid JSON."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );

        String response = webClientBuilder.build()
                .post()
                .uri(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parseResponse(response);
    }

    private String buildPrompt(Transaction tx) {
        return """
        Analyze the following transaction and return ONLY JSON:

        {
          "decision": "APPROVED | FLAGGED | REJECTED",
          "reason": "short explanation",
          "riskScore": number between 0 and 1
        }

        Transaction Details:
        amount: %s
        location: %s
        deviceId: %s

        Rules:
        - High amount (>50000) = higher risk
        - Unknown location = suspicious
        - Frequent unusual patterns = risky

        IMPORTANT:
        - Output ONLY valid JSON
        - No extra text
        """.formatted(
                tx.getAmount(),
                tx.getLocation(),
                tx.getDeviceId()
        );
    }

    private AgentDecision parseResponse(String response) {
        try {
            // Step 1: Extract content field from Groq response
            JsonNode root = objectMapper.readTree(response);
            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // Step 2: Parse actual JSON returned by model
            JsonNode json = objectMapper.readTree(content);

            return AgentDecision.builder()
                    .decision(json.get("decision").asText())
                    .reason(json.get("reason").asText())
                    .riskScore(json.get("riskScore").asDouble())
                    .build();

        } catch (Exception e) {
            // fallback (important for stability)
            return AgentDecision.builder()
                    .decision("FLAGGED")
                    .reason("Parsing failed, defaulting to safe side")
                    .riskScore(0.7)
                    .build();
        }
    }
}
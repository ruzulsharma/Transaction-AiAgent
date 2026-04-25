package com.agentic.ai.transactionagent.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private String id;

    private String userId;
    private Double amount;
    private String location;
    private String deviceId;

    private String status; // NEW, PROCESSED, FRAUD

    private String agentDecision; // AI output
    private String groqReasoning;

    private LocalDateTime createdAt;
}

package com.agentic.ai.transactionagent.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TransactionResponse {

    private String transactionId;
    private String userId;
    private Double amount;

    private String decision;   // APPROVED / FLAGGED / REJECTED
    private double riskScore;

    private List<String> ruleReasons;
    private String aiReason;

    private String status;     // PROCESSED / FRAUD / NEW
    private String location;
    private String deviceId;
}

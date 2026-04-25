package com.agentic.ai.transactionagent.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentDecision {

    private String decision;
    // APPROVED / REJECTED / FLAGGED

    private String reason;

    private Double riskScore;
}

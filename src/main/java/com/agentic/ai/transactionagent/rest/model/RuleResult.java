package com.agentic.ai.transactionagent.rest.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RuleResult {
    private double riskScore;
    private List<String> reasons;
}

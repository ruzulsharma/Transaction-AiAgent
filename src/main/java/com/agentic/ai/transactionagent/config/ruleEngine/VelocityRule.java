package com.agentic.ai.transactionagent.config.ruleEngine;

import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class VelocityRule implements RiskRule {

    @Override
    public RuleResult apply(Transaction tx, List<Transaction> history) {

        double risk = 0.0;
        List<String> reasons = new ArrayList<>();

        if (history.size() >= 5) {

            long highTx = history.stream()
                    .limit(5)
                    .filter(t -> t.getAmount() != null && t.getAmount() > 50000)
                    .count();

            if (highTx >= 3) {
                risk += 0.25;
                reasons.add("HIGH_FREQUENCY_HIGH_VALUE_TX");
            }
        }

        return RuleResult.builder()
                .riskScore(risk)
                .reasons(reasons)
                .build();
    }
}

package com.agentic.ai.transactionagent.config.ruleEngine;

import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FrequencyRule implements RiskRule{
    @Override
    public RuleResult apply(Transaction tx, List<Transaction> history) {

        double risk = 0.0;
        List<String> reasons = new ArrayList<>();

        if (history.size() >= 5) {

            // Last 5 transactions
            List<Transaction> recent = history.stream()
                    .limit(5)
                    .toList();

            long highValueCount = recent.stream()
                    .filter(t -> t.getAmount() != null && t.getAmount() > 50000)
                    .count();

            if (highValueCount >= 3) {
                risk += 0.25;
                reasons.add("HIGH_FREQUENCY_HIGH_VALUE_TX");
            }

            // Optional: too many transactions regardless of amount
            if (recent.size() >= 5) {
                risk += 0.1;
                reasons.add("HIGH_TRANSACTION_VELOCITY");
            }
        }

        return RuleResult.builder()
                .riskScore(risk)
                .reasons(reasons)
                .build();
    }
}

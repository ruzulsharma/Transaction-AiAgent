package com.agentic.ai.transactionagent.config.ruleEngine;

import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceRule implements RiskRule{

    @Override
    public RuleResult apply(Transaction tx, List<Transaction> history) {

        double risk = 0.0;
        List<String> reasons = new ArrayList<>();

        if (tx.getDeviceId() == null || tx.getDeviceId().isBlank()) {
            risk += 0.2;
            reasons.add("MISSING_DEVICE");
        } else {
            boolean known = history.stream()
                    .anyMatch(h -> tx.getDeviceId().equals(h.getDeviceId()));

            if (!known && !history.isEmpty()) {
                risk += 0.2;
                reasons.add("NEW_DEVICE");
            }
        }

        return RuleResult.builder()
                .riskScore(risk)
                .reasons(reasons)
                .build();
    }
}

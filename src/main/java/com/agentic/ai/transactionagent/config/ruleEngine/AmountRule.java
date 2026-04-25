package com.agentic.ai.transactionagent.config.ruleEngine;

import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AmountRule implements RiskRule {
    @Override
    public RuleResult apply(Transaction tx, List<Transaction> history) {

        double risk = 0.0;
        List<String> reasons = new ArrayList<>();

        if (tx.getAmount() != null) {
            if (tx.getAmount() > 100000) {
                risk += 0.35;
                reasons.add("HIGH_AMOUNT");
            } else if (tx.getAmount() > 50000) {
                risk += 0.15;
                reasons.add("MEDIUM_AMOUNT");
            }
        }

        return RuleResult.builder()
                .riskScore(risk)
                .reasons(reasons)
                .build();
    }

}

package com.agentic.ai.transactionagent.config.ruleEngine;

import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeRule implements RiskRule{

    @Override
    public RuleResult apply(Transaction tx, List<Transaction> history) {

        double risk = 0.0;
        List<String> reasons = new ArrayList<>();

        LocalDateTime time = tx.getCreatedAt() != null
                ? tx.getCreatedAt()
                : LocalDateTime.now();

        int hour = time.getHour();

        // Night hours (1 AM - 5 AM)
        if (hour >= 1 && hour <= 5) {
            risk += 0.15;
            reasons.add("ODD_HOUR_TRANSACTION");
        }

        // Optional: early morning risk (5–7 AM)
        else if (hour > 5 && hour <= 7) {
            risk += 0.05;
            reasons.add("EARLY_MORNING_ACTIVITY");
        }

        return RuleResult.builder()
                .riskScore(risk)
                .reasons(reasons)
                .build();
    }
}

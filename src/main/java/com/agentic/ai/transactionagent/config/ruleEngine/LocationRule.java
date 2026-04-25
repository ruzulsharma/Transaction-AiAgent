package com.agentic.ai.transactionagent.config.ruleEngine;

import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocationRule implements RiskRule{

    @Override
    public RuleResult apply(Transaction tx, List<Transaction> history) {

        double risk = 0.0;
        List<String> reasons = new ArrayList<>();

        // Unknown location
        if (tx.getLocation() == null || tx.getLocation().isBlank()) {
            risk += 0.25;
            reasons.add("UNKNOWN_LOCATION");
        }

        // Location change
        if (!history.isEmpty()) {
            Transaction lastTx = history.get(0);

            if (lastTx.getLocation() != null &&
                    tx.getLocation() != null &&
                    !lastTx.getLocation().equalsIgnoreCase(tx.getLocation())) {

                risk += 0.2;
                reasons.add("LOCATION_CHANGE");
            }
        }

        return RuleResult.builder()
                .riskScore(risk)
                .reasons(reasons)
                .build();
    }

}

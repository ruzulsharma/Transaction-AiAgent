package com.agentic.ai.transactionagent.rest.Service.rule;

import com.agentic.ai.transactionagent.Repository.TransactionRepository;
import com.agentic.ai.transactionagent.config.ruleEngine.RiskRule;
import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleEngineService {
//    @Autowired
//    private TransactionRepository repository;
//
//    public AgentDecision evaluate(Transaction tx) {
//
//        double riskScore = 0.0;
//        List<String> reasons = new ArrayList<>();
//
//        // Fetch user history once (important)
//        List<Transaction> history = repository
//                .findByUserIdOrderByCreatedAtDesc(tx.getUserId());
//
//        // 1. Amount-based risk (relative + absolute)
//        if (tx.getAmount() != null) {
//            if (tx.getAmount() > 100000) {
//                riskScore += 0.35;
//                reasons.add("HIGH_AMOUNT");
//            } else if (tx.getAmount() > 50000) {
//                riskScore += 0.15;
//                reasons.add("MEDIUM_AMOUNT");
//            }
//        }
//
//        // 2. Location risk
//        if (tx.getLocation() == null || tx.getLocation().isBlank()) {
//            riskScore += 0.25;
//            reasons.add("UNKNOWN_LOCATION");
//        }
//
//        // 3. Device anomaly
//        if (tx.getDeviceId() == null || tx.getDeviceId().isBlank()) {
//            riskScore += 0.2;
//            reasons.add("MISSING_DEVICE");
//        } else {
//            boolean knownDevice = history.stream()
//                    .anyMatch(h -> tx.getDeviceId().equals(h.getDeviceId()));
//
//            if (!knownDevice && !history.isEmpty()) {
//                riskScore += 0.2;
//                reasons.add("NEW_DEVICE");
//            }
//        }
//
//        // 4. Frequency anomaly
//        if (history.size() >= 5) {
//            long highTxCount = history.stream()
//                    .limit(5)
//                    .filter(t -> t.getAmount() != null && t.getAmount() > 50000)
//                    .count();
//
//            if (highTxCount >= 3) {
//                riskScore += 0.25;
//                reasons.add("HIGH_FREQUENCY_HIGH_VALUE_TX");
//            }
//        }
//
//        // 5. Geo anomaly (simple version)
//        if (!history.isEmpty()) {
//            Transaction lastTx = history.get(0);
//
//            if (lastTx.getLocation() != null &&
//                    tx.getLocation() != null &&
//                    !lastTx.getLocation().equalsIgnoreCase(tx.getLocation())) {
//
//                riskScore += 0.2;
//                reasons.add("LOCATION_CHANGE");
//            }
//        }
//
//        // 6. Time-based risk
//        LocalDateTime time = tx.getCreatedAt() != null
//                ? tx.getCreatedAt()
//                : LocalDateTime.now();
//
//        int hour = time.getHour();
//
//        if (hour >= 1 && hour <= 5) {
//            riskScore += 0.15;
//            reasons.add("ODD_HOUR_TRANSACTION");
//        }
//
//        // Normalize
//        riskScore = Math.min(riskScore, 1.0);
//
//        // Decision logic (tuned)
//        String decision;
//        if (riskScore >= 0.7) {
//            decision = "REJECTED";
//        } else if (riskScore >= 0.4) {
//            decision = "FLAGGED";
//        } else {
//            decision = "APPROVED";
//        }
//
//        // Final reason string
//        String finalReason = String.join(" | ", reasons);
//
//        return AgentDecision.builder()
//                .decision(decision)
//                .reason(finalReason.isEmpty() ? "LOW_RISK" : finalReason)
//                .riskScore(riskScore)
//                .build();
//    }


    private final List<RiskRule> rules;
    private final TransactionRepository repository;

    public RuleResult evaluate(Transaction tx) {

        List<Transaction> history =
                repository.findByUserIdOrderByCreatedAtDesc(tx.getUserId());

        double totalRisk = 0.0;
        List<String> allReasons = new ArrayList<>();

        for (RiskRule rule : rules) {
            RuleResult result = rule.apply(tx, history);

            totalRisk += result.getRiskScore();
            allReasons.addAll(result.getReasons());
        }

        totalRisk = Math.min(totalRisk, 1.0);

        return RuleResult.builder()
                .riskScore(totalRisk)
                .reasons(allReasons)
                .build();
    }

}

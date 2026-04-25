package com.agentic.ai.transactionagent.rest.Service.agent;

import com.agentic.ai.transactionagent.rest.dto.TransactionResponse;
import com.agentic.ai.transactionagent.rest.model.AgentDecision;
import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import com.agentic.ai.transactionagent.Repository.TransactionRepository;
import com.agentic.ai.transactionagent.rest.Service.llm.LlmService;
import com.agentic.ai.transactionagent.rest.Service.rule.RuleEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final RuleEngineService ruleEngine;
    private final LlmService llmService;
    private final TransactionRepository repository;

//    public Transaction process(Transaction tx) {
//
//        // Rule-based decision
//       // AgentDecision ruleDecision = ruleEngine.evaluate(tx);
//        RuleResult ruleResult = ruleEngine.evaluate(tx);
//
//        AgentDecision ruleDecision = AgentDecision.builder()
//                .decision(mapDecision(ruleResult.getRiskScore()))
//                .reason(String.join(" | ", ruleResult.getReasons()))
//                .riskScore(ruleResult.getRiskScore())
//                .build();
//
//        AgentDecision finalDecision;
//
//        // If very high risk → skip AI (fast path)
//        if (ruleDecision.getRiskScore() >= 0.7) {
//            finalDecision = ruleDecision;
//
//        } else {
//            // Using AI for nuanced decision
//            AgentDecision aiDecision = llmService.analyze(tx);
//
//            // Combine both (weighted)
//            double finalRisk = (ruleDecision.getRiskScore() * 0.6)
//                    + (aiDecision.getRiskScore() * 0.4);
//
//            String decision;
//            if (finalRisk >= 0.7) decision = "REJECTED";
//            else if (finalRisk >= 0.4) decision = "FLAGGED";
//            else decision = "APPROVED";
//
//            finalDecision = AgentDecision.builder()
//                    .decision(decision)
//                    .reason(ruleDecision.getReason() + " | AI: " + aiDecision.getReason())
//                    .riskScore(finalRisk)
//                    .build();
//        }
//
//        tx.setAgentDecision(finalDecision.getDecision());
//        tx.setStatus(mapStatus(finalDecision.getDecision()));
//        tx.setGroqResoning(finalDecision.getReason());
//
//        return repository.save(tx);
//    }
    public TransactionResponse process(Transaction tx) {

        //  Rule Engine
        RuleResult ruleResult = ruleEngine.evaluate(tx);

        AgentDecision ruleDecision = AgentDecision.builder()
                .decision(mapDecision(ruleResult.getRiskScore()))
                .reason(String.join(" | ", ruleResult.getReasons()))
                .riskScore(ruleResult.getRiskScore())
                .build();

        AgentDecision finalDecision;

        //  Fast path (skip AI)
        if (ruleDecision.getRiskScore() >= 0.7) {

            finalDecision = ruleDecision;

        } else {

            // S AI Decision
            AgentDecision aiDecision = llmService.analyze(tx);

            //THIS is the correct place for hybrid scoring
            double finalRisk = (ruleDecision.getRiskScore() * 0.6)
                    + (aiDecision.getRiskScore() * 0.4);

            String decision = mapDecision(finalRisk);

            finalDecision = AgentDecision.builder()
                    .decision(decision)
                    .reason(ruleDecision.getReason() + " | AI: " + aiDecision.getReason())
                    .riskScore(finalRisk)
                    .build();
        }

        //  Persist
        tx.setAgentDecision(finalDecision.getDecision());
        tx.setStatus(mapStatus(finalDecision.getDecision()));
        tx.setGroqReasoning(finalDecision.getReason());

        return mapToResponse(tx, finalDecision);
    }

    private TransactionResponse mapToResponse(Transaction tx, AgentDecision finalDecision) {

        // Split rule + AI reason (since you concatenated earlier)
        String[] parts = finalDecision.getReason().split("\\| AI:");

        String rulePart = parts[0].trim();
        String aiPart = parts.length > 1 ? parts[1].trim() : "N/A";

        List<String> ruleReasons = List.of(rulePart.split("\\|"));

        return TransactionResponse.builder()
                .transactionId(tx.getId())
                .userId(tx.getUserId())
                .amount(tx.getAmount())
                .decision(finalDecision.getDecision())
                .riskScore(finalDecision.getRiskScore())
                .ruleReasons(ruleReasons)
                .aiReason(aiPart)
                .status(tx.getStatus())
                .location(tx.getLocation())
                .deviceId(tx.getDeviceId())
                .build();
    }

    private String mapStatus(String decision) {
        switch (decision) {
            case "APPROVED": return "PROCESSED";
            case "FLAGGED": return "FRAUD";
            default: return "NEW";
        }
    }

    private String mapDecision(double risk) {
        if (risk >= 0.7) return "REJECTED";
        else if (risk >= 0.4) return "FLAGGED";
        else return "APPROVED";
    }




}

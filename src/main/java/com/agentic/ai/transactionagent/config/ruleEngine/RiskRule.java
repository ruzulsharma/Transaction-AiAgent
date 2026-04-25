package com.agentic.ai.transactionagent.config.ruleEngine;

import com.agentic.ai.transactionagent.rest.model.RuleResult;
import com.agentic.ai.transactionagent.rest.model.Transaction;

import java.util.List;

public interface RiskRule {
    RuleResult apply(Transaction tx, List<Transaction> history);
}

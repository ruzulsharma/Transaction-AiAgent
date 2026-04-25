package com.agentic.ai.transactionagent.kafka;

import com.agentic.ai.transactionagent.rest.Service.agent.TransactionService;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

//    private final TransactionService agentService;
//    private final TransactionProducer producer;
//
//    @KafkaListener(topics = "transaction-received", groupId = "transaction-group")
//    public void consume(Transaction tx) {
//
//        // Process using your AI agent
//        Transaction processed = agentService.process(tx);
//
//        // Publish result
//        producer.sendProcessed(processed);
//
//        if ("FRAUD".equals(processed.getStatus())) {
//            producer.sendFraud(processed);
//        }
//    }
}
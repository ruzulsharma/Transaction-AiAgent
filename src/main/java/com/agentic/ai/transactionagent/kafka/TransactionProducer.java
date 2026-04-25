package com.agentic.ai.transactionagent.kafka;

import com.agentic.ai.transactionagent.rest.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public void sendTransaction(Transaction tx) {
        kafkaTemplate.send("transaction-received", tx);
    }

    public void sendProcessed(Transaction tx) {
        kafkaTemplate.send("transaction-processed", tx);
    }

    public void sendFraud(Transaction tx) {
        kafkaTemplate.send("fraud-detected", tx);
    }
}

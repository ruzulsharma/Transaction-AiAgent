package com.agentic.ai.transactionagent.rest.Controllers;

import com.agentic.ai.transactionagent.kafka.TransactionProducer;
import com.agentic.ai.transactionagent.rest.dto.TransactionResponse;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import com.agentic.ai.transactionagent.rest.Service.agent.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("v1/api/transactions")
@RequiredArgsConstructor
public class TransactionController { @Autowired
    private  final TransactionService agentService;
    private final TransactionProducer producer;

    @Value("${app.kafka-enabled}")
    private boolean kafkaEnabled;


    @PostMapping("/process")
    public ResponseEntity<?> create(@RequestBody Transaction tx) {

        tx.setCreatedAt(LocalDateTime.now());
        tx.setStatus("RECEIVED");

        if (kafkaEnabled) {
            producer.sendTransaction(tx);
            return ResponseEntity.ok("Transaction queued (Kafka)");
        } else {
            TransactionResponse processed = agentService.process(tx);
            return ResponseEntity.ok(processed);
        }
    }
}

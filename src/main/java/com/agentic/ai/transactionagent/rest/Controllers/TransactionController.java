package com.agentic.ai.transactionagent.rest.Controllers;

import com.agentic.ai.transactionagent.kafka.TransactionProducer;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import com.agentic.ai.transactionagent.rest.Service.agent.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("v1/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    @Autowired
    private  final TransactionService agentService;
    @Autowired
    private final TransactionProducer producer;

    //without kafka
//    @PostMapping("/process")
//    public ResponseEntity<Transaction> process(@RequestBody Transaction tx) {
//
//        tx.setCreatedAt(LocalDateTime.now());
//        tx.setStatus("NEW");
//
//        return ResponseEntity.ok(agentService.process(tx));
//    }

    //kafka

    @PostMapping("/process")
    public ResponseEntity<?> create(@RequestBody Transaction tx) {

        tx.setCreatedAt(LocalDateTime.now());
        tx.setStatus("RECEIVED");

        producer.sendTransaction(tx);

        return ResponseEntity.ok("Transaction received and queued");
    }
}

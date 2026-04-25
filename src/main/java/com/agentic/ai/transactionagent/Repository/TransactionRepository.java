package com.agentic.ai.transactionagent.Repository;

import com.agentic.ai.transactionagent.rest.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Transaction> findAllByOrderByCreatedAtDesc();

    List<Transaction> findByStatusOrderByCreatedAtDesc(String status);

    long countByStatus(String status);
}

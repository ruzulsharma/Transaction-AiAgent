package com.agentic.ai.transactionagent.rest.Service.agent;

import com.agentic.ai.transactionagent.Repository.TransactionRepository;
import com.agentic.ai.transactionagent.rest.dto.StatsResponse;
import com.agentic.ai.transactionagent.rest.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private TransactionRepository repository;

    public List<Transaction> getAllTransactions() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public List<Transaction> getFrauds() {
        return repository.findByStatusOrderByCreatedAtDesc("FRAUD");
    }

    public StatsResponse getStats() {

        long total = repository.count();

        long fraud = repository.countByStatus("FRAUD");
        long processed = repository.countByStatus("PROCESSED");

        double fraudRate = total == 0 ? 0 : (double) fraud / total;

        StatsResponse stats = StatsResponse.builder()
                .totalTransactions(total)
                .fraudCount(fraud)
                .fraudRate(fraudRate)
                .processedCount(processed)
                .build();

        return stats;
    }

}

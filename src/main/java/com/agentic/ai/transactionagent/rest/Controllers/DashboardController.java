package com.agentic.ai.transactionagent.rest.Controllers;

import com.agentic.ai.transactionagent.rest.Service.agent.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {


    private final DashboardService service;

    @GetMapping("/transactions")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    @GetMapping("/frauds")
    public ResponseEntity<?> getFrauds() {
        return ResponseEntity.ok(service.getFrauds());
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(service.getStats());
    }


}

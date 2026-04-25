package com.agentic.ai.transactionagent.rest.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsResponse {
    public Long totalTransactions;
    public Long fraudCount;
    public Long processedCount;
    public Double fraudRate;
}

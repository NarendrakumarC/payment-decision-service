package com.payment.decision.model;

import java.time.Instant;
import java.util.List;

/**
 * Represents the aggregated payment decision returned to the caller.
 */
public class PaymentDecision {

    private final String transactionId;
    private final DecisionStatus status;
    private final List<ValidationResult> validationResults;
    private final String decisionReason;
    private final Instant decidedAt;

    public PaymentDecision(String transactionId, DecisionStatus status,
                           List<ValidationResult> validationResults,
                           String decisionReason) {
        this.transactionId = transactionId;
        this.status = status;
        this.validationResults = validationResults;
        this.decisionReason = decisionReason;
        this.decidedAt = Instant.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public DecisionStatus getStatus() {
        return status;
    }

    public List<ValidationResult> getValidationResults() {
        return validationResults;
    }

    public String getDecisionReason() {
        return decisionReason;
    }

    public Instant getDecidedAt() {
        return decidedAt;
    }
}

package com.payment.decision.model;

/**
 * Represents the final decision status for a payment.
 */
public enum DecisionStatus {

    /** Payment is approved — all validations passed. */
    APPROVED,

    /** Payment is declined — one or more validations failed. */
    DECLINED
}

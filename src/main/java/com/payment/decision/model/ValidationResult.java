package com.payment.decision.model;

/**
 * Represents the result of an individual validation check.
 */
public class ValidationResult {

    private final String checkName;
    private final boolean passed;
    private final String reason;

    public ValidationResult(String checkName, boolean passed, String reason) {
        this.checkName = checkName;
        this.passed = passed;
        this.reason = reason;
    }

    public String getCheckName() {
        return checkName;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getReason() {
        return reason;
    }
}

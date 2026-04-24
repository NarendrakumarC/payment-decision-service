package com.payment.decision.model;

import java.time.Instant;

/**
 * Standardised error response body returned on validation or processing failures.
 */
public class ErrorResponse {

    private final int status;
    private final String message;
    private final Instant timestamp;

    public ErrorResponse(int status, String message, Instant timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

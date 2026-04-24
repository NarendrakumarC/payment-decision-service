package com.payment.decision.service;

import com.payment.decision.model.PaymentRequest;
import com.payment.decision.model.ValidationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Checks whether the account has sufficient credit to cover the requested payment amount.
 *
 * <p>In production this service would query a core-banking or ledger system.
 * For demonstration and testing purposes it uses an in-memory credit-limit store.
 */
@Service
public class CreditAvailabilityService {

    static final String CHECK_NAME = "CREDIT_AVAILABILITY";

    /** Default credit limit applied when an account is not explicitly registered. */
    static final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal("5000.00");

    /** In-memory store of account credit limits (accountId → available credit). */
    private final Map<String, BigDecimal> accountCreditLimits = new ConcurrentHashMap<>();

    /**
     * Evaluates whether the account has sufficient credit for the requested amount.
     *
     * @param request the payment request to evaluate
     * @return a {@link ValidationResult} indicating whether the check passed
     */
    public ValidationResult evaluate(PaymentRequest request) {
        BigDecimal availableCredit = accountCreditLimits
                .getOrDefault(request.getAccountId(), DEFAULT_CREDIT_LIMIT);

        if (request.getAmount() == null || request.getAmount().compareTo(availableCredit) > 0) {
            return new ValidationResult(CHECK_NAME, false,
                    "Insufficient credit: requested " + request.getAmount()
                            + ", available " + availableCredit);
        }

        return new ValidationResult(CHECK_NAME, true,
                "Sufficient credit available: " + availableCredit);
    }

    /**
     * Registers or updates the credit limit for an account.
     * Useful for testing and simulation scenarios.
     *
     * @param accountId   the account identifier
     * @param creditLimit the credit limit to assign
     */
    public void setCreditLimit(String accountId, BigDecimal creditLimit) {
        accountCreditLimits.put(accountId, creditLimit);
    }
}

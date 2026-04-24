package com.payment.decision.service;

import com.payment.decision.model.PaymentRequest;
import com.payment.decision.model.ValidationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Performs a fraud check on the payment request.
 *
 * <p>In a production environment this service would call an external fraud-detection
 * platform (e.g., Feedzai, Actimize).  Here it applies a lightweight set of
 * heuristic rules that are easy to unit-test:
 * <ul>
 *   <li>Transactions above the high-risk threshold are declined.</li>
 *   <li>Transactions from known high-risk merchant categories are declined.</li>
 * </ul>
 */
@Service
public class FraudCheckService {

    static final String CHECK_NAME = "FRAUD_CHECK";

    /** Transactions at or above this amount trigger a fraud decline. */
    private static final BigDecimal HIGH_RISK_AMOUNT = new BigDecimal("10000.00");

    /** Merchant categories considered high-risk for fraud. */
    private static final Set<String> HIGH_RISK_CATEGORIES = Set.of(
            "GAMBLING", "CRYPTO_EXCHANGE", "ANONYMOUS_TRANSFER"
    );

    /**
     * Evaluates the payment request for potential fraud signals.
     *
     * @param request the payment request to evaluate
     * @return a {@link ValidationResult} indicating whether the check passed
     */
    public ValidationResult evaluate(PaymentRequest request) {
        if (request.getAmount() != null
                && request.getAmount().compareTo(HIGH_RISK_AMOUNT) >= 0) {
            return new ValidationResult(CHECK_NAME, false,
                    "Transaction amount exceeds fraud risk threshold of " + HIGH_RISK_AMOUNT);
        }

        String category = request.getMerchantCategory();
        if (category != null && HIGH_RISK_CATEGORIES.contains(category.toUpperCase())) {
            return new ValidationResult(CHECK_NAME, false,
                    "Merchant category '" + category + "' is flagged as high-risk");
        }

        return new ValidationResult(CHECK_NAME, true, "No fraud signals detected");
    }
}

package com.payment.decision.service;

import com.payment.decision.model.DecisionStatus;
import com.payment.decision.model.PaymentDecision;
import com.payment.decision.model.PaymentRequest;
import com.payment.decision.model.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Aggregates the results of all validation checks and produces the final
 * {@link PaymentDecision} for a payment request.
 *
 * <p>The decision logic is: <em>all</em> validation checks must pass for the
 * payment to be APPROVED.  A single failure causes the payment to be DECLINED,
 * and the reason of the first failed check is surfaced to the caller.
 */
@Service
public class PaymentDecisionService {

    private final FraudCheckService fraudCheckService;
    private final CreditAvailabilityService creditAvailabilityService;
    private final CurrencyConversionService currencyConversionService;

    public PaymentDecisionService(FraudCheckService fraudCheckService,
                                  CreditAvailabilityService creditAvailabilityService,
                                  CurrencyConversionService currencyConversionService) {
        this.fraudCheckService = fraudCheckService;
        this.creditAvailabilityService = creditAvailabilityService;
        this.currencyConversionService = currencyConversionService;
    }

    /**
     * Evaluates a payment request by running all validation checks and returning
     * an aggregated {@link PaymentDecision}.
     *
     * @param request the payment request to evaluate
     * @return the final payment decision
     */
    public PaymentDecision evaluate(PaymentRequest request) {
        List<ValidationResult> results = List.of(
                fraudCheckService.evaluate(request),
                creditAvailabilityService.evaluate(request),
                currencyConversionService.evaluate(request)
        );

        List<ValidationResult> failures = results.stream()
                .filter(r -> !r.isPassed())
                .collect(Collectors.toList());

        if (failures.isEmpty()) {
            return new PaymentDecision(
                    request.getTransactionId(),
                    DecisionStatus.APPROVED,
                    results,
                    "All validation checks passed"
            );
        }

        String declineReason = failures.stream()
                .map(ValidationResult::getReason)
                .collect(Collectors.joining("; "));

        return new PaymentDecision(
                request.getTransactionId(),
                DecisionStatus.DECLINED,
                results,
                declineReason
        );
    }
}

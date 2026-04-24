package com.payment.decision.service;

import com.payment.decision.model.PaymentRequest;
import com.payment.decision.model.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Checks whether the payment currency is supported and conversion is feasible.
 *
 * <p>In production this service would call a treasury / FX-rate provider.
 * Here it validates against a static list of supported ISO-4217 currency codes.
 */
@Service
public class CurrencyConversionService {

    static final String CHECK_NAME = "CURRENCY_CONVERSION";

    /** ISO-4217 currency codes that the platform currently supports. */
    private static final Set<String> SUPPORTED_CURRENCIES = Set.of(
            "USD", "EUR", "GBP", "JPY", "CAD", "AUD", "CHF", "CNY", "INR", "SGD"
    );

    /**
     * Validates that the payment currency is supported for conversion.
     *
     * @param request the payment request to evaluate
     * @return a {@link ValidationResult} indicating whether the check passed
     */
    public ValidationResult evaluate(PaymentRequest request) {
        String currency = request.getCurrency();
        if (currency == null || !SUPPORTED_CURRENCIES.contains(currency.toUpperCase())) {
            return new ValidationResult(CHECK_NAME, false,
                    "Currency '" + currency + "' is not supported for conversion");
        }

        return new ValidationResult(CHECK_NAME, true,
                "Currency '" + currency + "' is supported");
    }
}

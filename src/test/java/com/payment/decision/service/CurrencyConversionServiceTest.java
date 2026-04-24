package com.payment.decision.service;

import com.payment.decision.model.PaymentRequest;
import com.payment.decision.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyConversionServiceTest {

    private CurrencyConversionService currencyConversionService;

    @BeforeEach
    void setUp() {
        currencyConversionService = new CurrencyConversionService();
    }

    @Test
    void shouldPassForSupportedCurrencyUSD() {
        PaymentRequest request = buildRequest("USD");

        ValidationResult result = currencyConversionService.evaluate(request);

        assertThat(result.isPassed()).isTrue();
        assertThat(result.getCheckName()).isEqualTo(CurrencyConversionService.CHECK_NAME);
    }

    @Test
    void shouldPassForSupportedCurrencyEUR() {
        ValidationResult result = currencyConversionService.evaluate(buildRequest("EUR"));
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void shouldPassForSupportedCurrencyGBP() {
        ValidationResult result = currencyConversionService.evaluate(buildRequest("GBP"));
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void shouldFailForUnsupportedCurrency() {
        PaymentRequest request = buildRequest("XYZ");

        ValidationResult result = currencyConversionService.evaluate(request);

        assertThat(result.isPassed()).isFalse();
        assertThat(result.getReason()).contains("not supported");
    }

    @Test
    void shouldBeCaseInsensitiveForCurrencyCode() {
        ValidationResult result = currencyConversionService.evaluate(buildRequest("usd"));
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void shouldFailForNullCurrency() {
        PaymentRequest request = buildRequest(null);

        ValidationResult result = currencyConversionService.evaluate(request);

        assertThat(result.isPassed()).isFalse();
    }

    private PaymentRequest buildRequest(String currency) {
        return new PaymentRequest("TXN-001", "ACC001", new BigDecimal("100.00"),
                currency, "MERCH-1", "RETAIL");
    }
}

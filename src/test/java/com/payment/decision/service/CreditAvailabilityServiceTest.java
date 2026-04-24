package com.payment.decision.service;

import com.payment.decision.model.PaymentRequest;
import com.payment.decision.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CreditAvailabilityServiceTest {

    private CreditAvailabilityService creditAvailabilityService;

    @BeforeEach
    void setUp() {
        creditAvailabilityService = new CreditAvailabilityService();
    }

    @Test
    void shouldPassWhenAmountWithinDefaultCreditLimit() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("100.00"));

        ValidationResult result = creditAvailabilityService.evaluate(request);

        assertThat(result.isPassed()).isTrue();
        assertThat(result.getCheckName()).isEqualTo(CreditAvailabilityService.CHECK_NAME);
    }

    @Test
    void shouldFailWhenAmountExceedsDefaultCreditLimit() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("6000.00"));

        ValidationResult result = creditAvailabilityService.evaluate(request);

        assertThat(result.isPassed()).isFalse();
        assertThat(result.getReason()).contains("Insufficient credit");
    }

    @Test
    void shouldPassWhenAmountEqualsDefaultCreditLimit() {
        PaymentRequest request = buildRequest("ACC001",
                CreditAvailabilityService.DEFAULT_CREDIT_LIMIT);

        ValidationResult result = creditAvailabilityService.evaluate(request);

        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void shouldUseCustomCreditLimitWhenSet() {
        creditAvailabilityService.setCreditLimit("ACC002", new BigDecimal("200.00"));
        PaymentRequest request = buildRequest("ACC002", new BigDecimal("201.00"));

        ValidationResult result = creditAvailabilityService.evaluate(request);

        assertThat(result.isPassed()).isFalse();
    }

    @Test
    void shouldPassWithCustomCreditLimitWhenAmountIsWithinLimit() {
        creditAvailabilityService.setCreditLimit("ACC003", new BigDecimal("1000.00"));
        PaymentRequest request = buildRequest("ACC003", new BigDecimal("999.99"));

        ValidationResult result = creditAvailabilityService.evaluate(request);

        assertThat(result.isPassed()).isTrue();
    }

    private PaymentRequest buildRequest(String accountId, BigDecimal amount) {
        return new PaymentRequest("TXN-001", accountId, amount, "USD", "MERCH-1", "RETAIL");
    }
}

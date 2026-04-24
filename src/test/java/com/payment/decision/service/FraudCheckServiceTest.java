package com.payment.decision.service;

import com.payment.decision.model.PaymentRequest;
import com.payment.decision.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FraudCheckServiceTest {

    private FraudCheckService fraudCheckService;

    @BeforeEach
    void setUp() {
        fraudCheckService = new FraudCheckService();
    }

    @Test
    void shouldPassForNormalAmountAndSafeCategory() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("100.00"), "RETAIL");

        ValidationResult result = fraudCheckService.evaluate(request);

        assertThat(result.isPassed()).isTrue();
        assertThat(result.getCheckName()).isEqualTo(FraudCheckService.CHECK_NAME);
    }

    @Test
    void shouldFailWhenAmountExceedsHighRiskThreshold() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("10000.00"), "RETAIL");

        ValidationResult result = fraudCheckService.evaluate(request);

        assertThat(result.isPassed()).isFalse();
        assertThat(result.getReason()).contains("fraud risk threshold");
    }

    @Test
    void shouldFailForHighRiskMerchantCategory() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("500.00"), "GAMBLING");

        ValidationResult result = fraudCheckService.evaluate(request);

        assertThat(result.isPassed()).isFalse();
        assertThat(result.getReason()).contains("high-risk");
    }

    @Test
    void shouldFailForCryptoExchangeCategory() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("500.00"), "CRYPTO_EXCHANGE");

        ValidationResult result = fraudCheckService.evaluate(request);

        assertThat(result.isPassed()).isFalse();
    }

    @Test
    void shouldBeCaseInsensitiveForMerchantCategory() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("100.00"), "gambling");

        ValidationResult result = fraudCheckService.evaluate(request);

        assertThat(result.isPassed()).isFalse();
    }

    private PaymentRequest buildRequest(String accountId, BigDecimal amount, String category) {
        return new PaymentRequest("TXN-001", accountId, amount, "USD", "MERCH-1", category);
    }
}

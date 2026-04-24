package com.payment.decision.service;

import com.payment.decision.model.DecisionStatus;
import com.payment.decision.model.PaymentDecision;
import com.payment.decision.model.PaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentDecisionServiceTest {

    private PaymentDecisionService paymentDecisionService;
    private CreditAvailabilityService creditAvailabilityService;

    @BeforeEach
    void setUp() {
        FraudCheckService fraudCheckService = new FraudCheckService();
        creditAvailabilityService = new CreditAvailabilityService();
        CurrencyConversionService currencyConversionService = new CurrencyConversionService();
        paymentDecisionService = new PaymentDecisionService(
                fraudCheckService, creditAvailabilityService, currencyConversionService);
    }

    @Test
    void shouldApproveWhenAllChecksPass() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("100.00"), "USD", "RETAIL");

        PaymentDecision decision = paymentDecisionService.evaluate(request);

        assertThat(decision.getStatus()).isEqualTo(DecisionStatus.APPROVED);
        assertThat(decision.getTransactionId()).isEqualTo("TXN-001");
        assertThat(decision.getDecisionReason()).isEqualTo("All validation checks passed");
        assertThat(decision.getValidationResults()).hasSize(3);
        assertThat(decision.getDecidedAt()).isNotNull();
    }

    @Test
    void shouldDeclineWhenFraudCheckFails() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("15000.00"), "USD", "RETAIL");

        PaymentDecision decision = paymentDecisionService.evaluate(request);

        assertThat(decision.getStatus()).isEqualTo(DecisionStatus.DECLINED);
        assertThat(decision.getDecisionReason()).contains("fraud risk threshold");
    }

    @Test
    void shouldDeclineWhenCreditIsInsufficient() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("6000.00"), "USD", "RETAIL");

        PaymentDecision decision = paymentDecisionService.evaluate(request);

        assertThat(decision.getStatus()).isEqualTo(DecisionStatus.DECLINED);
        assertThat(decision.getDecisionReason()).contains("Insufficient credit");
    }

    @Test
    void shouldDeclineWhenCurrencyIsNotSupported() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("100.00"), "XYZ", "RETAIL");

        PaymentDecision decision = paymentDecisionService.evaluate(request);

        assertThat(decision.getStatus()).isEqualTo(DecisionStatus.DECLINED);
        assertThat(decision.getDecisionReason()).contains("not supported");
    }

    @Test
    void shouldDeclineWhenHighRiskMerchantCategory() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("100.00"), "USD", "GAMBLING");

        PaymentDecision decision = paymentDecisionService.evaluate(request);

        assertThat(decision.getStatus()).isEqualTo(DecisionStatus.DECLINED);
        assertThat(decision.getDecisionReason()).contains("high-risk");
    }

    @Test
    void shouldIncludeAllValidationResultsInDecision() {
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("100.00"), "USD", "RETAIL");

        PaymentDecision decision = paymentDecisionService.evaluate(request);

        assertThat(decision.getValidationResults())
                .extracting("checkName")
                .containsExactlyInAnyOrder(
                        FraudCheckService.CHECK_NAME,
                        CreditAvailabilityService.CHECK_NAME,
                        CurrencyConversionService.CHECK_NAME
                );
    }

    @Test
    void shouldCombineMultipleFailureReasonsInDeclineMessage() {
        // Amount high enough to trigger fraud check AND unsupported currency
        PaymentRequest request = buildRequest("ACC001", new BigDecimal("15000.00"), "XYZ", "RETAIL");

        PaymentDecision decision = paymentDecisionService.evaluate(request);

        assertThat(decision.getStatus()).isEqualTo(DecisionStatus.DECLINED);
        assertThat(decision.getDecisionReason()).contains("fraud risk threshold");
        assertThat(decision.getDecisionReason()).contains("not supported");
    }

    private PaymentRequest buildRequest(String accountId, BigDecimal amount,
                                        String currency, String category) {
        return new PaymentRequest("TXN-001", accountId, amount, currency, "MERCH-1", category);
    }
}

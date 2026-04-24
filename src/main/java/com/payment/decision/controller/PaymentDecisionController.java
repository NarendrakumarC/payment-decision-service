package com.payment.decision.controller;

import com.payment.decision.model.PaymentDecision;
import com.payment.decision.model.PaymentRequest;
import com.payment.decision.service.PaymentDecisionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes the payment decision endpoint.
 *
 * <p>Accepts a {@link PaymentRequest}, runs all validation checks via
 * {@link PaymentDecisionService}, and returns the aggregated {@link PaymentDecision}.
 */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentDecisionController {

    private final PaymentDecisionService paymentDecisionService;

    public PaymentDecisionController(PaymentDecisionService paymentDecisionService) {
        this.paymentDecisionService = paymentDecisionService;
    }

    /**
     * Evaluates a payment request and returns an approval or decline decision.
     *
     * @param request the payment request body
     * @return 200 OK with the decision payload
     */
    @PostMapping("/decide")
    public ResponseEntity<PaymentDecision> decide(@Valid @RequestBody PaymentRequest request) {
        PaymentDecision decision = paymentDecisionService.evaluate(request);
        return ResponseEntity.ok(decision);
    }
}

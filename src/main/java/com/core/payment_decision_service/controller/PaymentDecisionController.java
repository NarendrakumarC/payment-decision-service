package com.core.payment_decision_service.controller;


import com.core.payment_decision_service.model.PaymentStatus;
import com.core.payment_decision_service.service.PaymentDecisionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/payments")
public class PaymentDecisionController {


    private final PaymentDecisionService paymentDecisionService;

    public PaymentDecisionController(PaymentDecisionService paymentDecisionService) {
        this.paymentDecisionService = paymentDecisionService;
    }

    @GetMapping("/decision")
    public PaymentStatus decidePayment() {
        return paymentDecisionService.processPayment();
    }

}

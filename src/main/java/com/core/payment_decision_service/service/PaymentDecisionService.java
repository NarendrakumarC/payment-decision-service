package com.core.payment_decision_service.service;

import com.core.payment_decision_service.config.AsyncConfig;
import com.core.payment_decision_service.model.CreditStatus;
import com.core.payment_decision_service.model.ExchangeStatus;
import com.core.payment_decision_service.model.FraudStatus;
import com.core.payment_decision_service.model.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class PaymentDecisionService {

    private final FraudCheckService fraudCheckService;
    private final ExchangeRateService exchangeRateService;
    private final CreditCheckService creditCheckService;

    public PaymentDecisionService(FraudCheckService fraudCheckService, ExchangeRateService exchangeRateService, CreditCheckService creditCheckService) {
        this.fraudCheckService = fraudCheckService;
        this.exchangeRateService = exchangeRateService;
        this.creditCheckService = creditCheckService;
    }

    @Autowired
    private AsyncConfig asyncConfig;

    public PaymentStatus processPayment() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            Future<FraudStatus> fraudResult =
                    executor.submit(new FraudCheckService());

            Future<CreditStatus> creditResult =
                    executor.submit(new CreditCheckService());

            Future<ExchangeStatus> exchangeResult =
                    executor.submit(new ExchangeRateService());
            // Wait for all results
            FraudStatus fraudStatus = fraudResult.get();
            CreditStatus creditStatus = creditResult.get();
            ExchangeStatus exchangeStatus = exchangeResult.get();

            // Validation logic
            if (fraudStatus == FraudStatus.UNSAFE
                    || creditStatus == CreditStatus.INSUFFICIENT
                    || exchangeStatus == ExchangeStatus.INCONVERTIBLE) {

                return PaymentStatus.DECLINED;
            }
            return PaymentStatus.APPROVED;
        } catch (Exception e) {
            // Any failure = decline
            return PaymentStatus.DECLINED;
        } finally {
            executor.shutdown();
        }
    }
}

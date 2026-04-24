package com.core.payment_decision_service.service;

import com.core.payment_decision_service.model.FraudStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@Service
public class FraudCheckService implements Callable<FraudStatus> {

        @Override
        public FraudStatus call() throws Exception {
            System.out.println("Fraud Service thread name ::"+Thread.currentThread().getName());
            Thread.sleep(1000); // simulate delay
            return FraudStatus.SAFE;   // change to UNSAFE to test
        }

    /*public CompletableFuture<FraudStatus> checkFraudAsync() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            return FraudStatus.SAFE; // change to UNSAFE to test
        });
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }*/


}

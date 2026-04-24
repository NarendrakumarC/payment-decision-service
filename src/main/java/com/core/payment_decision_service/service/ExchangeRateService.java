package com.core.payment_decision_service.service;

import com.core.payment_decision_service.model.ExchangeStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

@Service
public class ExchangeRateService implements Callable<ExchangeStatus> {

    public ExchangeStatus call() throws Exception {
        System.out.println("ExchangeRate Service thread name ::"+Thread.currentThread().getName());
        sleep(1200);
        return ExchangeStatus.CONVERTIBLE; // change to INCONVERTIBLE to test
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {
            ignored.printStackTrace();
        }
    }
}

package com.core.payment_decision_service.service;

import com.core.payment_decision_service.model.CreditStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

@Service
public class CreditCheckService implements Callable<CreditStatus> {

    public CreditStatus call() throws Exception {
        System.out.println("Credit Service thread name ::"+Thread.currentThread().getName());
        sleep(1500);
        return CreditStatus.SUFFICIENT; // change to INSUFFICIENT to test
    }


}

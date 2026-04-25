# Payment-decision-service
Design multi-threaded microservice responsible for making the final payment approval or decline decision by aggregating multiple validation services.
Used in banking, card networks, fintech platforms

Example responsibilities

* Fraud check outcome
* Credit availability
* Currency conversion feasibility
* Risk thresholds (future)


✅ Problem Summary
You have a Payment Service that depends on 3 independent services:

Fraud Check Service
* Result: SAFE or UNSAFE

Credit Check Service
* Result: SUFFICIENT or INSUFFICIENT

Exchange Rate Service
* Result: CONVERTIBLE or INCONVERTIBLE



✅ Final Decision Rule

If any service returns an invalid status
(UNSAFE, INSUFFICIENT, INCONVERTIBLE)
➜ Payment = DECLINED
Else
➜ Payment = APPROVED

✅ Technical Requirement
* Invoke all services in parallel (multithreaded)
* Aggregate results
* Return final status

✅ Design Approach
We will use:

1. ExecutorService (Thread Pool)
2. Callable (to return results)
3. Future (to collect results safely)
4. Enums (type-safe statuses)

✅ High-Level Design

PaymentService

├── FraudCheckService   (Callable<FraudStatus>)

├── CreditCheckService  (Callable<CreditStatus>)

├── ExchangeRateService (Callable<ExchangeStatus>)

└── ExecutorService (runs all services in parallel)


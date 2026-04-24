# payment-decision-service

A Spring Boot microservice responsible for making the **final payment approval or decline decision** by aggregating multiple validation services.

✅ Applicable in banking, card networks, and fintech platforms.

---

## Responsibilities

| Validation Check         | Description |
|--------------------------|-------------|
| **Fraud Check**          | Flags transactions exceeding the high-risk amount threshold or originating from high-risk merchant categories (e.g. GAMBLING, CRYPTO_EXCHANGE). |
| **Credit Availability**  | Verifies that the account has sufficient credit to cover the requested amount. |
| **Currency Conversion**  | Validates that the payment currency is supported for conversion (USD, EUR, GBP, JPY, CAD, AUD, CHF, CNY, INR, SGD). |
| **Risk Thresholds**      | *(Future)* Configurable risk-score thresholds for additional control. |

---

## API

### POST `/api/v1/payments/decide`

Evaluates a payment request and returns an approval or decline decision.

**Request body**

```json
{
  "transactionId": "TXN-20240101-001",
  "accountId": "ACC-12345",
  "amount": 250.00,
  "currency": "USD",
  "merchantId": "MERCH-789",
  "merchantCategory": "RETAIL"
}
```

**Response body**

```json
{
  "transactionId": "TXN-20240101-001",
  "status": "APPROVED",
  "validationResults": [
    { "checkName": "FRAUD_CHECK",          "passed": true,  "reason": "No fraud signals detected" },
    { "checkName": "CREDIT_AVAILABILITY",  "passed": true,  "reason": "Sufficient credit available: 5000.00" },
    { "checkName": "CURRENCY_CONVERSION",  "passed": true,  "reason": "Currency 'USD' is supported" }
  ],
  "decisionReason": "All validation checks passed",
  "decidedAt": "2024-01-01T10:00:00Z"
}
```

`status` is either `APPROVED` or `DECLINED`.  When declined the `decisionReason` field lists the reasons from every failing check.

---

## Running locally

```bash
mvn spring-boot:run
```

The service starts on port **8080**.

---

## Running the tests

```bash
mvn test
```

29 unit and slice tests cover all validation services and the REST controller.

---

## Project structure

```
src/
├── main/java/com/payment/decision/
│   ├── PaymentDecisionServiceApplication.java   # Spring Boot entry point
│   ├── controller/
│   │   ├── PaymentDecisionController.java        # REST endpoint
│   │   └── GlobalExceptionHandler.java           # Error handling
│   ├── service/
│   │   ├── PaymentDecisionService.java           # Decision aggregator
│   │   ├── FraudCheckService.java
│   │   ├── CreditAvailabilityService.java
│   │   └── CurrencyConversionService.java
│   ├── model/
│   │   ├── PaymentRequest.java
│   │   ├── PaymentDecision.java
│   │   ├── ValidationResult.java
│   │   ├── DecisionStatus.java
│   │   └── ErrorResponse.java
│   └── exception/
│       └── PaymentValidationException.java
└── test/java/com/payment/decision/
    ├── controller/PaymentDecisionControllerTest.java
    └── service/
        ├── PaymentDecisionServiceTest.java
        ├── FraudCheckServiceTest.java
        ├── CreditAvailabilityServiceTest.java
        └── CurrencyConversionServiceTest.java
```

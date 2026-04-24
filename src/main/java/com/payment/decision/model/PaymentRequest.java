package com.payment.decision.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Represents an incoming payment request to be evaluated for approval or decline.
 */
public class PaymentRequest {

    @NotBlank(message = "Transaction ID must not be blank")
    private String transactionId;

    @NotBlank(message = "Account ID must not be blank")
    private String accountId;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency must not be blank")
    private String currency;

    @NotBlank(message = "Merchant ID must not be blank")
    private String merchantId;

    private String merchantCategory;

    public PaymentRequest() {
    }

    public PaymentRequest(String transactionId, String accountId, BigDecimal amount,
                          String currency, String merchantId, String merchantCategory) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.merchantId = merchantId;
        this.merchantCategory = merchantCategory;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantCategory() {
        return merchantCategory;
    }

    public void setMerchantCategory(String merchantCategory) {
        this.merchantCategory = merchantCategory;
    }
}

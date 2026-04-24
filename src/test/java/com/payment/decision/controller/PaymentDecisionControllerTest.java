package com.payment.decision.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.decision.model.DecisionStatus;
import com.payment.decision.model.PaymentDecision;
import com.payment.decision.model.PaymentRequest;
import com.payment.decision.service.PaymentDecisionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentDecisionController.class)
class PaymentDecisionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentDecisionService paymentDecisionService;

    @Test
    void shouldReturn200WithApprovedDecision() throws Exception {
        PaymentDecision approvedDecision = new PaymentDecision(
                "TXN-001", DecisionStatus.APPROVED, Collections.emptyList(),
                "All validation checks passed");
        Mockito.when(paymentDecisionService.evaluate(any())).thenReturn(approvedDecision);

        PaymentRequest request = validRequest();

        mockMvc.perform(post("/api/v1/payments/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TXN-001"))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.decisionReason").value("All validation checks passed"));
    }

    @Test
    void shouldReturn200WithDeclinedDecision() throws Exception {
        PaymentDecision declinedDecision = new PaymentDecision(
                "TXN-002", DecisionStatus.DECLINED, Collections.emptyList(),
                "Insufficient credit");
        Mockito.when(paymentDecisionService.evaluate(any())).thenReturn(declinedDecision);

        PaymentRequest request = validRequest();
        request.setTransactionId("TXN-002");

        mockMvc.perform(post("/api/v1/payments/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DECLINED"))
                .andExpect(jsonPath("$.decisionReason").value("Insufficient credit"));
    }

    @Test
    void shouldReturn400WhenTransactionIdIsBlank() throws Exception {
        PaymentRequest request = validRequest();
        request.setTransactionId("");

        mockMvc.perform(post("/api/v1/payments/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAmountIsNull() throws Exception {
        PaymentRequest request = validRequest();
        request.setAmount(null);

        mockMvc.perform(post("/api/v1/payments/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAmountIsZero() throws Exception {
        PaymentRequest request = validRequest();
        request.setAmount(BigDecimal.ZERO);

        mockMvc.perform(post("/api/v1/payments/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenCurrencyIsBlank() throws Exception {
        PaymentRequest request = validRequest();
        request.setCurrency("");

        mockMvc.perform(post("/api/v1/payments/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private PaymentRequest validRequest() {
        return new PaymentRequest("TXN-001", "ACC001", new BigDecimal("100.00"),
                "USD", "MERCH-1", "RETAIL");
    }
}

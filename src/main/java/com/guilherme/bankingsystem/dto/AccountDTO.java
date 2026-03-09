package com.guilherme.bankingsystem.dto;

import com.guilherme.bankingsystem.enums.TransactionStatus;
import com.guilherme.bankingsystem.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ── Request: Criar conta ──────────────────────────────────────────
public class AccountDTO {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "Nome do titular é obrigatório")
        private String titular;

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
        private String cpf;

        @DecimalMin(value = "0.0", message = "Saldo inicial não pode ser negativo")
        private BigDecimal saldoInicial = BigDecimal.ZERO;

        @DecimalMin(value = "0.0", message = "Limite de crédito não pode ser negativo")
        private BigDecimal limiteCredito = BigDecimal.ZERO;
    }

    // ── Response: Dados da conta ──────────────────────────────────
    @Data
    public static class Response {
        private Long id;
        private String numeroConta;
        private String titular;
        private String cpf;
        private BigDecimal saldo;
        private BigDecimal limiteCredito;
        private Boolean ativa;
        private LocalDateTime criadaEm;
    }
}

package com.guilherme.bankingsystem.dto;

import com.guilherme.bankingsystem.enums.TransactionStatus;
import com.guilherme.bankingsystem.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    // ── Request: Depósito ─────────────────────────────────────────
    @Data
    public static class DepositoRequest {
        @NotBlank(message = "Número da conta é obrigatório")
        private String numeroConta;

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor mínimo é R$ 0,01")
        private BigDecimal valor;

        private String descricao;
    }

    // ── Request: Saque ────────────────────────────────────────────
    @Data
    public static class SaqueRequest {
        @NotBlank(message = "Número da conta é obrigatório")
        private String numeroConta;

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor mínimo é R$ 0,01")
        private BigDecimal valor;

        private String descricao;
    }

    // ── Request: Transferência ────────────────────────────────────
    @Data
    public static class TransferenciaRequest {
        @NotBlank(message = "Conta de origem é obrigatória")
        private String numeroContaOrigem;

        @NotBlank(message = "Conta de destino é obrigatória")
        private String numeroContaDestino;

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor mínimo é R$ 0,01")
        private BigDecimal valor;

        private String descricao;
    }

    // ── Response: Resultado da transação ──────────────────────────
    @Data
    public static class Response {
        private Long id;
        private String contaOrigem;
        private String contaDestino;
        private BigDecimal valor;
        private TransactionType tipo;
        private TransactionStatus status;
        private String motivoNegacao;
        private String descricao;
        private LocalDateTime realizadaEm;
    }
}

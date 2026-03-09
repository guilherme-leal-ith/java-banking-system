package com.guilherme.bankingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroConta;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteCredito;

    @Column(nullable = false)
    private Boolean ativa;

    @Column(nullable = false)
    private LocalDateTime criadaEm;

    @PrePersist
    public void prePersist() {
        this.criadaEm = LocalDateTime.now();
        if (this.ativa == null) this.ativa = true;
        if (this.saldo == null) this.saldo = BigDecimal.ZERO;
        if (this.limiteCredito == null) this.limiteCredito = BigDecimal.ZERO;
    }
}

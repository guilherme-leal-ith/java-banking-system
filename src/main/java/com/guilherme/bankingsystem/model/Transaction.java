package com.guilherme.bankingsystem.model;

import com.guilherme.bankingsystem.enums.TransactionStatus;
import com.guilherme.bankingsystem.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id")
    private Account contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Account contaDestino;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    private String motivoNegacao;

    private String descricao;

    @Column(nullable = false)
    private LocalDateTime realizadaEm;

    @PrePersist
    public void prePersist() {
        this.realizadaEm = LocalDateTime.now();
    }
}

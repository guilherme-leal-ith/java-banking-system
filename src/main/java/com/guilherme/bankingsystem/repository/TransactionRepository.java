package com.guilherme.bankingsystem.repository;

import com.guilherme.bankingsystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByContaOrigemIdOrContaDestinoIdOrderByRealizadaEmDesc(Long origemId, Long destinoId);

    List<Transaction> findByContaOrigemIdOrderByRealizadaEmDesc(Long contaOrigemId);
}

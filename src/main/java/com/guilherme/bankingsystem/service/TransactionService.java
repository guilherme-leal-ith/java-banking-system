package com.guilherme.bankingsystem.service;

import com.guilherme.bankingsystem.dto.TransactionDTO;
import com.guilherme.bankingsystem.enums.TransactionStatus;
import com.guilherme.bankingsystem.enums.TransactionType;
import com.guilherme.bankingsystem.exception.BusinessException;
import com.guilherme.bankingsystem.model.Account;
import com.guilherme.bankingsystem.model.Transaction;
import com.guilherme.bankingsystem.repository.AccountRepository;
import com.guilherme.bankingsystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    // ── DEPÓSITO ──────────────────────────────────────────────────
    @Transactional
    public TransactionDTO.Response depositar(TransactionDTO.DepositoRequest request) {
        Account conta = accountService.buscarContaAtiva(request.getNumeroConta());

        // Regra: depósito máximo de R$ 50.000 por operação
        if (request.getValor().compareTo(new BigDecimal("50000")) > 0) {
            return salvarTransacaoNegada(null, conta,
                    request.getValor(), TransactionType.DEPOSITO,
                    "Depósito acima do limite de R$ 50.000 por operação.", request.getDescricao());
        }

        conta.setSaldo(conta.getSaldo().add(request.getValor()));
        accountRepository.save(conta);

        return salvarTransacaoAutorizada(null, conta,
                request.getValor(), TransactionType.DEPOSITO, request.getDescricao());
    }

    // ── SAQUE ─────────────────────────────────────────────────────
    @Transactional
    public TransactionDTO.Response sacar(TransactionDTO.SaqueRequest request) {
        Account conta = accountService.buscarContaAtiva(request.getNumeroConta());

        BigDecimal saldoDisponivel = conta.getSaldo().add(conta.getLimiteCredito());

        if (request.getValor().compareTo(saldoDisponivel) > 0) {
            return salvarTransacaoNegada(conta, null,
                    request.getValor(), TransactionType.SAQUE,
                    "Saldo insuficiente. Saldo disponível: R$ " + saldoDisponivel, request.getDescricao());
        }

        // Regra: saque máximo de R$ 5.000 por operação
        if (request.getValor().compareTo(new BigDecimal("5000")) > 0) {
            return salvarTransacaoNegada(conta, null,
                    request.getValor(), TransactionType.SAQUE,
                    "Saque acima do limite de R$ 5.000 por operação.", request.getDescricao());
        }

        conta.setSaldo(conta.getSaldo().subtract(request.getValor()));
        accountRepository.save(conta);

        return salvarTransacaoAutorizada(conta, null,
                request.getValor(), TransactionType.SAQUE, request.getDescricao());
    }

    // ── TRANSFERÊNCIA ─────────────────────────────────────────────
    @Transactional
    public TransactionDTO.Response transferir(TransactionDTO.TransferenciaRequest request) {
        if (request.getNumeroContaOrigem().equals(request.getNumeroContaDestino())) {
            throw new BusinessException("Conta de origem e destino não podem ser iguais.");
        }

        Account origem = accountService.buscarContaAtiva(request.getNumeroContaOrigem());
        Account destino = accountService.buscarContaAtiva(request.getNumeroContaDestino());

        BigDecimal saldoDisponivel = origem.getSaldo().add(origem.getLimiteCredito());

        if (request.getValor().compareTo(saldoDisponivel) > 0) {
            return salvarTransacaoNegada(origem, destino,
                    request.getValor(), TransactionType.TRANSFERENCIA,
                    "Saldo insuficiente. Saldo disponível: R$ " + saldoDisponivel, request.getDescricao());
        }

        // Regra: transferência máxima de R$ 10.000 por operação
        if (request.getValor().compareTo(new BigDecimal("10000")) > 0) {
            return salvarTransacaoNegada(origem, destino,
                    request.getValor(), TransactionType.TRANSFERENCIA,
                    "Transferência acima do limite de R$ 10.000 por operação.", request.getDescricao());
        }

        origem.setSaldo(origem.getSaldo().subtract(request.getValor()));
        destino.setSaldo(destino.getSaldo().add(request.getValor()));

        accountRepository.save(origem);
        accountRepository.save(destino);

        return salvarTransacaoAutorizada(origem, destino,
                request.getValor(), TransactionType.TRANSFERENCIA, request.getDescricao());
    }

    // ── EXTRATO ───────────────────────────────────────────────────
    public List<TransactionDTO.Response> extrato(String numeroConta) {
        Account conta = accountService.buscarContaAtiva(numeroConta);
        return transactionRepository
                .findByContaOrigemIdOrContaDestinoIdOrderByRealizadaEmDesc(conta.getId(), conta.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Helpers internos ──────────────────────────────────────────
    private TransactionDTO.Response salvarTransacaoAutorizada(Account origem, Account destino,
                                                               BigDecimal valor, TransactionType tipo,
                                                               String descricao) {
        Transaction tx = Transaction.builder()
                .contaOrigem(origem)
                .contaDestino(destino)
                .valor(valor)
                .tipo(tipo)
                .status(TransactionStatus.AUTORIZADA)
                .descricao(descricao)
                .build();
        return toResponse(transactionRepository.save(tx));
    }

    private TransactionDTO.Response salvarTransacaoNegada(Account origem, Account destino,
                                                           BigDecimal valor, TransactionType tipo,
                                                           String motivo, String descricao) {
        Transaction tx = Transaction.builder()
                .contaOrigem(origem)
                .contaDestino(destino)
                .valor(valor)
                .tipo(tipo)
                .status(TransactionStatus.NEGADA)
                .motivoNegacao(motivo)
                .descricao(descricao)
                .build();
        return toResponse(transactionRepository.save(tx));
    }

    public TransactionDTO.Response toResponse(Transaction tx) {
        TransactionDTO.Response response = new TransactionDTO.Response();
        response.setId(tx.getId());
        response.setContaOrigem(tx.getContaOrigem() != null ? tx.getContaOrigem().getNumeroConta() : null);
        response.setContaDestino(tx.getContaDestino() != null ? tx.getContaDestino().getNumeroConta() : null);
        response.setValor(tx.getValor());
        response.setTipo(tx.getTipo());
        response.setStatus(tx.getStatus());
        response.setMotivoNegacao(tx.getMotivoNegacao());
        response.setDescricao(tx.getDescricao());
        response.setRealizadaEm(tx.getRealizadaEm());
        return response;
    }
}

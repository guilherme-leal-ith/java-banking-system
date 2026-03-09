package com.guilherme.bankingsystem.service;

import com.guilherme.bankingsystem.dto.AccountDTO;
import com.guilherme.bankingsystem.exception.BusinessException;
import com.guilherme.bankingsystem.exception.NotFoundException;
import com.guilherme.bankingsystem.model.Account;
import com.guilherme.bankingsystem.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountDTO.Response criarConta(AccountDTO.CreateRequest request) {
        if (accountRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Já existe uma conta cadastrada com este CPF.");
        }

        Account account = Account.builder()
                .numeroConta(gerarNumeroConta())
                .titular(request.getTitular())
                .cpf(request.getCpf())
                .saldo(request.getSaldoInicial())
                .limiteCredito(request.getLimiteCredito())
                .ativa(true)
                .build();

        return toResponse(accountRepository.save(account));
    }

    public AccountDTO.Response buscarPorNumeroConta(String numeroConta) {
        Account account = accountRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada: " + numeroConta));
        return toResponse(account);
    }

    public List<AccountDTO.Response> listarContas() {
        return accountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AccountDTO.Response desativarConta(String numeroConta) {
        Account account = accountRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada: " + numeroConta));

        if (!account.getAtiva()) {
            throw new BusinessException("Conta já está desativada.");
        }

        account.setAtiva(false);
        return toResponse(accountRepository.save(account));
    }

    // ── Método interno usado pelos serviços de transação ──────────
    public Account buscarContaAtiva(String numeroConta) {
        Account account = accountRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada: " + numeroConta));

        if (!account.getAtiva()) {
            throw new BusinessException("Conta " + numeroConta + " está desativada.");
        }

        return account;
    }

    private String gerarNumeroConta() {
        String numero;
        do {
            numero = String.format("%08d", new Random().nextInt(99999999));
        } while (accountRepository.existsByNumeroConta(numero));
        return numero;
    }

    public AccountDTO.Response toResponse(Account account) {
        AccountDTO.Response response = new AccountDTO.Response();
        response.setId(account.getId());
        response.setNumeroConta(account.getNumeroConta());
        response.setTitular(account.getTitular());
        response.setCpf(account.getCpf());
        response.setSaldo(account.getSaldo());
        response.setLimiteCredito(account.getLimiteCredito());
        response.setAtiva(account.getAtiva());
        response.setCriadaEm(account.getCriadaEm());
        return response;
    }
}

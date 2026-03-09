package com.guilherme.bankingsystem.controller;

import com.guilherme.bankingsystem.dto.TransactionDTO;
import com.guilherme.bankingsystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposito")
    public ResponseEntity<TransactionDTO.Response> depositar(@Valid @RequestBody TransactionDTO.DepositoRequest request) {
        return ResponseEntity.ok(transactionService.depositar(request));
    }

    @PostMapping("/saque")
    public ResponseEntity<TransactionDTO.Response> sacar(@Valid @RequestBody TransactionDTO.SaqueRequest request) {
        return ResponseEntity.ok(transactionService.sacar(request));
    }

    @PostMapping("/transferencia")
    public ResponseEntity<TransactionDTO.Response> transferir(@Valid @RequestBody TransactionDTO.TransferenciaRequest request) {
        return ResponseEntity.ok(transactionService.transferir(request));
    }

    @GetMapping("/extrato/{numeroConta}")
    public ResponseEntity<List<TransactionDTO.Response>> extrato(@PathVariable String numeroConta) {
        return ResponseEntity.ok(transactionService.extrato(numeroConta));
    }
}

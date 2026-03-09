package com.guilherme.bankingsystem.controller;

import com.guilherme.bankingsystem.dto.AccountDTO;
import com.guilherme.bankingsystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO.Response> criarConta(@Valid @RequestBody AccountDTO.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.criarConta(request));
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO.Response>> listarContas() {
        return ResponseEntity.ok(accountService.listarContas());
    }

    @GetMapping("/{numeroConta}")
    public ResponseEntity<AccountDTO.Response> buscarConta(@PathVariable String numeroConta) {
        return ResponseEntity.ok(accountService.buscarPorNumeroConta(numeroConta));
    }

    @PatchMapping("/{numeroConta}/desativar")
    public ResponseEntity<AccountDTO.Response> desativarConta(@PathVariable String numeroConta) {
        return ResponseEntity.ok(accountService.desativarConta(numeroConta));
    }
}

package com.guilherme.bankingsystem;

import com.guilherme.bankingsystem.dto.AccountDTO;
import com.guilherme.bankingsystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Popula o banco com dados de exemplo para facilitar os testes manuais.
 * Remova esta classe quando conectar um banco real.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AccountService accountService;

    @Override
    public void run(String... args) {
        criarContaExemplo("João Silva", "12345678901", "1000.00", "500.00");
        criarContaExemplo("Maria Oliveira", "98765432100", "2500.00", "1000.00");
        criarContaExemplo("Carlos Santos", "11122233344", "0.00", "0.00");
        log.info("✅ Contas de exemplo criadas! Acesse GET /api/contas para visualizá-las.");
    }

    private void criarContaExemplo(String titular, String cpf, String saldo, String limite) {
        AccountDTO.CreateRequest req = new AccountDTO.CreateRequest();
        req.setTitular(titular);
        req.setCpf(cpf);
        req.setSaldoInicial(new BigDecimal(saldo));
        req.setLimiteCredito(new BigDecimal(limite));
        AccountDTO.Response res = accountService.criarConta(req);
        log.info("Conta criada → {} | Titular: {}", res.getNumeroConta(), res.getTitular());
    }
}

package com.guilherme.bankingsystem.repository;

import com.guilherme.bankingsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNumeroConta(String numeroConta);

    Optional<Account> findByCpf(String cpf);

    boolean existsByNumeroConta(String numeroConta);

    boolean existsByCpf(String cpf);
}

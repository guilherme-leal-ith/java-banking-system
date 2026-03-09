# 🏦 Banking System — MVP

Sistema backend em **Java + Spring Boot** que simula autorização de transações bancárias. Este é um MVP pensado para ser refatorado e evoluído progressivamente.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Para quê |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2 | Framework web |
| Spring Data JPA | 3.2 | Persistência |
| H2 Database | — | Banco em memória (dev) |
| Lombok | — | Reduzir boilerplate |
| Bean Validation | — | Validação dos DTOs |

---

## 📁 Estrutura do Projeto

```
src/main/java/com/banking/
├── BankingApplication.java        # Ponto de entrada
├── DataSeeder.java                # Dados de exemplo (dev)
│
├── controller/
│   ├── AccountController.java     # Endpoints de conta
│   └── TransactionController.java # Endpoints de transação
│
├── service/
│   ├── AccountService.java        # Regras de negócio de conta
│   └── TransactionService.java    # Autorização de transações
│
├── repository/
│   ├── AccountRepository.java
│   └── TransactionRepository.java
│
├── model/
│   ├── Account.java               # Entidade conta
│   └── Transaction.java           # Entidade transação
│
├── dto/
│   ├── AccountDTO.java            # Request/Response de conta
│   └── TransactionDTO.java        # Request/Response de transação
│
├── enums/
│   ├── TransactionType.java       # DEPOSITO, SAQUE, TRANSFERENCIA, PAGAMENTO
│   └── TransactionStatus.java     # AUTORIZADA, NEGADA, PENDENTE
│
└── exception/
    ├── BusinessException.java
    ├── NotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## ▶️ Como Rodar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Executar
```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`

> O banco H2 é **em memória**: os dados são perdidos ao reiniciar. Ideal para desenvolvimento. Ao iniciar, 3 contas de exemplo são criadas automaticamente.

### Console H2 (banco de dados visual)
Acesse: `http://localhost:8080/h2-console`

```
JDBC URL: jdbc:h2:mem:bankingdb
User: sa
Password: (vazio)
```

---

## 📡 Endpoints da API

### Contas

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/contas` | Criar conta |
| `GET` | `/api/contas` | Listar todas as contas |
| `GET` | `/api/contas/{numeroConta}` | Buscar conta |
| `PATCH` | `/api/contas/{numeroConta}/desativar` | Desativar conta |

### Transações

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/transacoes/deposito` | Realizar depósito |
| `POST` | `/api/transacoes/saque` | Realizar saque |
| `POST` | `/api/transacoes/transferencia` | Realizar transferência |
| `GET` | `/api/transacoes/extrato/{numeroConta}` | Ver extrato |

---

## 📋 Exemplos de Uso

### Criar Conta
```http
POST /api/contas
Content-Type: application/json

{
  "titular": "Ana Lima",
  "cpf": "55566677788",
  "saldoInicial": 1000.00,
  "limiteCredito": 500.00
}
```

### Depositar
```http
POST /api/transacoes/deposito
Content-Type: application/json

{
  "numeroConta": "00000001",
  "valor": 500.00,
  "descricao": "Salário"
}
```

### Sacar
```http
POST /api/transacoes/saque
Content-Type: application/json

{
  "numeroConta": "00000001",
  "valor": 200.00,
  "descricao": "Supermercado"
}
```

### Transferir
```http
POST /api/transacoes/transferencia
Content-Type: application/json

{
  "numeroContaOrigem": "00000001",
  "numeroContaDestino": "00000002",
  "valor": 150.00,
  "descricao": "Aluguel"
}
```

### Extrato
```http
GET /api/transacoes/extrato/00000001
```

---

## ✅ Regras de Negócio Implementadas

| Regra | Detalhe |
|---|---|
| CPF único | Não é possível criar duas contas com o mesmo CPF |
| Saldo insuficiente | Saque/transferência negado se não há saldo (nem limite) |
| Limite por operação — Depósito | Máximo de R$ 50.000 por operação |
| Limite por operação — Saque | Máximo de R$ 5.000 por operação |
| Limite por operação — Transferência | Máximo de R$ 10.000 por operação |
| Conta inativa | Operações bloqueadas em contas desativadas |
| Transferência para si mesmo | Bloqueada |
| Histórico completo | Transações negadas também são salvas com motivo |

---

## 🔮 Próximos Passos (Refatorações Sugeridas)

- [ ] Substituir H2 por PostgreSQL/MySQL
- [ ] Adicionar Spring Security com autenticação JWT
- [ ] Criar testes unitários (JUnit 5 + Mockito)
- [ ] Adicionar paginação no extrato
- [ ] Implementar tipo `PAGAMENTO` com validação de boleto
- [ ] Adicionar Swagger/OpenAPI para documentação interativa
- [ ] Criar limite diário de transações por conta
- [ ] Implementar auditoria de alterações (Spring Data Envers)
- [ ] Containerizar com Docker + docker-compose

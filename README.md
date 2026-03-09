# java-banking-system

Projeto backend em Java + Spring Boot que simula um sistema bancário básico — criado pra praticar arquitetura em camadas, JPA e regras de negócio.

---

## Tecnologias usadas

- Java 21
- Spring Boot 4.0.3
- Spring Data JPA
- Banco H2 (em memória, pra não precisar configurar nada)
- Lombok

---

## Como rodar

Precisa ter o Java 21 instalado.

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`

Ao iniciar, 3 contas de exemplo já são criadas automaticamente pra facilitar os testes.

---

## Endpoints

### Contas
| Método | Endpoint | O que faz |
|---|---|---|
| POST | `/api/contas` | Cria uma conta |
| GET | `/api/contas` | Lista todas as contas |
| GET | `/api/contas/{numeroConta}` | Busca uma conta |
| PATCH | `/api/contas/{numeroConta}/desativar` | Desativa uma conta |

### Transações
| Método | Endpoint | O que faz |
|---|---|---|
| POST | `/api/transacoes/deposito` | Faz um depósito |
| POST | `/api/transacoes/saque` | Faz um saque |
| POST | `/api/transacoes/transferencia` | Faz uma transferência |
| GET | `/api/transacoes/extrato/{numeroConta}` | Pega o extrato |

---

## Exemplos de requisição

**Criar conta**
```json
POST /api/contas
{
  "titular": "Ana Lima",
  "cpf": "55566677788",
  "saldoInicial": 1000.00,
  "limiteCredito": 500.00
}
```

**Depositar**
```json
POST /api/transacoes/deposito
{
  "numeroConta": "00000001",
  "valor": 500.00,
  "descricao": "Salário"
}
```

**Transferir**
```json
POST /api/transacoes/transferencia
{
  "numeroContaOrigem": "00000001",
  "numeroContaDestino": "00000002",
  "valor": 150.00,
  "descricao": "Aluguel"
}
```

---

## Regras implementadas

- CPF único por conta
- Saldo insuficiente bloqueia saque e transferência (considera limite de crédito)
- Limite por operação: depósito R$50k, saque R$5k, transferência R$10k
- Conta desativada não realiza operações
- Não é possível transferir pra si mesmo
- Transações negadas também são salvas com o motivo

---

## O que ainda quero fazer

- [ ] Trocar H2 por PostgreSQL
- [ ] Autenticação com JWT
- [ ] Testes unitários com JUnit
- [ ] Swagger pra documentar a API
- [ ] Docker

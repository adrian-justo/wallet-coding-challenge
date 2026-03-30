# Wallet Coding Challenge
A basic bookkeeping (accounting) application that keeps track of funds. Also called a “wallet" in online gaming terminology.

## Tech Stack
- Java 25
- Spring Boot 4
- MySQL 9
- Flyway
- Docker

## API Documentation
Once the application is running, access the Swagger UI documentation at:
```http
http://localhost:8080/docs
```
| Endpoint | Description |
| :--- | :--- |
| `GET /api/v1/accounts` | View all accounts |
| `POST /api/v1/accounts` | Create an account |
| `GET /api/v1/accounts/{accountId}/transactions` | View transactions of an account |
| `GET /api/v1/accounts/{accountId}/balance` | View balance of an account |
| `GET /api/v1/transactions` | View all transactions |
| `POST /api/v1/transactions/transfer` | Transfer balance to another account |

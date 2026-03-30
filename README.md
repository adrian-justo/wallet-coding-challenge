# Wallet Coding Challenge
A basic bookkeeping (accounting) application that keeps track of funds. Also called a “wallet" in online gaming terminology.

## Tech Stack
- Java 25
- Spring Boot 4
- MySQL 9
- Flyway
- Docker

## Pre-requisites
- Maven 3.9 or higher must be installed.
  ```bash
  mvn -version
  ```
- Docker & Docker compose must be installed.
  ```docker
  docker --version
  docker compose version
  ```
- Docker Network must be created.
  ```docker
  docker network create bookkeeping-net
  ```

## Getting Started
- Clone the repository.
  ```bash
  git clone https://github.com/adrian-justo/wallet-coding-challenge.git
  cd wallet-coding-challenge
  ```
- Create the package/JAR file
  ```bash
  mvn clean package -DskipTests
  ```
- Navigate to `deployment` directory and run command:
  ```bash
  cd wallet-coding-challenge/deployment
  docker compose up -d
  ```

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

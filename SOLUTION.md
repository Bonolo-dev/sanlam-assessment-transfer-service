# SOLUTION.md

## Overview
This document outlines the design decisions, implementation details, and limitations of the **Transfer Service** and **Ledger Service** projects.  
Due to time constraints, I was able to implement most of the requirements successfully, but a few features could not be completed.

---

## 1. Circuit Breaker (Theory and Intended Implementation)
I planned to use **Resilience4j** (or Spring Cloud Circuit Breaker as an alternative) for fault tolerance between the `Transfer Service` and the `Ledger Service`.

- **Why Circuit Breaker?**  
  To prevent cascading failures if the `Ledger Service` is down or slow. Instead of continuously retrying failed requests, the circuit breaker would “trip” and short-circuit calls temporarily.

- **Intended Setup**:
    - Add the `resilience4j-spring-boot3` dependency.
    - Annotate the `FeignClient` or `RestTemplate` calls in the `Transfer Service` with `@CircuitBreaker(name="ledgerService", fallbackMethod="fallbackLedger")`.
    - Define a fallback method in the service layer to return a safe response (e.g., queue the request for retry, or mark as `PENDING`).

- **Status**:  
  Not implemented due to time constraints. The architecture is ready for it, but code was not completed.

---

## 2. Authentication and Authorization
- **Approach**: I intended to use **Spring Security** with **JWT tokens** for securing both services.
- **Flow**:
    1. User logs in via an `Auth` endpoint, receiving a signed JWT.
    2. The JWT is included in the `Authorization: Bearer <token>` header for all API requests.
    3. The services validate the JWT signature and claims (e.g., roles, expiration).
    4. Fine-grained authorization would be enforced using Spring Security method-level annotations such as `@PreAuthorize("hasRole('ADMIN')")`.

This setup ensures that only authenticated and authorized users can initiate transfers or query ledger entries.

---

## 3. Continuous Integration (GitHub Actions)
A minimal GitHub Actions workflow was created in `.github/workflows/ci.yml`.  
It builds both services and runs tests on push or PR to `main`.

```yaml
name: CI Pipeline

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: "21"
          distribution: "temurin"

      - name: Build with Gradle
        run: ./gradlew clean build --no-daemon
```
---

## 4. Docker Compose
- I initially planned to orchestrate the services with docker-compose.yml so that both services, the database, and networks could be managed in one place.
- Docker Desktop started failing during setup, so I could not finish the docker-compose implementation.
- **Temporary fix**:
```bash

docker network create transfer-ledger-net

docker build -t ledger-service ./ledger
docker run -d --name ledger --network transfer-ledger-net -p 8081:8081 ledger-service

docker build -t transfer-service -f Dockerfile .
docker run -d --name transfer --network transfer-ledger-net -p 8080:8080 -e LEDGER_URL=http://ledger:8081 transfer-service

```

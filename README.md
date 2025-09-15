# Transfer Service

This is the **Transfer Service** microservice implemented in Java with Spring Boot. It manages money transfers and batch processes for transfers.

## Prerequisites

* Java 21
* Maven or Gradle
* IntelliJ IDEA
* Docker Desktop
* Postman (optional)
---

## Setup in IntelliJ IDE

1. Open IntelliJ IDEA.
2. Click **File → New → Project from Existing Sources…**
3. Select `transfer-service` folder → Choose Maven/Gradle.
4. Ensure **Java SDK 21** is selected.
5. Build the project:

   ```bash
   ./mvnw clean install    # Maven
   ./gradlew build         # Gradle
   ```

---

## Running Locally

1. Open `TransferApplication.java`.
2. Right-click → **Run 'TransferApplication'**.
3. Service will start at `http://localhost:8080`.

---

## Docker Setup

---

## Running with Docker

1. Build and run in root directory:

```bash
docker build -t transfer-service ./transfer-service -f Dockerfile .
docker run -p 8080:8080 transfer-service
```

2. Service will be available at `http://localhost:8080`.

---

## Swagger UI

* `http://localhost:8080/swagger-ui/index.html`

---

## H2 Console

* `http://localhost:8080/h2-console` - Please reference `application.yml` for credentials.

---

## Postman Collection

* Postman collection available in `postman` folder.

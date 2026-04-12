# VerifNow Spring Boot – Validation Example

> **Public example project** demonstrating how to integrate the [VerifNow](https://verifnow.io) Spring SDK (`io.verifnow:verifnow-spring-boot-starter:2.1.0`) into a Spring Boot application.

Also available in French: see [README.fr.md](README.fr.md)

| Stack | Version |
|-------|---------|
| Java | 21 |
| Spring Boot | 3.5.13 |
| VerifNow SDK | 2.1.0 |
| Build | Maven |

---

## What this project demonstrates

This sample application covers the three main integration patterns when using the VerifNow Spring SDK:

### 1. Annotation-based validation on DTOs

The `UserDto` class uses VerifNow annotations to validate inputs automatically via the standard Bean Validation (`@Valid`) mechanism — no manual API calls needed:

```java
public class UserDto {

  @VerifNowEmail(maxRiskScore = 30)   // rejects emails with a risk score above 30
  private String email;

  @VerifNowPhone
  private String phone;
}
```

### 2. Global error handling with structured JSON responses

The `ValidationExceptionHandler` (`@RestControllerAdvice`) intercepts validation failures and returns a clean, structured JSON error body instead of the default Spring 400 response:

```json
{
  "timestamp": "2026-04-12T14:30:00.000Z",
  "status": 400,
  "error": "Validation failed",
  "fieldErrors": {
    "email": "Invalid email address"
  }
}
```

### 3. Custom validation error messages

Override the SDK's default messages by placing a `ValidationMessages.properties` file in `src/main/resources/`:

```properties
validation.verifnow.email.invalid=Adresse email invalide
validation.verifnow.phone.invalid=Numéro de téléphone invalide
```

Locale-specific variants are also supported: `ValidationMessages_fr.properties`, `ValidationMessages_en.properties`, etc.

---

## Quick start

### Prerequisites

- JDK 21 installed and configured in your IDE
- Maven 3.9+
- A VerifNow API key ([get one here](https://verifnow.io))

### 1 – Configure your API key

The application reads its configuration from environment variables. Export them in your shell or configure them in your IDE run configuration:

```bash
export VERIFNOW_API_KEY="<your_verifnow_api_key>"
# Optional – defaults to https://api.verifnow.io
export VERIFNOW_BASE_URL="https://api.verifnow.io"
```

> **Note:** Spring Boot does not auto-load a `.env` file. If you prefer `.env`, make sure your shell (e.g. direnv) or IDE loads it.

### 2 – Build and run

```bash
./mvnw clean package
java -jar target/validation-demo-1.0.0.jar
```

The service starts on port **8090** by default.

### 3 – Try it

**Valid request:**

```bash
curl -s -X POST http://localhost:8090/users \
  -H "Content-Type: application/json" \
  -d '{"email":"john.doe@example.com","phone":"+14155552671"}'
```

→ `200 OK` – `User is valid and accepted!`

**Invalid request (bad email):**

```bash
curl -s -X POST http://localhost:8090/users \
  -H "Content-Type: application/json" \
  -d '{"email":"not-an-email","phone":"+14155552671"}'
```

→ `400 Bad Request` with structured error body:

```json
{
  "timestamp": "2026-04-12T14:30:00.000Z",
  "status": 400,
  "error": "Validation failed",
  "fieldErrors": {
    "email": "Invalid email address"
  }
}
```

---

## SDK configuration

The starter is declared in `pom.xml`:

```xml
<dependency>
    <groupId>io.verifnow</groupId>
    <artifactId>verifnow-spring-boot-starter</artifactId>
    <version>2.1.0</version>
</dependency>
```

Runtime configuration in `src/main/resources/application.yml` (supports environment variable overrides):

| Property | Env variable | Default |
|----------|-------------|---------|
| `verifnow.api.base-url` | `VERIFNOW_BASE_URL` | `https://api.verifnow.io` |
| `verifnow.api.api-key` | `VERIFNOW_API_KEY` | *(none – required)* |

You typically only need to set `VERIFNOW_API_KEY`.

---

## Project structure

```
src/main/java/com/example/
├── DemoApplication.java              # Spring Boot entry point
├── controller/
│   └── UserController.java           # POST /users endpoint with @Valid
├── dto/
│   └── UserDto.java                  # @VerifNowEmail, @VerifNowPhone
└── exception/
    └── ValidationExceptionHandler.java  # Global error handler (@RestControllerAdvice)

src/main/resources/
├── application.yml                   # SDK & server config
└── ValidationMessages.properties     # Custom error message overrides

postman/                              # Postman collection & environment
```

---

## Run from IntelliJ IDEA

1. Set **Project SDK** to JDK 21.
2. Align the Maven importer and Runner JDK to the same JDK 21.
3. Re-import Maven, then Run `DemoApplication`.

If you see an error like:

```
java.lang.ExceptionInInitializerError com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

it usually means the IDE compiled with a different JDK than the one used by Maven. Align everything to JDK 21 and rebuild.

---

## Postman

Use the files in the `postman/` directory:

- Import `local.postman_environment.json` and verify `baseUrl` is `http://localhost:8090`.
- Import `UserController.postman_collection.json` — it includes three ready-made requests:
  - **Create User – Valid** (expects 200)
  - **Create User – Invalid Email** (expects 400)
  - **Create User – Invalid Phone** (expects 400)

---

## Security

- No secrets are committed. Provide your API key via environment variables.
- This example does not configure authentication. Add Spring Security as needed for your use case.

## License

MIT – see [LICENSE](LICENSE).

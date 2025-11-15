# VerifNow Spring Boot Validation Example

This repository is a minimal, production‑ready example showing how to integrate the VerifNow SDK for Spring Boot (`io.verifnow:verifnow-spring-boot-starter`) to validate fields like email and phone using simple annotations.

- Framework: Spring Boot 3.5.7
- Java: 21
- Build: Maven

Also available in French: see README.fr.md

## What it does
The DTO `UserDto` uses VerifNow annotations to validate inputs automatically:
- `@VerifNowEmail`
- `@VerifNowPhone`

Requests hitting `POST /users` will be validated before your controller code runs. If invalid, Spring returns a 400 with a standard validation error body.

## Quick start

1) Prerequisites
- JDK 21 installed and configured in your IDE
- Maven 3.9+

2) Configure your API key (required)

The application reads its configuration from environment variables. Export them in your shell or configure them in your IDE run configuration:

```
export VERIFNOW_API_KEY="<your_verifnow_api_key>"
# Optional, defaults to https://api.verifnow.io
export VERIFNOW_BASE_URL="https://api.verifnow.io"
```

Note: Spring Boot does not auto-load a `.env` file. If you prefer using `.env`, make sure your shell (e.g., direnv) or IDE loads it. A sample `.env.example` is provided.

3) Build and run

```
./mvnw clean package
java -jar target/validation-demo-1.0.0.jar
```

The service starts on port 8090 by default.

4) Try it
- POST http://localhost:8090/users
- Body:
```
{
  "email": "john.doe@example.com",
  "phone": "+14155552671"
}
```
- Expected: 200 OK with message `User is valid and accepted!`
- Invalid data returns 400 with validation errors.

Quick curl test:

```
curl -i \
  -H "Content-Type: application/json" \
  -d '{"email":"john.doe@example.com","phone":"+14155552671"}' \
  http://localhost:8090/users
```

## How the SDK is configured
The starter is declared in `pom.xml`:

- Group: `io.verifnow`
- Artifact: `verifnow-spring-boot-starter`
- Version: `1.0.0`

Runtime configuration lives in `src/main/resources/application.yml` and supports environment overrides:

- `verifnow.api.base-url` (env: `VERIFNOW_BASE_URL`, default `https://api.verifnow.io`)
- `verifnow.api.api-key` (env: `VERIFNOW_API_KEY`, no default; required)

You typically only need to set `VERIFNOW_API_KEY`.

## Project structure
- `DemoApplication` – Spring Boot main class
- `UserController` – exposes `POST /users`
- `UserDto` – demonstrates `@VerifNowEmail` and `@VerifNowPhone`
- `application.yml` – SDK configuration (env‑first)
- `postman/` – environment and collection to test the endpoint

## Run from IntelliJ IDEA
1) Set Project SDK to JDK 21.
2) Set Maven importer and Runner JDK to the same JDK 21.
3) Reimport Maven, then Run `DemoApplication`.

If you see an error like:
```
java.lang.ExceptionInInitializerError com.sun.tools.javac.code.TypeTag :: UNKNOWN
```
it usually means the IDE compiled with a different JDK than the one used by Maven. Align everything to JDK 21 and rebuild.

## Postman
Use the files in the `postman/` directory:
- Import `local.postman_environment.json` and set `baseUrl` to `http://localhost:8090` if not already.
- Import `UserController.postman_collection.json` to exercise the endpoint.

## Security
- Secrets are not committed. Provide your API key via env vars.
- The example disables no security; add Spring Security if needed for your use case.

## License
This example is provided for demonstration purposes. You can adapt it to your needs.

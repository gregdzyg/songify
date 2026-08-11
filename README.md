# Songify

Songify is a learning REST API for managing songs, albums, artists and genres. The project was developed step by step while learning Spring Boot and later extended with database migrations, automated tests and stateless JWT security.

It demonstrates a complete backend flow: HTTP request validation, domain logic, JPA persistence, PostgreSQL migrations, authentication, role-based authorization and integration testing with a real database container.

## Main features

- CRUD operations for songs and related music catalogue entities,
- relationships between songs, genres, albums and artists,
- pagination and request validation,
- PostgreSQL schema managed by Flyway,
- optimistic locking for persisted entities,
- user registration with BCrypt password hashing,
- username/password authentication and signed JWT access tokens,
- role-based access for `USER` and `ADMIN`,
- OpenAPI documentation with Bearer token support,
- unit tests and a full Testcontainers integration happy path.

## Tech stack

- Java 17
- Spring Boot 3
- Spring Web and Validation
- Spring Data JPA
- Spring Security and OAuth2 Resource Server JWT support
- PostgreSQL and Flyway
- Maven
- JUnit 5, AssertJ, MockMvc and Testcontainers
- Docker Compose

## Security flow

1. `POST /auth/register` creates a `USER` account and stores only its BCrypt password hash.
2. `POST /auth/token` delegates username/password verification to Spring Security's `AuthenticationManager`.
3. Songify issues an HS256-signed JWT containing the username, roles and expiration time.
4. The client sends the token in `Authorization: Bearer <token>`.
5. Spring Security validates the signature and expiration, rebuilds the authorities and checks access rules.

| Access | Endpoints |
| --- | --- |
| Public | `/auth/register`, `/auth/token`, Swagger/OpenAPI |
| `USER` or `ADMIN` | `GET` catalogue endpoints |
| `ADMIN` | `POST`, `PUT`, `PATCH` and `DELETE` catalogue endpoints |

The API uses stateless Bearer tokens, so server-side sessions are disabled. CSRF protection is disabled for this API style, while CORS origins are configured explicitly.

## Running locally

Requirements: Java 17 and Docker.

Start PostgreSQL:

```bash
docker compose up -d db
```

Configure a JWT signing secret containing at least 32 characters. Optionally provide the first administrator account:

```bash
export SONGIFY_JWT_SECRET="replace-with-a-long-random-development-secret"
export SONGIFY_ADMIN_USERNAME="admin"
export SONGIFY_ADMIN_PASSWORD="change-this-admin-password"
```

Run the application:

```bash
./mvnw spring-boot:run
```

Swagger UI is available at `http://localhost:8080/swagger-ui/index.html`.

## Authentication examples

Register a regular user:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"listener","password":"listener-password"}'
```

Request a token:

```bash
curl -X POST http://localhost:8080/auth/token \
  -H "Content-Type: application/json" \
  -d '{"username":"listener","password":"listener-password"}'
```

Use the returned `accessToken`:

```bash
curl http://localhost:8080/songs \
  -H "Authorization: Bearer <accessToken>"
```

## Tests

Docker must be running because the integration test starts PostgreSQL through Testcontainers.

```bash
./mvnw test
```

The integration happy path verifies migrations, registration, login, duplicate-user handling, `401`, `403`, role-based access and the complete song CRUD flow.

## Project status

Songify is intentionally a tutorial project rather than a production music platform. The code preserves the visible progression of learning while the final version provides a coherent and demonstrable backend. Possible next steps include refresh tokens, account management and deployment-specific HTTPS configuration.

## Author

Grzegorz Dżyg

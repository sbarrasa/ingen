# User API MongoDB POC

Java 21 REST API POC using Spring Boot, MongoDB, records and lambdas.

## Requirements

- Java 21
- Maven
- MongoDB running locally

## MongoDB configuration

The application connects to:

```text
mongodb://localhost:27017/local
```

Collection:

```text
users
```

## Run

```bash
mvn spring-boot:run
```

## Endpoints

### Health check

```bash
curl http://localhost:8080/hello
```

### Create user

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Sergio","registrationDate":"2026-05-11"}'
```

### Get user by id

```bash
curl http://localhost:8080/users/1
```

### Get all users

```bash
curl http://localhost:8080/users
```

### Update user by id

```bash
curl -X PUT http://localhost:8080/users/1 \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Sergio Updated","registrationDate":"2026-05-11"}'
```

### Delete user by id

```bash
curl -X DELETE http://localhost:8080/users/1
```

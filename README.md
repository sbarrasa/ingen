# User API MongoDB POC

Java 21 REST API POC using Spring Boot, MongoDB, records and lambdas.

## Infrastructure (Local Development)

The project includes a `compose.yaml` file to run the required infrastructure (MongoDB and ActiveMQ) using Podman or Docker.

### Start services

```bash
podman compose up -d
```

This will start:
- **MongoDB**: `mongodb://localhost:27017/local`

*(Nota: JMS ahora corre de forma **embebida** dentro de la aplicación, por lo que no es necesario levantarlo externamente).*

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

## JMS Integration

The application produces messages to the queue `user.updates.queue` on every create, update, or delete operation.

- **Producer**: `JmsProducer.java` sends `UserEvent` objects as JSON.
- **Consumer**: `OrchestratorConsumer.java` listens to the same queue and logs the received events.

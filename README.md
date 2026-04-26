# Taskboard API

Pure Java CRUD REST API using JDK `HttpServer` with minimal libraries.

## Names

- `groupId`: `com.cgp`
- `artifactId`: `taskboard-api`
- base package: `com.cgp.taskboard.api`

## Stack

- Java 17
- JDK built-in `com.sun.net.httpserver.HttpServer`
- Jackson (`jackson-databind`) for JSON serialization/deserialization

## Build and Run

```bash
mvn clean package
java -jar target/taskboard-api-0.0.1-SNAPSHOT.jar
```

Server runs at `http://localhost:8080`.

## OpenAPI and Docs

- OpenAPI YAML: `http://localhost:8080/openapi.yaml`
- Human docs page: `http://localhost:8080/docs`

## Sample cURL Commands

```bash
curl -X POST "http://localhost:8080/api/v1/tasks" \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries","description":"Milk, eggs"}'
```

```bash
curl "http://localhost:8080/api/v1/tasks"
```

```bash
curl "http://localhost:8080/api/v1/tasks/{id}"
```

```bash
curl -X PUT "http://localhost:8080/api/v1/tasks/{id}" \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries and bread","description":"Milk, eggs, bread","completed":true}'
```

```bash
curl -X DELETE "http://localhost:8080/api/v1/tasks/{id}"
```

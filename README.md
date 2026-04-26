# Taskboard API

Pure Java CRUD REST API using JDK `HttpServer` with no web framework.

## Stack

- Java 21
- JDK built-in `com.sun.net.httpserver.HttpServer` with virtual threads
- Jackson for JSON serialization

## Build and Run

```bash
./start.sh
```

The script builds the fat JAR on first run (skips the build if the JAR already exists), then starts the server.

To force a rebuild:

```bash
mvn clean package -DskipTests && ./start.sh
```

Server listens on `http://localhost:8080` by default. Override with the `PORT` environment variable:

```bash
PORT=9090 ./start.sh
```

## API

Base path: `/api/v1/tasks`

| Method | Path                  | Description      |
|--------|-----------------------|------------------|
| GET    | `/api/v1/tasks`       | List all tasks   |
| POST   | `/api/v1/tasks`       | Create a task    |
| GET    | `/api/v1/tasks/{id}`  | Get a task       |
| PUT    | `/api/v1/tasks/{id}`  | Replace a task   |
| DELETE | `/api/v1/tasks/{id}`  | Delete a task    |

## Sample cURL Commands

```bash
# Create
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries","description":"Milk, eggs"}'

# List all
curl http://localhost:8080/api/v1/tasks

# Get one
curl http://localhost:8080/api/v1/tasks/{id}

# Update
curl -X PUT http://localhost:8080/api/v1/tasks/{id} \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries and bread","description":"Milk, eggs, bread","completed":true}'

# Delete
curl -X DELETE http://localhost:8080/api/v1/tasks/{id}
```

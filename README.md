# Notification Service

Listens for order events on RabbitMQ and logs a mock customer notification. No database — pure message consumption and logging.

Part of a three-service system: [Product Service](https://github.com/rKrishan99/csci44092-product-service) · [Order Service](https://github.com/rKrishan99/csci44092-order-service) · **Notification Service** (this repo).

## Stack

- Java 17, Spring Boot 3.5.15
- Spring AMQP (RabbitMQ)
- JUnit 5 + Mockito + spring-rabbit-test

## Workflow

1. Order Service publishes an `OrderEvent` (`orderId`, `customerId`, `timestamp`) to `order.queue` after saving an order.
2. This service's `@RabbitListener` consumes the message.
3. It logs a mock notification simulating what a real email/SMS/push system would send.

## Running locally

Requires RabbitMQ reachable at `localhost:5672` — provided by the shared `docker-compose.yml` at the root of the three-repo workspace:

```bash
docker compose up -d rabbitmq
```

Then start the service:

```bash
./mvnw spring-boot:run
```

The app starts on **port 8080**. It has no REST endpoints of its own — its job is purely to consume messages and log them, so a running process with an active RabbitMQ consumer connection *is* the expected behavior.

## Configuration

Environment-variable driven — nothing hardcoded (see `src/main/resources/application.properties`):

| Variable | Default | Purpose |
|---|---|---|
| `RABBITMQ_HOST` | `localhost` | RabbitMQ host |
| `RABBITMQ_PORT` | `5672` | RabbitMQ port |
| `RABBITMQ_USER` | `guest` | RabbitMQ user |
| `RABBITMQ_PASS` | `guest` | RabbitMQ password |

Queue consumed: `order.queue` (configurable via `rabbitmq.queue.name`).

## Seeing it work

1. Start Product Service, Order Service, and this service.
2. Create a product, then create an order (see the [Order Service README](https://github.com/rKrishan99/csci44092-order-service) for exact requests).
3. Watch this service's console — you should see:

```
=== NOTIFICATION SERVICE: Order Event Received ===
Order ID   : 1
Customer ID: CUST-100
Timestamp  : 2026-07-05T20:40:22.872029100
--------------------------------------------------
[MOCK NOTIFICATION] Dear Customer 'CUST-100', your order #1 has been placed successfully at 2026-07-05T20:40:22.872029100.
=== Notification processed successfully ===
```

You can also verify delivery via the RabbitMQ management UI at `http://localhost:15672` (`guest` / `guest`) — under **Queues and Streams → order.queue**, the message rate graph shows a publish/deliver spike at the moment the order was created, and settles back to 0 ready / 0 unacked once consumed.

## Tests

```bash
./mvnw test
```

Covers the RabbitMQ consumer logic (`NotificationConsumerTest`), using an in-memory/mocked message to verify the listener processes an `OrderEvent` correctly without needing a live broker.

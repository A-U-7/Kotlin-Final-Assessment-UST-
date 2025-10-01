# Multi-Module Microservices Project with Spring Boot

This project demonstrates a microservices architecture using Spring Cloud, Eureka Service Discovery, API Gateway, and more.

## Project Structure

- **service-registry**: Eureka Server for service discovery (port 8761)
- **api-gateway**: Spring Cloud Gateway (port 8085)
- **order-service**: Order management service (port 8080)
- **user-service**: User management service (port 8081)

## Prerequisites

- Java 21 or later
- Maven 3.8.4 or later
- Docker (for containerization)
- H2 Database (embedded)
- Kafka (for event-driven communication)

## Getting Started

### 1. Start Service Registry (Eureka Server)

```bash
cd service-registry
mvn spring-boot:run
```

Access Eureka Dashboard: http://localhost:8761

### 2. Start API Gateway

```bash
cd api-gateway
mvn spring-boot:run
```

### 3. Start Order Service

```bash
cd order-service
mvn spring-boot:run
```

### 4. Start User Service

```bash
cd user-service
mvn spring-boot:run
```

## Docker Support

You can also run the services using Docker:

```bash
docker-compose up --build
```

## API Documentation

### Service Registry (Eureka)

- **Dashboard**: http://localhost:8761
- **Health Check**: 
  ```bash
  curl -X GET http://localhost:8761/actuator/health
  ```

### API Documentation

API documentation is available at:
- API Gateway: http://localhost:8085/swagger-ui.html
- Order Service: http://localhost:8080/swagger-ui.html
- User Service: http://localhost:8081/swagger-ui.html

### API Gateway

- **Port**: 8085
- **Base URL**: http://localhost:8085
- **Health Check**: 
  ```bash
  curl -X GET http://localhost:8085/actuator/health
  ```
- **List Routes**:
  ```bash
  curl -X GET http://localhost:8085/actuator/gateway/routes
  ```

### Order Service

- **Port**: 8080
- **Base URL**: http://localhost:8080
  (or through gateway: http://localhost:8085/order-service)

#### Endpoints

```http
POST /orders
Content-Type: application/json

{
  "userId": 1,
  "productId": 101,
  "quantity": 2,
  "totalAmount": 199.98
}
```

```http
GET /orders/{id}
```

```http
GET /orders
```

#### cURL Examples

Create Order:
```bash
curl -X POST http://localhost:8085/order-service/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 101,
    "quantity": 2,
    "totalAmount": 199.98
  }'
```

Get Order:
```bash
curl -X GET http://localhost:8085/order-service/orders/1
```

### User Service

- **Port**: 8081
- **Base URL**: http://localhost:8081
  (or through gateway: http://localhost:8085/user-service)

#### Endpoints

```http
POST /users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com"
}
```

```http
GET /users/{id}
```

```http
GET /users
```

#### cURL Examples

Create User:
```bash
curl -X POST http://localhost:8085/user-service/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com"
  }'
```

Get User:
```bash
curl -X GET http://localhost:8085/user-service/users/1
```

## Monitoring

### Actuator Endpoints

All services expose actuator endpoints for monitoring:

```bash
# Health check
curl -X GET http://localhost:8080/actuator/health

# Metrics
curl -X GET http://localhost:8080/actuator/metrics

# Prometheus metrics
curl -X GET http://localhost:8080/actuator/prometheus

# Circuit breakers status
curl -X GET http://localhost:8080/actuator/circuitbreakers
```

## Kafka Integration

The system uses Kafka for event-driven communication between services.

**Topics:**
- `order-events`
- `user-events`

## Database

- **H2 Console**: http://localhost:8080/h2-console (for Order Service)
  - JDBC URL: jdbc:h2:mem:orderdb
  - Username: sa
  - Password: password

## Build and Run

To build all services:

```bash
mvn clean install
```

To run a specific service:

```bash
cd <service-directory>
mvn spring-boot:run
```

## Dependencies

- Spring Boot 3.5.6
- Spring Cloud 2023.0.0
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Spring Data JPA
- H2 Database
- Kafka
- Resilience4j
- Micrometer & Prometheus

---

---

## 🚀 Project Author

<div align="center">

### Amit Upadhyay

[![GitHub](https://img.shields.io/badge/View%20on%20GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/A-U-7)

</div>

<div align="center">

[![LinkedIn](https://img.shields.io/badge/Connect%20on%20LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/your-profile)
[![Twitter](https://img.shields.io/badge/Follow%20on%20Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/your-handle)

</div>

  

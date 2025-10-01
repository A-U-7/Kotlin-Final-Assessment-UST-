# Docker Setup for Microservices Project

This document provides detailed information about the Docker setup for the microservices project, including Kafka integration.

## Table of Contents
- [Architecture Overview](#architecture-overview)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Service Details](#service-details)
- [Kafka Integration](#kafka-integration)
- [Common Commands](#common-commands)
- [Troubleshooting](#troubleshooting)
- [Useful URLs](#useful-urls)

## Architecture Overview

The project uses the following Docker containers:
- **Zookeeper**: For Kafka cluster coordination
- **Kafka**: Message broker for event-driven architecture
- **Service Registry (Eureka)**: For service discovery
- **API Gateway**: Single entry point for all services
- **Order Service**: Handles order processing
- **User Service**: Manages user data
- **Kafka UI**: Web interface for monitoring Kafka

## Prerequisites

- Docker 20.10.0 or later
- Docker Compose 1.29.0 or later
- At least 8GB RAM (16GB recommended)
- At least 4 CPU cores

## Getting Started

### 1. Build and Start Services

```bash
# Make the build script executable (Linux/Mac)
chmod +x docker-build.sh

# Build and start all services
./docker-build.sh
```

### 2. Verify Services

Wait for all services to start (2-3 minutes). You can check the status using:

```bash
docker-compose ps
```

## Service Details

| Service | Port | Description |
|---------|------|-------------|
| Zookeeper | 2181 | Kafka dependency |
| Kafka | 9092, 29092 | Message broker |
| Service Registry | 8761 | Eureka dashboard |
| API Gateway | 8085 | Main entry point |
| Order Service | 8080 | Order management |
| User Service | 8081 | User management |
| Kafka UI | 8082 | Web interface for Kafka |

## Kafka Integration

### Topics
- `order-events`
- `user-events`

### Kafka Commands

```bash
# List all topics
docker exec -it kafka kafka-topics --list --bootstrap-server localhost:9092

# Create a topic
docker exec -it kafka kafka-topics --create --topic test-topic --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

# Produce messages
docker exec -it kafka kafka-console-producer --topic test-topic --bootstrap-server localhost:9092

# Consume messages
docker exec -it kafka kafka-console-consumer --topic test-topic --from-beginning --bootstrap-server localhost:9092
```

## Common Commands

### Start Services
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose down
```

### View Logs
```bash
# View logs for all services
docker-compose logs -f

# View logs for a specific service
docker-compose logs -f service-name
```

### Rebuild a Specific Service
```bash
docker-compose up -d --build service-name
```

## Troubleshooting

### Port Conflicts
If you encounter port conflicts, check which process is using the port:
```bash
# Linux/Mac
lsof -i :PORT_NUMBER

# Windows
netstat -ano | findstr :PORT_NUMBER
```

### Container Issues
```bash
# Check container status
docker ps -a

# View container logs
docker logs CONTAINER_ID

# Enter container shell
docker exec -it CONTAINER_ID /bin/bash
```

## Useful URLs

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8085
- **Order Service**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Kafka UI**: http://localhost:8082
- **Order Service H2 Console**: http://localhost:8080/h2-console
- **User Service H2 Console**: http://localhost:8081/h2-console

## Environment Variables

Key environment variables used in the services:

- `SPRING_PROFILES_ACTIVE`: Active Spring profile (default: docker)
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`: Eureka server URL
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka broker address
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_H2_CONSOLE_ENABLED`: Enable/disable H2 console

## Cleanup

To remove all containers, networks, and volumes:

```bash
docker-compose down -v --rmi all --remove-orphans
```

## Notes

- All services are connected to a Docker network called `microservices-network`
- Kafka is configured for both internal and external access
- H2 databases are used for development (data is lost when containers are removed)
- For production, consider using persistent volumes for databases and Kafka data

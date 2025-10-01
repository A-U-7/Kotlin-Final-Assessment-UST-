#!/bin/bash

# Build all services
echo "Building all services..."
docker-compose build

echo "Starting all services..."
docker-compose up -d

echo "Waiting for services to start..."
sleep 30

echo "Services are up and running!"
echo ""
echo "Access the following services:"
echo "- Eureka Dashboard: http://localhost:8761"
echo "- API Gateway: http://localhost:8085"
echo "- Order Service: http://localhost:8080"
echo "- User Service: http://localhost:8081"
echo "- Kafka UI: http://localhost:8082"
echo ""
echo "To stop all services, run: docker-compose down"

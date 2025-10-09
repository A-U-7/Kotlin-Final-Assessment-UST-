# Mall Parking System

A comprehensive Spring Boot application built with Kotlin for managing mall parking operations. This system provides complete parking management functionality including user management, vehicle registration, parking spot allocation, and parking session tracking with automated billing.

## 🚀 Features

### Core Functionality
- **User Management**: Register and manage customers, staff, and administrators
- **Vehicle Registration**: Register vehicles with license plate validation
- **Parking Spot Management**: Manage different types of parking spots (Standard, Compact, Handicapped, Electric, Premium)
- **Parking Sessions**: Track parking sessions with automatic entry/exit time recording
- **Automated Billing**: Calculate parking fees based on duration and spot type
- **Real-time Analytics**: Monitor parking occupancy and generate revenue reports

### Technical Features
- **RESTful API**: Complete REST API with 38+ endpoints
- **Exception Handling**: Comprehensive error handling with custom exceptions
- **Database Integration**: JPA/Hibernate with H2 in-memory database
- **Logging**: Detailed logging for debugging and monitoring
- **CORS Support**: Cross-origin resource sharing enabled

## 🏗️ Architecture

### Project Structure
```
MallParkingSystem/
├── src/main/kotlin/com/ust_internal/mallparkingsystem/
│   ├── controller/          # REST Controllers
│   ├── dto/                # Data Transfer Objects
│   ├── entity/             # JPA Entities
│   ├── exception/          # Custom Exceptions & Global Handler
│   ├── repository/         # Data Access Layer
│   └── service/            # Business Logic Layer
├── src/main/resources/
│   └── application.properties
└── build.gradle.kts
```

### Technology Stack
- **Framework**: Spring Boot 3.5.6
- **Language**: Kotlin 1.9.25
- **Database**: H2 Database (In-memory)
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Gradle with Kotlin DSL
- **Java Version**: 21

## 📡 API Endpoints

### User Management (`/api/users`)
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/type/{userType}` - Get users by type
- `GET /api/users/active` - Get active users

### Vehicle Management (`/api/vehicles`)
- `GET /api/vehicles` - Get all vehicles
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `GET /api/vehicles/license/{licensePlate}` - Get vehicle by license plate
- `POST /api/vehicles` - Register new vehicle
- `PUT /api/vehicles/{id}` - Update vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle
- `GET /api/vehicles/owner/{ownerId}` - Get vehicles by owner
- `GET /api/vehicles/owner/{ownerId}/active` - Get active vehicles by owner

### Parking Spot Management (`/api/parking-spots`)
- `GET /api/parking-spots` - Get all parking spots
- `GET /api/parking-spots/{id}` - Get parking spot by ID
- `GET /api/parking-spots/number/{spotNumber}` - Get parking spot by number
- `POST /api/parking-spots` - Create new parking spot
- `PUT /api/parking-spots/{id}` - Update parking spot
- `DELETE /api/parking-spots/{id}` - Delete parking spot
- `GET /api/parking-spots/available` - Get available spots
- `GET /api/parking-spots/floor/{floor}` - Get spots by floor
- `GET /api/parking-spots/section/{section}` - Get spots by section
- `GET /api/parking-spots/active` - Get active spots
- `GET /api/parking-spots/summary` - Get parking summary

### Parking Records (`/api/parking-records`)
- `GET /api/parking-records` - Get all parking records
- `GET /api/parking-records/{id}` - Get parking record by ID
- `POST /api/parking-records/start` - Start parking session
- `POST /api/parking-records/{id}/end` - End parking session
- `GET /api/parking-records/active` - Get active sessions
- `GET /api/parking-records/vehicle/{vehicleId}` - Get records by vehicle
- `GET /api/parking-records/user/{userId}` - Get records by user
- `GET /api/parking-records/spot/{spotId}` - Get records by spot
- `GET /api/parking-records/summary` - Get parking summary with date range

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Gradle (or use included Gradle wrapper)

### Running the Application

1. **Navigate to project directory:**
   ```bash
   cd MallParkingSystem
   ```

2. **Build the project:**
   ```bash
   ./gradlew build
   ```

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```
   Or run the JAR directly:
   ```bash
   java -jar build/libs/MallParkingSystem-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application:**
   - API Base URL: `http://localhost:9090`
   - H2 Database Console: `http://localhost:9090/h2-console`

### Database Configuration

The application uses H2 in-memory database with the following settings:
- **JDBC URL**: `jdbc:h2:mem:mallparkingdb`
- **Username**: `sa`
- **Password**: (empty)

Database tables are automatically created on startup due to `spring.jpa.hibernate.ddl-auto=update`.

## 📊 Data Models

### User Entity
```kotlin
data class User(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val userType: UserType, // CUSTOMER, ADMIN, STAFF
    val isActive: Boolean
)
```

### Vehicle Entity
```kotlin
data class Vehicle(
    val id: Long?,
    val licensePlate: String,
    val vehicleType: VehicleType, // CAR, MOTORCYCLE, TRUCK, VAN
    val make: String,
    val model: String,
    val color: String,
    val owner: User
)
```

### Parking Spot Entity
```kotlin
data class ParkingSpot(
    val id: Long?,
    val spotNumber: String,
    val floor: String,
    val section: String,
    val spotType: ParkingSpotType, // STANDARD, COMPACT, HANDICAPPED, ELECTRIC, PREMIUM
    val status: ParkingSpotStatus, // AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
    val hourlyRate: BigDecimal
)
```

### Parking Record Entity
```kotlin
data class ParkingRecord(
    val id: Long?,
    val vehicle: Vehicle,
    val parkingSpot: ParkingSpot,
    val user: User,
    val entryTime: LocalDateTime,
    val exitTime: LocalDateTime?,
    val status: ParkingStatus, // ACTIVE, COMPLETED, CANCELLED, OVERDUE
    val totalAmount: BigDecimal?,
    val durationInHours: Double?
)
```

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=9090

# Database Configuration
spring.datasource.url=jdbc:h2:mem:mallparkingdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Logging
logging.level.com.ust_internal.mallparkingsystem=DEBUG
```

## 🧪 Testing the API

### Example API Calls

1. **Create a User:**
   ```bash
   curl -X POST http://localhost:9090/api/users \
     -H "Content-Type: application/json" \
     -d '{
       "firstName": "John",
       "lastName": "Doe",
       "email": "john.doe@example.com",
       "phoneNumber": "1234567890",
       "userType": "CUSTOMER"
     }'
   ```

2. **Register a Vehicle:**
   ```bash
   curl -X POST http://localhost:9090/api/vehicles \
     -H "Content-Type: application/json" \
     -d '{
       "licensePlate": "ABC123",
       "vehicleType": "CAR",
       "make": "Toyota",
       "model": "Camry",
       "color": "Blue",
       "ownerId": 1
     }'
   ```

3. **Create a Parking Spot:**
   ```bash
   curl -X POST http://localhost:9090/api/parking-spots \
     -H "Content-Type: application/json" \
     -d '{
       "spotNumber": "A1-001",
       "floor": "A1",
       "section": "A",
       "spotType": "STANDARD",
       "hourlyRate": 5.00
     }'
   ```

4. **Start Parking Session:**
   ```bash
   curl -X POST http://localhost:9090/api/parking-records/start \
     -H "Content-Type: application/json" \
     -d '{
       "vehicleId": 1,
       "parkingSpotId": 1,
       "userId": 1
     }'
   ```

5. **End Parking Session:**
   ```bash
   curl -X POST http://localhost:9090/api/parking-records/1/end
   ```

## 📈 Monitoring

- **Application Health**: Access `http://localhost:9090/actuator/health`
- **Database Console**: Access `http://localhost:9090/h2-console` for database inspection
- **Application Logs**: Check console output for detailed logging information

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if necessary
5. Submit a pull request

## 📝 License

This project is part of an internal UST assessment system.

---

**Note**: This application is designed for mall parking management and includes comprehensive features for real-world parking operations.

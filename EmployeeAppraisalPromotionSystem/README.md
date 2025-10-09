# Employee Appraisal and Promotion System

A comprehensive Spring Boot application built with Kotlin for managing employee appraisals and promotion processes. This system automates the evaluation of employee performance and determines promotion eligibility based on years of experience and performance ratings.

## 🚀 Features

### Core Functionality
- **Employee Management**: Register and manage employees with different roles (Employee/Manager)
- **Performance Tracking**: Monitor employee ratings and years of experience
- **Automated Promotion Logic**: Smart eligibility checking based on configurable criteria
- **Inheritance-based Architecture**: Manager extends Employee with additional responsibilities
- **Real-time Status Updates**: Observable promotion status changes with notifications
- **Bulk Promotion Processing**: Calculate promotions for all eligible employees

### Technical Features
- **RESTful API**: Complete REST API with comprehensive endpoints
- **JPA Inheritance**: Single table inheritance strategy for Employee-Manager relationship
- **Exception Handling**: Robust error handling and validation
- **Database Integration**: JPA/Hibernate with H2 in-memory database
- **Observable Properties**: Kotlin delegation for automatic status change tracking
- **CORS Support**: Cross-origin resource sharing enabled

## 🏗️ Architecture

### Project Structure
```
EmployeeAppraisalPromotionSystem/
├── src/main/kotlin/com/ust_internal/employeeappraisalpromotionsystem/
│   ├── controller/          # REST Controllers
│   ├── dto/                # Data Transfer Objects
│   ├── entity/             # JPA Entities (Employee, Manager)
│   ├── exception/          # Custom Exception Handling
│   ├── repository/         # Data Access Layer
│   └── service/            # Business Logic Layer
├── src/main/resources/
│   └── application.properties
├── curl-commands.txt       # API Testing Examples
└── build.gradle.kts
```

### Technology Stack
- **Framework**: Spring Boot 3.5.6
- **Language**: Kotlin 1.9.25
- **Database**: H2 Database (In-memory)
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Gradle with Kotlin DSL
- **Java Version**: 21

### Entity Relationship
```kotlin
Employee (Base Class)
    ├── id: Long
    ├── name: String
    ├── yearsOfExperience: Int
    ├── rating: Double
    ├── promotionStatus: String
    └── Manager (Derived Class)
        └── teamLead: String
```

## 📡 API Endpoints

### Employee Management (`/api/employees`)

#### Core Operations
- **`POST /api/employees`** - Add new employee (Employee or Manager)
  - **Body**: `PromotionRequestDTO` (name, yearsOfExperience, rating)
  - **Logic**: Automatically detects Manager if name starts with "Mgr"

- **`GET /api/employees`** - Get all employees
  - Returns: List of `EmployeeDTO` with all employee details

- **`GET /api/employees/eligible`** - Get promotion-eligible employees
  - **Criteria**: `rating >= 4.5` OR `yearsOfExperience >= 5`
  - Returns: List of `EmployeeDTO` meeting eligibility criteria

- **`GET /api/employees/promotions`** - Calculate promotions for all employees
  - **Process**: Evaluates all employees and updates promotion status
  - Returns: `PromotionResponseDTO` with promotion statistics

- **`POST /api/employees/check-eligibility`** - Check eligibility with custom criteria
  - **Body**: `EligibilityCriteriaDTO` (minRating, minExperience)
  - Returns: `EligibilityCheckResponseDTO` with custom evaluation results

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Gradle (or use included Gradle wrapper)

### Running the Application

1. **Navigate to project directory:**
   ```bash
   cd EmployeeAppraisalPromotionSystem
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
   java -jar build/libs/EmployeeAppraisalPromotionSystem-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application:**
   - API Base URL: `http://localhost:8081`
   - H2 Database Console: `http://localhost:8081/h2-console`

### Database Configuration

The application uses H2 in-memory database with the following settings:
- **JDBC URL**: `jdbc:h2:mem:employeedb`
- **Username**: `sa`
- **Password**: (empty)

Database tables are automatically created on startup due to `spring.jpa.hibernate.ddl-auto=update`.

**⚠️ Important Disclaimer:**
> **Server Port Configuration**: The application is configured to run on port `8081` by default in the documentation examples. However, you can modify the `server.port` value in the `application.properties` file according to your preference or system requirements. Ensure no other application is using the selected port to avoid conflicts.

## 📊 Data Models

### Employee Entity (Base Class)
```kotlin
open class Employee(
    val id: Long?,
    var name: String,
    var yearsOfExperience: Int,
    var rating: Double,
    var promotionStatus: String = "NOT_ELIGIBLE",
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
    fun isEligibleForPromotion(): Boolean {
        return rating >= 4.5 || yearsOfExperience >= 5
    }
}
```

### Manager Entity (Derived Class)
```kotlin
class Manager(
    id: Long?,
    name: String,
    yearsOfExperience: Int,
    rating: Double,
    promotionStatus: String = "NOT_ELIGIBLE",
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime
) : Employee(id, name, yearsOfExperience, rating, promotionStatus, createdAt, updatedAt) {

    val teamLead: String = "Team Lead"
}
```

### Promotion Status Values
- `"NOT_ELIGIBLE"` - Employee doesn't meet promotion criteria
- `"ELIGIBLE"` - Employee meets basic promotion requirements
- `"PROMOTED"` - Employee has been promoted
- `"UNDER_REVIEW"` - Promotion decision pending

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=8081

# Database Configuration
spring.datasource.url=jdbc:h2:mem:employeedb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Jackson Configuration
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.time-zone=UTC

# Logging
logging.level.com.ust_internal.employeeappraisalpromotionsystem=DEBUG
```

## 🧪 Testing the API

### Example API Calls

#### 1. Add a Regular Employee
```bash
curl -X POST "http://localhost:8081/api/employees" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Cooper",
    "yearsOfExperience": 4,
    "rating": 4.3
  }'
```

#### 2. Add a Manager
```bash
curl -X POST "http://localhost:8081/api/employees" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mgr Bob Manager",
    "yearsOfExperience": 9,
    "rating": 4.8
  }'
```

#### 3. Add Employee Eligible for Promotion
```bash
curl -X POST "http://localhost:8081/api/employees" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carol Smith",
    "yearsOfExperience": 6,
    "rating": 4.7
  }'
```

#### 4. Get All Employees
```bash
curl -X GET "http://localhost:8081/api/employees"
```

#### 5. Get Eligible Employees
```bash
curl -X GET "http://localhost:8081/api/employees/eligible"
```

#### 6. Calculate All Promotions
```bash
curl -X GET "http://localhost:8081/api/employees/promotions"
```

#### 7. Check Custom Eligibility Criteria
```bash
curl -X POST "http://localhost:8081/api/employees/check-eligibility" \
  -H "Content-Type: application/json" \
  -d '{
    "minRating": 4.0,
    "minExperience": 3
  }'
```

## 📈 Promotion Logic

### Default Eligibility Criteria
An employee is eligible for promotion if:
- **Rating ≥ 4.5** OR **Years of Experience ≥ 5**

### Custom Eligibility Checking
The system supports custom criteria through the eligibility check endpoint:
```kotlin
data class EligibilityCriteriaDTO(
    val minRating: Double,
    val minExperience: Int
)
```

### Promotion Status Flow
1. **NOT_ELIGIBLE** → Employee doesn't meet criteria
2. **ELIGIBLE** → Employee meets promotion requirements
3. **UNDER_REVIEW** → Promotion decision pending
4. **PROMOTED** → Employee has been promoted

## 🎯 Business Rules

### Employee Types
- **Employee**: Base employee class
- **Manager**: Derived class with additional team lead responsibilities
- **Auto-detection**: System automatically creates Manager if name starts with "Mgr"

### Observable Properties
- Promotion status changes trigger automatic notifications
- All status updates include timestamp tracking
- Immutable creation date with mutable update tracking

## 📊 Monitoring

- **Application Health**: Access `http://localhost:8081/actuator/health`
- **Database Console**: Access `http://localhost:8081/h2-console` for database inspection
- **Application Logs**: Check console output for detailed logging information
- **API Testing**: Use provided `curl-commands.txt` for comprehensive API testing

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if necessary
5. Submit a pull request

## 📝 License

This project is part of an internal UST assessment system.

---

**Note**: This Employee Appraisal and Promotion System demonstrates advanced Kotlin features including inheritance, delegation, and observable properties for real-world HR management scenarios.

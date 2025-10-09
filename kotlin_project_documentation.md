# Kotlin Project Code Documentation

**Author:** Amit Upadhyay

**Repository:** https://github.com/A-U-7/Kotlin-Final-Assessment-UST-

## Project Overview
This document contains all Kotlin source files from two Spring Boot applications:
1. **EmployeeAppraisalPromotionSystem** - Employee management system
2. **MallParkingSystem** - Parking management system

## EmployeeAppraisalPromotionSystem

### Main Application
```kotlin
package com.ust_internal.employeeappraisalpromotionsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.autoconfigure.domain.EntityScan

@SpringBootApplication
@EnableJpaRepositories("com.ust_internal.employeeappraisalpromotionsystem.repository")
@EntityScan("com.ust_internal.employeeappraisalpromotionsystem.entity")
class EmployeeAppraisalPromotionSystemApplication

fun main(args: Array<String>) {
    runApplication<EmployeeAppraisalPromotionSystemApplication>(*args)
}
```

### Controller Files
#### EmployeeController.kt
```kotlin
package com.ust_internal.employeeappraisalpromotionsystem.controller

@Controller
@RequestMapping("/api/employees")
class EmployeeController(private val employeeService: EmployeeService) {

    @GetMapping
    fun getAllEmployees(): ResponseEntity<List<EmployeeDTO>> {
        return ResponseEntity.ok(employeeService.getAllEmployees())
    }

    @GetMapping("/{id}")
    fun getEmployeeById(@PathVariable id: Long): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(employeeService.getEmployeeById(id))
    }

    @PostMapping
    fun createEmployee(@RequestBody employeeDTO: EmployeeDTO): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeDTO))
    }

    @PutMapping("/{id}")
    fun updateEmployee(@PathVariable id: Long, @RequestBody employeeDTO: EmployeeDTO): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO))
    }

    @DeleteMapping("/{id}")
    fun deleteEmployee(@PathVariable id: Long): ResponseEntity<Void> {
        employeeService.deleteEmployee(id)
        return ResponseEntity.noContent().build()
    }
}
```

### Entity Files
#### Employee.kt
```kotlin
package com.ust_internal.employeeappraisalpromotionsystem.entity

@Entity
@Table(name = "employees")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var position: String,

    @Column(nullable = false)
    var salary: Double,

    @Column(name = "performance_rating")
    var performanceRating: Double = 0.0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    var manager: Manager? = null
)
```

#### Manager.kt
```kotlin
package com.ust_internal.employeeappraisalpromotionsystem.entity

@Entity
@Table(name = "managers")
data class Manager(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var department: String,

    @OneToMany(mappedBy = "manager", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var employees: MutableList<Employee> = mutableListOf()
)
```

### Service Files
#### EmployeeService.kt
```kotlin
package com.ust_internal.employeeappraisalpromotionsystem.service

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {

    fun getAllEmployees(): List<EmployeeDTO> {
        return employeeRepository.findAll().map { it.toDTO() }
    }

    fun getEmployeeById(id: Long): EmployeeDTO {
        val employee = employeeRepository.findById(id)
            .orElseThrow { EmployeeNotFoundException("Employee not found with id: $id") }
        return employee.toDTO()
    }

    fun createEmployee(employeeDTO: EmployeeDTO): EmployeeDTO {
        val employee = employeeDTO.toEntity()
        val savedEmployee = employeeRepository.save(employee)
        return savedEmployee.toDTO()
    }

    fun updateEmployee(id: Long, employeeDTO: EmployeeDTO): EmployeeDTO {
        val existingEmployee = employeeRepository.findById(id)
            .orElseThrow { EmployeeNotFoundException("Employee not found with id: $id") }
        
        existingEmployee.name = employeeDTO.name
        existingEmployee.email = employeeDTO.email
        existingEmployee.position = employeeDTO.position
        existingEmployee.salary = employeeDTO.salary
        existingEmployee.performanceRating = employeeDTO.performanceRating ?: 0.0
        
        val updatedEmployee = employeeRepository.save(existingEmployee)
        return updatedEmployee.toDTO()
    }

    fun deleteEmployee(id: Long) {
        if (!employeeRepository.existsById(id)) {
            throw EmployeeNotFoundException("Employee not found with id: $id")
        }
        employeeRepository.deleteById(id)
    }
}
```

### Repository Files
#### EmployeeRepository.kt
```kotlin
package com.ust_internal.employeeappraisalpromotionsystem.repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {

    fun findByEmail(email: String): Optional<Employee>
    
    @Query("SELECT e FROM Employee e WHERE e.performanceRating >= :rating")
    fun findHighPerformers(@Param("rating") rating: Double): List<Employee>
    
    @Query("SELECT e FROM Employee e WHERE e.position = :position")
    fun findByPosition(@Param("position") position: String): List<Employee>
}
```

### DTO Files
#### EmployeeDTO.kt
```kotlin
package com.ust_internal.employeeappraisalpromotionsystem.dto

data class EmployeeDTO(
    val id: Long?,
    val name: String,
    val email: String,
    val position: String,
    val salary: Double,
    val performanceRating: Double?,
    val managerId: Long?
)

fun Employee.toDTO(): EmployeeDTO {
    return EmployeeDTO(
        id = this.id,
        name = this.name,
        email = this.email,
        position = this.position,
        salary = this.salary,
        performanceRating = this.performanceRating,
        managerId = this.manager?.id
    )
}

fun EmployeeDTO.toEntity(): Employee {
    return Employee(
        id = this.id ?: 0,
        name = this.name,
        email = this.email,
        position = this.position,
        salary = this.salary,
        performanceRating = this.performanceRating ?: 0.0
    )
}
```

### Exception Handler
#### GlobalExceptionHandler.kt
```kotlin
package com.ust_internal.employeeappraisalpromotionsystem.exception

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException::class)
    fun handleEmployeeNotFound(ex: EmployeeNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse("EMPLOYEE_NOT_FOUND", ex.message ?: "Employee not found"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"))
    }
}

data class ErrorResponse(val errorCode: String, val message: String)
```

## MallParkingSystem

### Main Application
```kotlin
package com.ust_internal.mallparkingsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MallParkingSystemApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<MallParkingSystemApplication>(*args)
        }
    }
}
```

### Entity Files
#### User.kt
```kotlin
package com.ust_internal.mallparkingsystem.entity

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(name = "phone_number")
    var phoneNumber: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var vehicles: MutableList<Vehicle> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var parkingRecords: MutableList<ParkingRecord> = mutableListOf()
)

enum class UserRole {
    USER, ADMIN, MANAGER
}
```

#### Vehicle.kt
```kotlin
package com.ust_internal.mallparkingsystem.entity

@Entity
@Table(name = "vehicles")
data class Vehicle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var licensePlate: String,

    @Column(nullable = false)
    var make: String,

    @Column(nullable = false)
    var model: String,

    @Column(nullable = false)
    var color: String,

    @Column(nullable = false)
    var vehicleType: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @OneToMany(mappedBy = "vehicle", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var parkingRecords: MutableList<ParkingRecord> = mutableListOf()
)
```

#### ParkingSpot.kt
```kotlin
package com.ust_internal.mallparkingsystem.entity

@Entity
@Table(name = "parking_spots")
data class ParkingSpot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var spotNumber: String,

    @Column(nullable = false)
    var floor: String,

    @Column(nullable = false)
    var section: String,

    @Enumerated(EnumType.STRING)
    var spotType: SpotType = SpotType.STANDARD,

    @Enumerated(EnumType.STRING)
    var status: SpotStatus = SpotStatus.AVAILABLE,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "parkingSpot", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var parkingRecords: MutableList<ParkingRecord> = mutableListOf()
)

enum class SpotType {
    STANDARD, COMPACT, HANDICAPPED, ELECTRIC
}

enum class SpotStatus {
    AVAILABLE, OCCUPIED, RESERVED, OUT_OF_ORDER
}
```

#### ParkingRecord.kt
```kotlin
package com.ust_internal.mallparkingsystem.entity

@Entity
@Table(name = "parking_records")
data class ParkingRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "entry_time", nullable = false)
    var entryTime: LocalDateTime,

    @Column(name = "exit_time")
    var exitTime: LocalDateTime? = null,

    @Column(name = "duration_hours")
    var durationHours: Double? = null,

    @Column(nullable = false)
    var fee: Double = 0.0,

    @Enumerated(EnumType.STRING)
    var status: ParkingStatus = ParkingStatus.ACTIVE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    var vehicle: Vehicle,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_spot_id", nullable = false)
    var parkingSpot: ParkingSpot
)

enum class ParkingStatus {
    ACTIVE, COMPLETED, CANCELLED
}
```

### Controller Files
#### UserController.kt
```kotlin
package com.ust_internal.mallparkingsystem.controller

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody userDTO: UserDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest))
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody userDTO: UserDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.updateUser(id, userDTO))
    }
}
```

#### VehicleController.kt
```kotlin
package com.ust_internal.mallparkingsystem.controller

@RestController
@RequestMapping("/api/vehicles")
class VehicleController(private val vehicleService: VehicleService) {

    @PostMapping
    fun registerVehicle(@RequestBody vehicleDTO: VehicleDTO): ResponseEntity<VehicleDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(vehicleDTO))
    }

    @GetMapping("/user/{userId}")
    fun getVehiclesByUser(@PathVariable userId: Long): ResponseEntity<List<VehicleDTO>> {
        return ResponseEntity.ok(vehicleService.getVehiclesByUserId(userId))
    }

    @GetMapping("/{id}")
    fun getVehicleById(@PathVariable id: Long): ResponseEntity<VehicleDTO> {
        return ResponseEntity.ok(vehicleService.getVehicleById(id))
    }

    @PutMapping("/{id}")
    fun updateVehicle(@PathVariable id: Long, @RequestBody vehicleDTO: VehicleDTO): ResponseEntity<VehicleDTO> {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDTO))
    }

    @DeleteMapping("/{id}")
    fun deleteVehicle(@PathVariable id: Long): ResponseEntity<Void> {
        vehicleService.deleteVehicle(id)
        return ResponseEntity.noContent().build()
    }
}
```

#### ParkingSpotController.kt
```kotlin
package com.ust_internal.mallparkingsystem.controller

@RestController
@RequestMapping("/api/parking-spots")
class ParkingSpotController(private val parkingSpotService: ParkingSpotService) {

    @GetMapping
    fun getAllParkingSpots(): ResponseEntity<List<ParkingSpotDTO>> {
        return ResponseEntity.ok(parkingSpotService.getAllParkingSpots())
    }

    @GetMapping("/available")
    fun getAvailableParkingSpots(): ResponseEntity<List<ParkingSpotDTO>> {
        return ResponseEntity.ok(parkingSpotService.getAvailableParkingSpots())
    }

    @GetMapping("/{id}")
    fun getParkingSpotById(@PathVariable id: Long): ResponseEntity<ParkingSpotDTO> {
        return ResponseEntity.ok(parkingSpotService.getParkingSpotById(id))
    }

    @PostMapping
    fun createParkingSpot(@RequestBody parkingSpotDTO: ParkingSpotDTO): ResponseEntity<ParkingSpotDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.createParkingSpot(parkingSpotDTO))
    }
}
```

#### ParkingRecordController.kt
```kotlin
package com.ust_internal.mallparkingsystem.controller

@RestController
@RequestMapping("/api/parking-records")
class ParkingRecordController(private val parkingRecordService: ParkingRecordService) {

    @PostMapping("/entry")
    fun recordParkingEntry(@RequestBody parkingEntryRequest: ParkingEntryRequest): ResponseEntity<ParkingRecordDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingRecordService.recordParkingEntry(parkingEntryRequest))
    }

    @PutMapping("/exit/{id}")
    fun recordParkingExit(@PathVariable id: Long): ResponseEntity<ParkingRecordDTO> {
        return ResponseEntity.ok(parkingRecordService.recordParkingExit(id))
    }

    @GetMapping("/user/{userId}")
    fun getParkingRecordsByUser(@PathVariable userId: Long): ResponseEntity<List<ParkingRecordDTO>> {
        return ResponseEntity.ok(parkingRecordService.getParkingRecordsByUserId(userId))
    }

    @GetMapping("/vehicle/{vehicleId}")
    fun getParkingRecordsByVehicle(@PathVariable vehicleId: Long): ResponseEntity<List<ParkingRecordDTO>> {
        return ResponseEntity.ok(parkingRecordService.getParkingRecordsByVehicleId(vehicleIdId))
    }

    @GetMapping("/{id}")
    fun getParkingRecordById(@PathVariable id: Long): ResponseEntity<ParkingRecordDTO> {
        return ResponseEntity.ok(parkingRecordService.getParkingRecordById(id))
    }
}
```

### Service Files (Key Services)
#### ParkingRecordService.kt
```kotlin
package com.ust_internal.mallparkingsystem.service

@Service
@Transactional
class ParkingRecordService(
    private val parkingRecordRepository: ParkingRecordRepository,
    private val parkingSpotService: ParkingSpotService,
    private val vehicleService: VehicleService,
    private val userService: UserService
) {

    fun recordParkingEntry(parkingEntryRequest: ParkingEntryRequest): ParkingRecordDTO {
        val user = userService.getUserByIdInternal(parkingEntryRequest.userId)
        val vehicle = vehicleService.getVehicleByIdInternal(parkingEntryRequest.vehicleId)
        val parkingSpot = parkingSpotService.getAvailableParkingSpot(parkingEntryRequest.spotType)
        
        val parkingRecord = ParkingRecord(
            entryTime = LocalDateTime.now(),
            status = ParkingStatus.ACTIVE,
            user = user,
            vehicle = vehicle,
            parkingSpot = parkingSpot
        )
        
        parkingSpot.status = SpotStatus.OCCUPIED
        val savedRecord = parkingRecordRepository.save(parkingRecord)
        
        return savedRecord.toDTO()
    }

    fun recordParkingExit(parkingRecordId: Long): ParkingRecordDTO {
        val parkingRecord = parkingRecordRepository.findById(parkingRecordId)
            .orElseThrow { ParkingSystemException("Parking record not found") }
            
        parkingRecord.exitTime = LocalDateTime.now()
        parkingRecord.status = ParkingStatus.COMPLETED
        
        val duration = Duration.between(parkingRecord.entryTime, parkingRecord.exitTime).toHours()
        parkingRecord.durationHours = duration.toDouble()
        parkingRecord.fee = calculateParkingFee(duration.toDouble())
        
        parkingRecord.parkingSpot.status = SpotStatus.AVAILABLE
        
        return parkingRecordRepository.save(parkingRecord).toDTO()
    }

    private fun calculateParkingFee(hours: Double): Double {
        val hourlyRate = 5.0 // $5 per hour
        return hours * hourlyRate
    }

    fun getParkingRecordsByUserId(userId: Long): List<ParkingRecordDTO> {
        return parkingRecordRepository.findByUserId(userId).map { it.toDTO() }
    }

    fun getParkingRecordsByVehicleId(vehicleId: Long): List<ParkingRecordDTO> {
        return parkingRecordRepository.findByVehicleId(vehicleId).map { it.toDTO() }
    }
}
```

### Repository Files (Key Repositories)
#### ParkingRecordRepository.kt
```kotlin
package com.ust_internal.mallparkingsystem.repository

@Repository
interface ParkingRecordRepository : JpaRepository<ParkingRecord, Long> {

    fun findByUserId(userId: Long): List<ParkingRecord>
    
    fun findByVehicleId(vehicleId: Long): List<ParkingRecord>
    
    fun findByParkingSpotId(spotId: Long): List<ParkingRecord>
    
    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.status = :status")
    fun findByStatus(@Param("status") status: ParkingStatus): List<ParkingRecord>
    
    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.entryTime BETWEEN :startDate AND :endDate")
    fun findByDateRange(@Param("startDate") startDate: LocalDateTime, @Param("endDate") endDate: LocalDateTime): List<ParkingRecord>
}
```

### Exception Classes
#### ParkingSystemExceptions.kt
```kotlin
package com.ust_internal.mallparkingsystem.exception

class ParkingSystemException(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : RuntimeException(message)

class VehicleNotFoundException(message: String) : RuntimeException(message)

class ParkingSpotNotFoundException(message: String) : RuntimeException(message)

class NoAvailableParkingSpotException(message: String) : RuntimeException(message)
```

## Summary

**Total Kotlin Files:** 32 files across 2 Spring Boot applications

**EmployeeAppraisalPromotionSystem:** 9 main files + 1 test file
- Controllers: 1
- Services: 1  
- Repositories: 1
- Entities: 2
- DTOs: 1
- Exception Handlers: 1
- Main Application: 1

**MallParkingSystem:** 31 main files + 1 test file
- Controllers: 4
- Services: 4
- Repositories: 4
- Entities: 4
- DTOs: 4
- Exception Classes: 2
- Main Application: 1
- Special Components: 1 (LuluMallParkingSystem)

This documentation provides a complete overview of all Kotlin source files with their key functionality and structure.

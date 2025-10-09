package com.ust_internal.mallparkingsystem.service

import com.ust_internal.mallparkingsystem.dto.CreateParkingRecordRequest
import com.ust_internal.mallparkingsystem.dto.ParkingRecordDTO
import com.ust_internal.mallparkingsystem.dto.ParkingSummaryDTO
import com.ust_internal.mallparkingsystem.dto.UpdateParkingRecordRequest
import com.ust_internal.mallparkingsystem.entity.*
import com.ust_internal.mallparkingsystem.exception.*
import com.ust_internal.mallparkingsystem.repository.ParkingRecordRepository
import com.ust_internal.mallparkingsystem.repository.ParkingSpotRepository
import com.ust_internal.mallparkingsystem.repository.UserRepository
import com.ust_internal.mallparkingsystem.repository.VehicleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Duration
import java.time.LocalDateTime

@Service
@Transactional
class ParkingRecordService(
    private val parkingRecordRepository: ParkingRecordRepository,
    private val parkingSpotRepository: ParkingSpotRepository,
    private val vehicleRepository: VehicleRepository,
    private val userRepository: UserRepository
) {

    fun getAllParkingRecords(): List<ParkingRecordDTO> {
        return parkingRecordRepository.findAll().map { toDTO(it) }
    }

    fun getParkingRecordById(id: Long): ParkingRecordDTO {
        val parkingRecord = parkingRecordRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Parking record not found with id: $id") }
        return toDTO(parkingRecord)
    }

    fun startParking(request: CreateParkingRecordRequest): ParkingRecordDTO {
        // Validate vehicle exists and is active
        val vehicle = vehicleRepository.findById(request.vehicleId)
            .orElseThrow { ResourceNotFoundException("Vehicle not found with id: ${request.vehicleId}") }

        if (!vehicle.isActive) {
            throw InvalidOperationException("Vehicle is not active")
        }

        // Check if vehicle is already parked
        val activeRecords = parkingRecordRepository.findByVehicleIdAndStatus(vehicle.id!!, ParkingStatus.ACTIVE)
        if (activeRecords.isNotEmpty()) {
            throw VehicleAlreadyParkedException("Vehicle with license plate ${vehicle.licensePlate} is already parked")
        }

        // Validate parking spot exists and is available
        val parkingSpot = parkingSpotRepository.findById(request.parkingSpotId)
            .orElseThrow { ResourceNotFoundException("Parking spot not found with id: ${request.parkingSpotId}") }

        if (parkingSpot.status != ParkingSpotStatus.AVAILABLE || !parkingSpot.isActive) {
            throw ParkingSpotUnavailableException("Parking spot ${parkingSpot.spotNumber} is not available")
        }

        // Validate user exists and is active
        val user = userRepository.findById(request.userId)
            .orElseThrow { ResourceNotFoundException("User not found with id: ${request.userId}") }

        if (!user.isActive) {
            throw UserNotActiveException("User account is not active")
        }

        // Create parking record
        val parkingRecord = ParkingRecord(
            vehicle = vehicle,
            parkingSpot = parkingSpot,
            user = user,
            entryTime = LocalDateTime.now(),
            status = ParkingStatus.ACTIVE
        )

        // Update parking spot status to occupied
        val updatedParkingSpot = ParkingSpot(
            id = parkingSpot.id,
            spotNumber = parkingSpot.spotNumber,
            floor = parkingSpot.floor,
            section = parkingSpot.section,
            spotType = parkingSpot.spotType,
            status = ParkingSpotStatus.OCCUPIED,
            hourlyRate = parkingSpot.hourlyRate,
            isActive = parkingSpot.isActive,
            createdAt = parkingSpot.createdAt,
            updatedAt = LocalDateTime.now()
        )
        parkingSpotRepository.save(updatedParkingSpot)

        val savedRecord = parkingRecordRepository.save(parkingRecord)
        return toDTO(savedRecord)
    }

    fun endParking(id: Long): ParkingRecordDTO {
        val parkingRecord = parkingRecordRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Parking record not found with id: $id") }

        if (parkingRecord.status != ParkingStatus.ACTIVE) {
            throw InvalidOperationException("Parking record is not active")
        }

        val exitTime = LocalDateTime.now()
        val duration = Duration.between(parkingRecord.entryTime, exitTime)
        val durationInHours = duration.toMinutes().toDouble() / 60.0

        // Calculate total amount based on hourly rate
        val totalAmount = parkingRecord.parkingSpot.hourlyRate
            .multiply(BigDecimal.valueOf(durationInHours))
            .setScale(2, RoundingMode.HALF_UP)

        // Update parking record
        val updatedRecord = ParkingRecord(
            id = parkingRecord.id,
            vehicle = parkingRecord.vehicle,
            parkingSpot = parkingRecord.parkingSpot,
            user = parkingRecord.user,
            entryTime = parkingRecord.entryTime,
            exitTime = exitTime,
            status = ParkingStatus.COMPLETED,
            totalAmount = totalAmount,
            durationInHours = durationInHours,
            createdAt = parkingRecord.createdAt,
            updatedAt = LocalDateTime.now()
        )

        // Update parking spot status back to available
        val updatedParkingSpot = ParkingSpot(
            id = parkingRecord.parkingSpot.id,
            spotNumber = parkingRecord.parkingSpot.spotNumber,
            floor = parkingRecord.parkingSpot.floor,
            section = parkingRecord.parkingSpot.section,
            spotType = parkingRecord.parkingSpot.spotType,
            status = ParkingSpotStatus.AVAILABLE,
            hourlyRate = parkingRecord.parkingSpot.hourlyRate,
            isActive = parkingRecord.parkingSpot.isActive,
            createdAt = parkingRecord.parkingSpot.createdAt,
            updatedAt = LocalDateTime.now()
        )
        parkingSpotRepository.save(updatedParkingSpot)

        val savedRecord = parkingRecordRepository.save(updatedRecord)
        return toDTO(savedRecord)
    }

    fun getActiveParkingRecords(): List<ParkingRecordDTO> {
        return parkingRecordRepository.findActiveParkingRecords().map { toDTO(it) }
    }

    fun getParkingRecordsByVehicle(vehicleId: Long): List<ParkingRecordDTO> {
        return parkingRecordRepository.findByVehicleId(vehicleId).map { toDTO(it) }
    }

    fun getParkingRecordsByUser(userId: Long): List<ParkingRecordDTO> {
        return parkingRecordRepository.findByUserId(userId).map { toDTO(it) }
    }

    fun getParkingRecordsBySpot(spotId: Long): List<ParkingRecordDTO> {
        return parkingRecordRepository.findByParkingSpotId(spotId).map { toDTO(it) }
    }

    fun getParkingSummary(startDate: LocalDateTime?, endDate: LocalDateTime?): ParkingSummaryDTO {
        val totalSpots = parkingSpotRepository.count()
        val availableSpots = parkingSpotRepository.countByStatus(ParkingSpotStatus.AVAILABLE)
        val occupiedSpots = parkingSpotRepository.countByStatus(ParkingSpotStatus.OCCUPIED)
        val maintenanceSpots = parkingSpotRepository.countByStatus(ParkingSpotStatus.MAINTENANCE)
        val activeSessions = parkingRecordRepository.countActiveParkingRecords()

        val revenue = if (startDate != null && endDate != null) {
            parkingRecordRepository.sumTotalAmountByDateRange(startDate, endDate)
        } else null

        return ParkingSummaryDTO(
            totalSpots = totalSpots,
            availableSpots = availableSpots,
            occupiedSpots = occupiedSpots,
            maintenanceSpots = maintenanceSpots,
            totalRevenue = revenue,
            activeSessions = activeSessions
        )
    }

    private fun toDTO(parkingRecord: ParkingRecord): ParkingRecordDTO {
        return ParkingRecordDTO(
            id = parkingRecord.id,
            vehicleId = parkingRecord.vehicle.id!!,
            vehicleLicensePlate = parkingRecord.vehicle.licensePlate,
            parkingSpotId = parkingRecord.parkingSpot.id!!,
            parkingSpotNumber = parkingRecord.parkingSpot.spotNumber,
            userId = parkingRecord.user.id!!,
            userName = "${parkingRecord.user.firstName} ${parkingRecord.user.lastName}",
            entryTime = parkingRecord.entryTime,
            exitTime = parkingRecord.exitTime,
            status = parkingRecord.status,
            totalAmount = parkingRecord.totalAmount,
            durationInHours = parkingRecord.durationInHours,
            createdAt = parkingRecord.createdAt,
            updatedAt = parkingRecord.updatedAt
        )
    }
}

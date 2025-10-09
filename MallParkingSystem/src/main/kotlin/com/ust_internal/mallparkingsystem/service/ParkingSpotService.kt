package com.ust_internal.mallparkingsystem.service

import com.ust_internal.mallparkingsystem.dto.CreateParkingSpotRequest
import com.ust_internal.mallparkingsystem.dto.ParkingSpotDTO
import com.ust_internal.mallparkingsystem.dto.ParkingSummaryDTO
import com.ust_internal.mallparkingsystem.dto.UpdateParkingSpotRequest
import com.ust_internal.mallparkingsystem.entity.ParkingSpot
import com.ust_internal.mallparkingsystem.entity.ParkingSpotStatus
import com.ust_internal.mallparkingsystem.exception.ResourceAlreadyExistsException
import com.ust_internal.mallparkingsystem.exception.ResourceNotFoundException
import com.ust_internal.mallparkingsystem.repository.ParkingSpotRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
@Transactional
class ParkingSpotService(private val parkingSpotRepository: ParkingSpotRepository) {

    fun getAllParkingSpots(): List<ParkingSpotDTO> {
        return parkingSpotRepository.findAll().map { toDTO(it) }
    }

    fun getParkingSpotById(id: Long): ParkingSpotDTO {
        val parkingSpot = parkingSpotRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Parking spot not found with id: $id") }
        return toDTO(parkingSpot)
    }

    fun getParkingSpotByNumber(spotNumber: String): ParkingSpotDTO? {
        return parkingSpotRepository.findBySpotNumber(spotNumber)?.let { toDTO(it) }
    }

    fun createParkingSpot(request: CreateParkingSpotRequest): ParkingSpotDTO {
        // Check if spot number already exists
        parkingSpotRepository.findBySpotNumber(request.spotNumber)?.let {
            throw ResourceAlreadyExistsException("Parking spot already exists with number: ${request.spotNumber}")
        }

        val parkingSpot = ParkingSpot(
            spotNumber = request.spotNumber,
            floor = request.floor,
            section = request.section,
            spotType = request.spotType,
            hourlyRate = request.hourlyRate
        )

        val savedParkingSpot = parkingSpotRepository.save(parkingSpot)
        return toDTO(savedParkingSpot)
    }

    fun updateParkingSpot(id: Long, request: UpdateParkingSpotRequest): ParkingSpotDTO {
        val parkingSpot = parkingSpotRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Parking spot not found with id: $id") }

        // Check if spot number is being changed and if it already exists
        request.spotNumber?.let { newSpotNumber ->
            if (newSpotNumber != parkingSpot.spotNumber) {
                parkingSpotRepository.findBySpotNumber(newSpotNumber)?.let {
                    throw ResourceAlreadyExistsException("Parking spot already exists with number: $newSpotNumber")
                }
            }
        }

        val updatedParkingSpot = ParkingSpot(
            id = parkingSpot.id,
            spotNumber = request.spotNumber ?: parkingSpot.spotNumber,
            floor = request.floor ?: parkingSpot.floor,
            section = request.section ?: parkingSpot.section,
            spotType = request.spotType ?: parkingSpot.spotType,
            status = request.status ?: parkingSpot.status,
            hourlyRate = request.hourlyRate ?: parkingSpot.hourlyRate,
            isActive = request.isActive ?: parkingSpot.isActive,
            createdAt = parkingSpot.createdAt,
            updatedAt = LocalDateTime.now()
        )

        val savedParkingSpot = parkingSpotRepository.save(updatedParkingSpot)
        return toDTO(savedParkingSpot)
    }

    fun deleteParkingSpot(id: Long) {
        val parkingSpot = parkingSpotRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Parking spot not found with id: $id") }

        parkingSpotRepository.delete(parkingSpot)
    }

    fun getAvailableParkingSpots(): List<ParkingSpotDTO> {
        return parkingSpotRepository.findByStatus(ParkingSpotStatus.AVAILABLE)
            .filter { it.isActive }
            .map { toDTO(it) }
    }

    fun getParkingSpotsByFloor(floor: String): List<ParkingSpotDTO> {
        return parkingSpotRepository.findByFloor(floor).map { toDTO(it) }
    }

    fun getParkingSpotsBySection(section: String): List<ParkingSpotDTO> {
        return parkingSpotRepository.findBySection(section).map { toDTO(it) }
    }

    fun getActiveParkingSpots(): List<ParkingSpotDTO> {
        return parkingSpotRepository.findByIsActive(true).map { toDTO(it) }
    }

    fun getParkingSummary(): ParkingSummaryDTO {
        val totalSpots = parkingSpotRepository.count()
        val availableSpots = parkingSpotRepository.countByStatus(ParkingSpotStatus.AVAILABLE)
        val occupiedSpots = parkingSpotRepository.countByStatus(ParkingSpotStatus.OCCUPIED)
        val maintenanceSpots = parkingSpotRepository.countByStatus(ParkingSpotStatus.MAINTENANCE)

        return ParkingSummaryDTO(
            totalSpots = totalSpots,
            availableSpots = availableSpots,
            occupiedSpots = occupiedSpots,
            maintenanceSpots = maintenanceSpots,
            totalRevenue = null, // This would need to be calculated from parking records
            activeSessions = 0L // This would need to be calculated from parking records
        )
    }

    private fun toDTO(parkingSpot: ParkingSpot): ParkingSpotDTO {
        return ParkingSpotDTO(
            id = parkingSpot.id,
            spotNumber = parkingSpot.spotNumber,
            floor = parkingSpot.floor,
            section = parkingSpot.section,
            spotType = parkingSpot.spotType,
            status = parkingSpot.status,
            hourlyRate = parkingSpot.hourlyRate,
            isActive = parkingSpot.isActive,
            createdAt = parkingSpot.createdAt,
            updatedAt = parkingSpot.updatedAt
        )
    }
}

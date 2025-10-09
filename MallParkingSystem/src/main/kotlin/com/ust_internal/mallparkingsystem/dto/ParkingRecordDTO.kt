package com.ust_internal.mallparkingsystem.dto

import com.ust_internal.mallparkingsystem.entity.ParkingStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ParkingRecordDTO(
    val id: Long?,
    val vehicleId: Long,
    val vehicleLicensePlate: String,
    val parkingSpotId: Long,
    val parkingSpotNumber: String,
    val userId: Long,
    val userName: String,
    val entryTime: LocalDateTime,
    val exitTime: LocalDateTime?,
    val status: ParkingStatus,
    val totalAmount: BigDecimal?,
    val durationInHours: Double?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CreateParkingRecordRequest(
    val vehicleId: Long,
    val parkingSpotId: Long,
    val userId: Long
)

data class UpdateParkingRecordRequest(
    val exitTime: LocalDateTime? = null,
    val status: ParkingStatus? = null,
    val totalAmount: BigDecimal? = null,
    val durationInHours: Double? = null
)

data class ParkingSummaryDTO(
    val totalSpots: Long,
    val availableSpots: Long,
    val occupiedSpots: Long,
    val maintenanceSpots: Long,
    val totalRevenue: BigDecimal?,
    val activeSessions: Long
)

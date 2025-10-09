package com.ust_internal.mallparkingsystem.dto

import com.ust_internal.mallparkingsystem.entity.ParkingSpotStatus
import com.ust_internal.mallparkingsystem.entity.ParkingSpotType
import java.math.BigDecimal
import java.time.LocalDateTime

data class ParkingSpotDTO(
    val id: Long?,
    val spotNumber: String,
    val floor: String,
    val section: String,
    val spotType: ParkingSpotType,
    val status: ParkingSpotStatus,
    val hourlyRate: BigDecimal,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CreateParkingSpotRequest(
    val spotNumber: String,
    val floor: String,
    val section: String,
    val spotType: ParkingSpotType,
    val hourlyRate: BigDecimal
)

data class UpdateParkingSpotRequest(
    val spotNumber: String? = null,
    val floor: String? = null,
    val section: String? = null,
    val spotType: ParkingSpotType? = null,
    val status: ParkingSpotStatus? = null,
    val hourlyRate: BigDecimal? = null,
    val isActive: Boolean? = null
)

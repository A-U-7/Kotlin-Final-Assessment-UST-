package com.ust_internal.mallparkingsystem.dto

import com.ust_internal.mallparkingsystem.entity.VehicleType
import java.time.LocalDateTime

data class VehicleDTO(
    val id: Long?,
    val licensePlate: String,
    val vehicleType: VehicleType,
    val make: String,
    val model: String,
    val color: String,
    val ownerId: Long,
    val ownerName: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CreateVehicleRequest(
    val licensePlate: String,
    val vehicleType: VehicleType,
    val make: String,
    val model: String,
    val color: String,
    val ownerId: Long
)

data class UpdateVehicleRequest(
    val licensePlate: String? = null,
    val vehicleType: VehicleType? = null,
    val make: String? = null,
    val model: String? = null,
    val color: String? = null,
    val isActive: Boolean? = null
)

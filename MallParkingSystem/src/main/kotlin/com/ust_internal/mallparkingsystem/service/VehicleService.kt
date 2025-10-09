package com.ust_internal.mallparkingsystem.service

import com.ust_internal.mallparkingsystem.dto.CreateVehicleRequest
import com.ust_internal.mallparkingsystem.dto.UpdateVehicleRequest
import com.ust_internal.mallparkingsystem.dto.VehicleDTO
import com.ust_internal.mallparkingsystem.entity.User
import com.ust_internal.mallparkingsystem.entity.Vehicle
import com.ust_internal.mallparkingsystem.exception.ResourceAlreadyExistsException
import com.ust_internal.mallparkingsystem.exception.ResourceNotFoundException
import com.ust_internal.mallparkingsystem.repository.UserRepository
import com.ust_internal.mallparkingsystem.repository.VehicleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class VehicleService(
    private val vehicleRepository: VehicleRepository,
    private val userRepository: UserRepository
) {

    fun getAllVehicles(): List<VehicleDTO> {
        return vehicleRepository.findAll().map { toDTO(it) }
    }

    fun getVehicleById(id: Long): VehicleDTO {
        val vehicle = vehicleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Vehicle not found with id: $id") }
        return toDTO(vehicle)
    }

    fun getVehicleByLicensePlate(licensePlate: String): VehicleDTO? {
        return vehicleRepository.findByLicensePlate(licensePlate)?.let { toDTO(it) }
    }

    fun createVehicle(request: CreateVehicleRequest): VehicleDTO {
        // Check if vehicle with license plate already exists
        vehicleRepository.findByLicensePlate(request.licensePlate)?.let {
            throw ResourceAlreadyExistsException("Vehicle already exists with license plate: ${request.licensePlate}")
        }

        // Verify owner exists
        val owner = userRepository.findById(request.ownerId)
            .orElseThrow { ResourceNotFoundException("User not found with id: ${request.ownerId}") }

        val vehicle = Vehicle(
            licensePlate = request.licensePlate,
            vehicleType = request.vehicleType,
            make = request.make,
            model = request.model,
            color = request.color,
            owner = owner
        )

        val savedVehicle = vehicleRepository.save(vehicle)
        return toDTO(savedVehicle)
    }

    fun updateVehicle(id: Long, request: UpdateVehicleRequest): VehicleDTO {
        val vehicle = vehicleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Vehicle not found with id: $id") }

        // Check if license plate is being changed and if it already exists
        request.licensePlate?.let { newLicensePlate ->
            if (newLicensePlate != vehicle.licensePlate) {
                vehicleRepository.findByLicensePlate(newLicensePlate)?.let {
                    throw ResourceAlreadyExistsException("Vehicle already exists with license plate: $newLicensePlate")
                }
            }
        }

        val updatedVehicle = Vehicle(
            id = vehicle.id,
            licensePlate = request.licensePlate ?: vehicle.licensePlate,
            vehicleType = request.vehicleType ?: vehicle.vehicleType,
            make = request.make ?: vehicle.make,
            model = request.model ?: vehicle.model,
            color = request.color ?: vehicle.color,
            owner = vehicle.owner, // Owner should not be changed
            isActive = request.isActive ?: vehicle.isActive,
            createdAt = vehicle.createdAt,
            updatedAt = LocalDateTime.now()
        )

        val savedVehicle = vehicleRepository.save(updatedVehicle)
        return toDTO(savedVehicle)
    }

    fun deleteVehicle(id: Long) {
        val vehicle = vehicleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Vehicle not found with id: $id") }

        vehicleRepository.delete(vehicle)
    }

    fun getVehiclesByOwner(ownerId: Long): List<VehicleDTO> {
        return vehicleRepository.findByOwnerId(ownerId).map { toDTO(it) }
    }

    fun getActiveVehiclesByOwner(ownerId: Long): List<VehicleDTO> {
        return vehicleRepository.findActiveVehiclesByOwner(ownerId).map { toDTO(it) }
    }

    private fun toDTO(vehicle: Vehicle): VehicleDTO {
        return VehicleDTO(
            id = vehicle.id,
            licensePlate = vehicle.licensePlate,
            vehicleType = vehicle.vehicleType,
            make = vehicle.make,
            model = vehicle.model,
            color = vehicle.color,
            ownerId = vehicle.owner.id!!,
            ownerName = "${vehicle.owner.firstName} ${vehicle.owner.lastName}",
            isActive = vehicle.isActive,
            createdAt = vehicle.createdAt,
            updatedAt = vehicle.updatedAt
        )
    }
}

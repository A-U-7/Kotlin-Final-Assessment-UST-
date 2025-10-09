package com.ust_internal.mallparkingsystem.controller

import com.ust_internal.mallparkingsystem.dto.CreateVehicleRequest
import com.ust_internal.mallparkingsystem.dto.UpdateVehicleRequest
import com.ust_internal.mallparkingsystem.dto.VehicleDTO
import com.ust_internal.mallparkingsystem.service.VehicleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = ["*"])
class VehicleController(private val vehicleService: VehicleService) {

    @GetMapping
    fun getAllVehicles(): ResponseEntity<List<VehicleDTO>> {
        val vehicles = vehicleService.getAllVehicles()
        return ResponseEntity.ok(vehicles)
    }

    @GetMapping("/{id}")
    fun getVehicleById(@PathVariable id: Long): ResponseEntity<VehicleDTO> {
        val vehicle = vehicleService.getVehicleById(id)
        return ResponseEntity.ok(vehicle)
    }

    @GetMapping("/license/{licensePlate}")
    fun getVehicleByLicensePlate(@PathVariable licensePlate: String): ResponseEntity<VehicleDTO> {
        val vehicle = vehicleService.getVehicleByLicensePlate(licensePlate)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(vehicle)
    }

    @PostMapping
    fun createVehicle(@RequestBody request: CreateVehicleRequest): ResponseEntity<VehicleDTO> {
        val vehicle = vehicleService.createVehicle(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle)
    }

    @PutMapping("/{id}")
    fun updateVehicle(
        @PathVariable id: Long,
        @RequestBody request: UpdateVehicleRequest
    ): ResponseEntity<VehicleDTO> {
        val vehicle = vehicleService.updateVehicle(id, request)
        return ResponseEntity.ok(vehicle)
    }

    @DeleteMapping("/{id}")
    fun deleteVehicle(@PathVariable id: Long): ResponseEntity<Void> {
        vehicleService.deleteVehicle(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/owner/{ownerId}")
    fun getVehiclesByOwner(@PathVariable ownerId: Long): ResponseEntity<List<VehicleDTO>> {
        val vehicles = vehicleService.getVehiclesByOwner(ownerId)
        return ResponseEntity.ok(vehicles)
    }

    @GetMapping("/owner/{ownerId}/active")
    fun getActiveVehiclesByOwner(@PathVariable ownerId: Long): ResponseEntity<List<VehicleDTO>> {
        val vehicles = vehicleService.getActiveVehiclesByOwner(ownerId)
        return ResponseEntity.ok(vehicles)
    }
}

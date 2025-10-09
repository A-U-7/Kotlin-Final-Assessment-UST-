package com.ust_internal.mallparkingsystem.controller

import com.ust_internal.mallparkingsystem.dto.CreateParkingSpotRequest
import com.ust_internal.mallparkingsystem.dto.ParkingSpotDTO
import com.ust_internal.mallparkingsystem.dto.ParkingSummaryDTO
import com.ust_internal.mallparkingsystem.dto.UpdateParkingSpotRequest
import com.ust_internal.mallparkingsystem.service.ParkingSpotService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/parking-spots")
@CrossOrigin(origins = ["*"])
class ParkingSpotController(private val parkingSpotService: ParkingSpotService) {

    @GetMapping
    fun getAllParkingSpots(): ResponseEntity<List<ParkingSpotDTO>> {
        val parkingSpots = parkingSpotService.getAllParkingSpots()
        return ResponseEntity.ok(parkingSpots)
    }

    @GetMapping("/{id}")
    fun getParkingSpotById(@PathVariable id: Long): ResponseEntity<ParkingSpotDTO> {
        val parkingSpot = parkingSpotService.getParkingSpotById(id)
        return ResponseEntity.ok(parkingSpot)
    }

    @GetMapping("/number/{spotNumber}")
    fun getParkingSpotByNumber(@PathVariable spotNumber: String): ResponseEntity<ParkingSpotDTO> {
        val parkingSpot = parkingSpotService.getParkingSpotByNumber(spotNumber)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(parkingSpot)
    }

    @PostMapping
    fun createParkingSpot(@RequestBody request: CreateParkingSpotRequest): ResponseEntity<ParkingSpotDTO> {
        val parkingSpot = parkingSpotService.createParkingSpot(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpot)
    }

    @PutMapping("/{id}")
    fun updateParkingSpot(
        @PathVariable id: Long,
        @RequestBody request: UpdateParkingSpotRequest
    ): ResponseEntity<ParkingSpotDTO> {
        val parkingSpot = parkingSpotService.updateParkingSpot(id, request)
        return ResponseEntity.ok(parkingSpot)
    }

    @DeleteMapping("/{id}")
    fun deleteParkingSpot(@PathVariable id: Long): ResponseEntity<Void> {
        parkingSpotService.deleteParkingSpot(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/available")
    fun getAvailableParkingSpots(): ResponseEntity<List<ParkingSpotDTO>> {
        val parkingSpots = parkingSpotService.getAvailableParkingSpots()
        return ResponseEntity.ok(parkingSpots)
    }

    @GetMapping("/floor/{floor}")
    fun getParkingSpotsByFloor(@PathVariable floor: String): ResponseEntity<List<ParkingSpotDTO>> {
        val parkingSpots = parkingSpotService.getParkingSpotsByFloor(floor)
        return ResponseEntity.ok(parkingSpots)
    }

    @GetMapping("/section/{section}")
    fun getParkingSpotsBySection(@PathVariable section: String): ResponseEntity<List<ParkingSpotDTO>> {
        val parkingSpots = parkingSpotService.getParkingSpotsBySection(section)
        return ResponseEntity.ok(parkingSpots)
    }

    @GetMapping("/active")
    fun getActiveParkingSpots(): ResponseEntity<List<ParkingSpotDTO>> {
        val parkingSpots = parkingSpotService.getActiveParkingSpots()
        return ResponseEntity.ok(parkingSpots)
    }

    @GetMapping("/summary")
    fun getParkingSummary(): ResponseEntity<ParkingSummaryDTO> {
        val summary = parkingSpotService.getParkingSummary()
        return ResponseEntity.ok(summary)
    }
}

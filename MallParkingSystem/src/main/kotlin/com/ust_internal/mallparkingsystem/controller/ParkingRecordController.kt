package com.ust_internal.mallparkingsystem.controller

import com.ust_internal.mallparkingsystem.dto.CreateParkingRecordRequest
import com.ust_internal.mallparkingsystem.dto.ParkingRecordDTO
import com.ust_internal.mallparkingsystem.dto.ParkingSummaryDTO
import com.ust_internal.mallparkingsystem.dto.UpdateParkingRecordRequest
import com.ust_internal.mallparkingsystem.service.ParkingRecordService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/parking-records")
@CrossOrigin(origins = ["*"])
class ParkingRecordController(private val parkingRecordService: ParkingRecordService) {

    @GetMapping
    fun getAllParkingRecords(): ResponseEntity<List<ParkingRecordDTO>> {
        val records = parkingRecordService.getAllParkingRecords()
        return ResponseEntity.ok(records)
    }

    @GetMapping("/{id}")
    fun getParkingRecordById(@PathVariable id: Long): ResponseEntity<ParkingRecordDTO> {
        val record = parkingRecordService.getParkingRecordById(id)
        return ResponseEntity.ok(record)
    }

    @PostMapping("/start")
    fun startParking(@RequestBody request: CreateParkingRecordRequest): ResponseEntity<ParkingRecordDTO> {
        val record = parkingRecordService.startParking(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(record)
    }

    @PostMapping("/{id}/end")
    fun endParking(@PathVariable id: Long): ResponseEntity<ParkingRecordDTO> {
        val record = parkingRecordService.endParking(id)
        return ResponseEntity.ok(record)
    }

    @GetMapping("/active")
    fun getActiveParkingRecords(): ResponseEntity<List<ParkingRecordDTO>> {
        val records = parkingRecordService.getActiveParkingRecords()
        return ResponseEntity.ok(records)
    }

    @GetMapping("/vehicle/{vehicleId}")
    fun getParkingRecordsByVehicle(@PathVariable vehicleId: Long): ResponseEntity<List<ParkingRecordDTO>> {
        val records = parkingRecordService.getParkingRecordsByVehicle(vehicleId)
        return ResponseEntity.ok(records)
    }

    @GetMapping("/user/{userId}")
    fun getParkingRecordsByUser(@PathVariable userId: Long): ResponseEntity<List<ParkingRecordDTO>> {
        val records = parkingRecordService.getParkingRecordsByUser(userId)
        return ResponseEntity.ok(records)
    }

    @GetMapping("/spot/{spotId}")
    fun getParkingRecordsBySpot(@PathVariable spotId: Long): ResponseEntity<List<ParkingRecordDTO>> {
        val records = parkingRecordService.getParkingRecordsBySpot(spotId)
        return ResponseEntity.ok(records)
    }

    @GetMapping("/summary")
    fun getParkingSummary(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime?
    ): ResponseEntity<ParkingSummaryDTO> {
        val summary = parkingRecordService.getParkingSummary(startDate, endDate)
        return ResponseEntity.ok(summary)
    }
}

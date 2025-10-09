package com.ust_internal.mallparkingsystem.repository

import com.ust_internal.mallparkingsystem.entity.ParkingRecord
import com.ust_internal.mallparkingsystem.entity.ParkingStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
interface ParkingRecordRepository : JpaRepository<ParkingRecord, Long> {

    fun findByVehicleId(vehicleId: Long): List<ParkingRecord>

    fun findByUserId(userId: Long): List<ParkingRecord>

    fun findByParkingSpotId(parkingSpotId: Long): List<ParkingRecord>

    fun findByStatus(status: ParkingStatus): List<ParkingRecord>

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.vehicle.id = :vehicleId AND pr.status = :status")
    fun findByVehicleIdAndStatus(vehicleId: Long, status: ParkingStatus): List<ParkingRecord>

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.entryTime >= :startDate AND pr.entryTime <= :endDate")
    fun findByEntryTimeBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<ParkingRecord>

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.status = 'ACTIVE'")
    fun findActiveParkingRecords(): List<ParkingRecord>

    @Query("SELECT COUNT(pr) FROM ParkingRecord pr WHERE pr.status = 'ACTIVE'")
    fun countActiveParkingRecords(): Long

    @Query("SELECT SUM(pr.totalAmount) FROM ParkingRecord pr WHERE pr.status = 'COMPLETED' AND pr.entryTime >= :startDate AND pr.entryTime <= :endDate")
    fun sumTotalAmountByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal?

    @Query("SELECT AVG(pr.durationInHours) FROM ParkingRecord pr WHERE pr.status = 'COMPLETED' AND pr.entryTime >= :startDate AND pr.entryTime <= :endDate")
    fun averageDurationByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Double?

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.parkingSpot.floor = :floor AND pr.entryTime >= :startDate AND pr.entryTime <= :endDate")
    fun findByFloorAndDateRange(floor: String, startDate: LocalDateTime, endDate: LocalDateTime): List<ParkingRecord>

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.user.id = :userId AND pr.status = 'COMPLETED' ORDER BY pr.entryTime DESC")
    fun findCompletedByUserOrderByEntryTimeDesc(userId: Long): List<ParkingRecord>

    @Query("SELECT COUNT(pr) FROM ParkingRecord pr WHERE pr.parkingSpot.id = :parkingSpotId AND pr.status = 'COMPLETED'")
    fun countCompletedRecordsByParkingSpot(parkingSpotId: Long): Long
}

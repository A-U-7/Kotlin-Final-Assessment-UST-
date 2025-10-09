package com.ust_internal.mallparkingsystem.repository

import com.ust_internal.mallparkingsystem.entity.ParkingSpot
import com.ust_internal.mallparkingsystem.entity.ParkingSpotStatus
import com.ust_internal.mallparkingsystem.entity.ParkingSpotType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ParkingSpotRepository : JpaRepository<ParkingSpot, Long> {

    fun findBySpotNumber(spotNumber: String): ParkingSpot?

    fun findByFloor(floor: String): List<ParkingSpot>

    fun findBySection(section: String): List<ParkingSpot>

    fun findBySpotType(spotType: ParkingSpotType): List<ParkingSpot>

    fun findByStatus(status: ParkingSpotStatus): List<ParkingSpot>

    fun findByIsActive(isActive: Boolean): List<ParkingSpot>

    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.status = :status AND ps.spotType = :spotType")
    fun findByStatusAndSpotType(status: ParkingSpotStatus, spotType: ParkingSpotType): List<ParkingSpot>

    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.floor = :floor AND ps.section = :section AND ps.status = :status")
    fun findByFloorAndSectionAndStatus(floor: String, section: String, status: ParkingSpotStatus): List<ParkingSpot>

    @Query("SELECT COUNT(ps) FROM ParkingSpot ps WHERE ps.status = :status")
    fun countByStatus(status: ParkingSpotStatus): Long

    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.isActive = true ORDER BY ps.floor, ps.section, ps.spotNumber")
    fun findAllActiveOrderByLocation(): List<ParkingSpot>
}

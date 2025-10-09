package com.ust_internal.mallparkingsystem.repository

import com.ust_internal.mallparkingsystem.entity.Vehicle
import com.ust_internal.mallparkingsystem.entity.VehicleType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VehicleRepository : JpaRepository<Vehicle, Long> {

    fun findByLicensePlate(licensePlate: String): Vehicle?

    fun findByLicensePlateAndIsActive(licensePlate: String, isActive: Boolean): Vehicle?

    fun findByOwnerId(ownerId: Long): List<Vehicle>

    fun findByVehicleType(vehicleType: VehicleType): List<Vehicle>

    fun findByIsActive(isActive: Boolean): List<Vehicle>

    @Query("SELECT v FROM Vehicle v WHERE v.owner.id = :ownerId AND v.isActive = true")
    fun findActiveVehiclesByOwner(ownerId: Long): List<Vehicle>

    @Query("SELECT v FROM Vehicle v WHERE v.licensePlate LIKE %:licensePlate%")
    fun findByLicensePlateContaining(licensePlate: String): List<Vehicle>
}

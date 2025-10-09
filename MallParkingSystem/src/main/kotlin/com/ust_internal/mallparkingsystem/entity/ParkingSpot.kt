package com.ust_internal.mallparkingsystem.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "parking_spots")
open class ParkingSpot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    open var spotNumber: String,

    @Column(nullable = false)
    open var floor: String,

    @Column(nullable = false)
    open var section: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var spotType: ParkingSpotType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var status: ParkingSpotStatus = ParkingSpotStatus.AVAILABLE,

    @Column(nullable = false, precision = 10, scale = 2)
    open var hourlyRate: BigDecimal,

    @Column(nullable = false)
    open var isActive: Boolean = true,

    @Column(nullable = false)
    open val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    open var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    // Default constructor for JPA
    constructor() : this(
        id = null,
        spotNumber = "",
        floor = "",
        section = "",
        spotType = ParkingSpotType.STANDARD,
        status = ParkingSpotStatus.AVAILABLE,
        hourlyRate = BigDecimal.ZERO,
        isActive = true,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    override fun toString(): String {
        return "ParkingSpot(id=$id, spotNumber='$spotNumber', floor='$floor', section='$section', status=$status)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParkingSpot

        if (id != other.id) return false
        if (spotNumber != other.spotNumber) return false
        if (floor != other.floor) return false
        if (section != other.section) return false
        if (spotType != other.spotType) return false
        if (status != other.status) return false
        if (hourlyRate != other.hourlyRate) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + spotNumber.hashCode()
        result = 31 * result + floor.hashCode()
        result = 31 * result + section.hashCode()
        result = 31 * result + spotType.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + hourlyRate.hashCode()
        result = 31 * result + isActive.hashCode()
        return result
    }
}

enum class ParkingSpotType {
    STANDARD, COMPACT, HANDICAPPED, ELECTRIC, PREMIUM
}

enum class ParkingSpotStatus {
    AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
}

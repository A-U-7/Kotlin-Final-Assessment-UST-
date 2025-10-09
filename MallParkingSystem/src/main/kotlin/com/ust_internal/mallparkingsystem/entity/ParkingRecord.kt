package com.ust_internal.mallparkingsystem.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "parking_records")
open class ParkingRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    open var vehicle: Vehicle,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_spot_id", nullable = false)
    open var parkingSpot: ParkingSpot,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: User,

    @Column(nullable = false)
    open var entryTime: LocalDateTime,

    @Column
    open var exitTime: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var status: ParkingStatus = ParkingStatus.ACTIVE,

    @Column(precision = 10, scale = 2)
    open var totalAmount: BigDecimal? = null,

    @Column
    open var durationInHours: Double? = null,

    @Column(nullable = false)
    open val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    open var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    // Default constructor for JPA
    constructor() : this(
        id = null,
        vehicle = Vehicle(),
        parkingSpot = ParkingSpot(),
        user = User(),
        entryTime = LocalDateTime.now(),
        exitTime = null,
        status = ParkingStatus.ACTIVE,
        totalAmount = null,
        durationInHours = null,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    override fun toString(): String {
        return "ParkingRecord(id=$id, vehicle=${vehicle.licensePlate}, parkingSpot=${parkingSpot.spotNumber}, status=$status)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParkingRecord

        if (id != other.id) return false
        if (vehicle.id != other.vehicle.id) return false
        if (parkingSpot.id != other.parkingSpot.id) return false
        if (user.id != other.user.id) return false
        if (entryTime != other.entryTime) return false
        if (exitTime != other.exitTime) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (vehicle.id?.hashCode() ?: 0)
        result = 31 * result + (parkingSpot.id?.hashCode() ?: 0)
        result = 31 * result + (user.id?.hashCode() ?: 0)
        result = 31 * result + entryTime.hashCode()
        result = 31 * result + (exitTime?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        return result
    }
}

enum class ParkingStatus {
    ACTIVE, COMPLETED, CANCELLED, OVERDUE
}

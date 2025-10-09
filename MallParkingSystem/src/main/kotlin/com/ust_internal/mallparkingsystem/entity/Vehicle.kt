package com.ust_internal.mallparkingsystem.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "vehicles")
open class Vehicle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    open var licensePlate: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var vehicleType: VehicleType,

    @Column(nullable = false)
    open var make: String,

    @Column(nullable = false)
    open var model: String,

    @Column(nullable = false)
    open var color: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    open var owner: User,

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
        licensePlate = "",
        vehicleType = VehicleType.CAR,
        make = "",
        model = "",
        color = "",
        owner = User(),
        isActive = true,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    override fun toString(): String {
        return "Vehicle(id=$id, licensePlate='$licensePlate', vehicleType=$vehicleType, make='$make', model='$model')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vehicle

        if (id != other.id) return false
        if (licensePlate != other.licensePlate) return false
        if (vehicleType != other.vehicleType) return false
        if (make != other.make) return false
        if (model != other.model) return false
        if (color != other.color) return false
        if (owner.id != other.owner.id) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + licensePlate.hashCode()
        result = 31 * result + vehicleType.hashCode()
        result = 31 * result + make.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + (owner.id?.hashCode() ?: 0)
        result = 31 * result + isActive.hashCode()
        return result
    }
}

enum class VehicleType {
    CAR, MOTORCYCLE, TRUCK, VAN
}

package com.ust_internal.employeeappraisalpromotionsystem.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employee_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("EMPLOYEE")
open class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    @Column(name = "years_of_experience", nullable = false)
    var yearsOfExperience: Int,

    @Column(nullable = false)
    var rating: Double,

    @Column(name = "promotion_status")
    var promotionStatus: String = "NOT_ELIGIBLE",

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Observable property for promotion status changes
    var promotionStatusObservable: String
        get() = promotionStatus
        set(value) {
            if (promotionStatus != value) {
                println("Promotion status changed for ${name}: $promotionStatus -> $value")
                promotionStatus = value
                updatedAt = LocalDateTime.now()
            }
        }

    fun isEligibleForPromotion(): Boolean {
        return rating >= 4.5 || yearsOfExperience >= 5
    }

    override fun toString(): String {
        return "Employee(id=$id, name='$name', yearsOfExperience=$yearsOfExperience, rating=$rating, promotionStatus='$promotionStatus')"
    }
}

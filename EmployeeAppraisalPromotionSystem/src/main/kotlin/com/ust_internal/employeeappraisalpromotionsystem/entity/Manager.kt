package com.ust_internal.employeeappraisalpromotionsystem.entity

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("MANAGER")
class Manager(
    id: Long? = null,
    name: String,
    yearsOfExperience: Int,
    rating: Double,
    promotionStatus: String = "NOT_ELIGIBLE",
    createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),
    updatedAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
) : Employee(id, name, yearsOfExperience, rating, promotionStatus, createdAt, updatedAt) {

    // Additional responsibility for managers
    val teamLead: String = "Team Lead"

    override fun toString(): String {
        return "Manager(id=$id, name='$name', yearsOfExperience=$yearsOfExperience, rating=$rating, promotionStatus='$promotionStatus', teamLead='$teamLead')"
    }
}

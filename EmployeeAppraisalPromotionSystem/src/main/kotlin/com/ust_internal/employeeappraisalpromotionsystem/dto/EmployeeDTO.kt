package com.ust_internal.employeeappraisalpromotionsystem.dto

data class EmployeeDTO(
    val id: Long?,
    val name: String,
    val yearsOfExperience: Int,
    val rating: Double,
    val promotionStatus: String,
    val createdAt: String?,
    val updatedAt: String?
)

data class PromotionRequestDTO(
    val name: String,
    val yearsOfExperience: Int,
    val rating: Double
)

data class EligibilityCriteriaDTO(
    val minRating: Double = 4.5,
    val minExperience: Int = 5
)

data class EligibilityCheckResponseDTO(
    val message: String,
    val criteria: EligibilityCriteriaDTO,
    val eligibleEmployees: List<EmployeeDTO>,
    val totalChecked: Int,
    val eligibleCount: Int
)

data class PromotionResponseDTO(
    val message: String,
    val eligibleEmployees: List<EmployeeDTO>
)

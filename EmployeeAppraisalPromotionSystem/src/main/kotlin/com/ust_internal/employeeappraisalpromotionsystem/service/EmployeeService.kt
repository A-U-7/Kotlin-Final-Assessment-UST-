package com.ust_internal.employeeappraisalpromotionsystem.service

import com.ust_internal.employeeappraisalpromotionsystem.dto.*
import com.ust_internal.employeeappraisalpromotionsystem.entity.Employee
import com.ust_internal.employeeappraisalpromotionsystem.entity.Manager
import com.ust_internal.employeeappraisalpromotionsystem.repository.EmployeeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmployeeService(
    val employeeRepository: EmployeeRepository
) {

    // Function with default args to calculate promotions
    fun calculatePromotion(
        employee: Employee,
        minRating: Double = 4.5,
        minExperience: Int = 5
    ): Boolean {
        val eligible = employee.rating >= minRating || employee.yearsOfExperience >= minExperience

        // Update promotion status using observable property
        employee.promotionStatusObservable = if (eligible) "ELIGIBLE" else "NOT_ELIGIBLE"

        return eligible
    }

    // Add employee to collection
    @Transactional
    fun addEmployee(employeeDTO: PromotionRequestDTO): Employee {
        val employee = if (employeeDTO.name.startsWith("Mgr")) {
            Manager(
                name = employeeDTO.name,
                yearsOfExperience = employeeDTO.yearsOfExperience,
                rating = employeeDTO.rating
            )
        } else {
            Employee(
                name = employeeDTO.name,
                yearsOfExperience = employeeDTO.yearsOfExperience,
                rating = employeeDTO.rating
            )
        }

        // Calculate and set initial promotion status
        calculatePromotion(employee)

        return employeeRepository.save(employee)
    }

    // Get all employees eligible for promotion
    fun getEligibleEmployees(): List<EmployeeDTO> {
        val eligibleEmployees = employeeRepository.findByPromotionStatus("ELIGIBLE")
        return eligibleEmployees.map { convertToDTO(it) }
    }

    // Get all employees
    fun getAllEmployees(): List<EmployeeDTO> {
        val allEmployees = employeeRepository.findAll()
        return allEmployees.map { convertToDTO(it) }
    }

    // Use functional programming (map/filter) to shortlist promoted employees
    fun getShortlistedPromotedEmployees(): List<EmployeeDTO> {
        return employeeRepository.findAll().filter { it.promotionStatus == "ELIGIBLE" }
            .map { convertToDTO(it) }
    }

    // Check eligibility with custom criteria and return detailed response
    fun checkEligibilityWithCriteria(criteria: EligibilityCriteriaDTO): EligibilityCheckResponseDTO {
        val allEmployees = employeeRepository.findAll()
        val eligibleEmployees = mutableListOf<EmployeeDTO>()

        // Check each employee against the provided criteria
        allEmployees.forEach { employee ->
            val isEligible = employee.rating >= criteria.minRating || employee.yearsOfExperience >= criteria.minExperience
            if (isEligible) {
                eligibleEmployees.add(convertToDTO(employee))
                employee.promotionStatusObservable = "ELIGIBLE"
            } else {
                employee.promotionStatusObservable = "NOT_ELIGIBLE"
            }
        }

        return EligibilityCheckResponseDTO(
            message = "Eligibility check completed for ${allEmployees.size} employees using criteria: minRating=${criteria.minRating}, minExperience=${criteria.minExperience}",
            criteria = criteria,
            eligibleEmployees = eligibleEmployees,
            totalChecked = allEmployees.size,
            eligibleCount = eligibleEmployees.size
        )
    }

    // Calculate promotions for all employees
    fun calculatePromotionsForAll(): PromotionResponseDTO {
        val allEmployees = employeeRepository.findAll()

        // Process each employee and calculate promotion eligibility
        allEmployees.forEach { employee ->
            calculatePromotion(employee)
        }

        val eligibleEmployees = getEligibleEmployees()

        return PromotionResponseDTO(
            message = "Calculated promotions for ${allEmployees.size} employees. ${eligibleEmployees.size} are eligible.",
            eligibleEmployees = eligibleEmployees
        )
    }

    // Convert entity to DTO
    private fun convertToDTO(employee: Employee): EmployeeDTO {
        return EmployeeDTO(
            id = employee.id,
            name = employee.name,
            yearsOfExperience = employee.yearsOfExperience,
            rating = employee.rating,
            promotionStatus = employee.promotionStatus,
            createdAt = employee.createdAt.toString(),
            updatedAt = employee.updatedAt.toString()
        )
    }
}

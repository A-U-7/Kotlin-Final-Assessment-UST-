package com.ust_internal.employeeappraisalpromotionsystem.controller

import com.ust_internal.employeeappraisalpromotionsystem.dto.EligibilityCriteriaDTO
import com.ust_internal.employeeappraisalpromotionsystem.dto.EligibilityCheckResponseDTO
import com.ust_internal.employeeappraisalpromotionsystem.dto.EmployeeDTO
import com.ust_internal.employeeappraisalpromotionsystem.dto.PromotionRequestDTO
import com.ust_internal.employeeappraisalpromotionsystem.dto.PromotionResponseDTO
import com.ust_internal.employeeappraisalpromotionsystem.service.EmployeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping
    fun addEmployee(@RequestBody employeeDTO: PromotionRequestDTO): ResponseEntity<String> {
        try {
            val employee = employeeService.addEmployee(employeeDTO)
            return ResponseEntity.ok("Employee ${employee.name} added successfully with ID: ${employee.id}")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error adding employee: ${e.message}")
        }
    }

    @GetMapping
    fun getAllEmployees(): ResponseEntity<List<EmployeeDTO>> {
        try {
            val employees = employeeService.getAllEmployees()
            return ResponseEntity.ok(employees)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/eligible")
    fun getEligibleEmployees(): ResponseEntity<List<EmployeeDTO>> {
        try {
            val eligibleEmployees = employeeService.getEligibleEmployees()
            return ResponseEntity.ok(eligibleEmployees)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/promotions")
    fun calculatePromotions(): ResponseEntity<PromotionResponseDTO> {
        try {
            val response = employeeService.calculatePromotionsForAll()
            return ResponseEntity.ok(response)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/check-eligibility")
    fun checkPromotionEligibility(@RequestBody criteria: EligibilityCriteriaDTO): ResponseEntity<EligibilityCheckResponseDTO> {
        try {
            val response = employeeService.checkEligibilityWithCriteria(criteria)
            return ResponseEntity.ok(response)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}

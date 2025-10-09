package com.ust_internal.employeeappraisalpromotionsystem.repository

import com.ust_internal.employeeappraisalpromotionsystem.entity.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {
    fun findByPromotionStatus(promotionStatus: String): List<Employee>
    fun findByYearsOfExperienceGreaterThanEqual(years: Int): List<Employee>
    fun findByRatingGreaterThanEqual(rating: Double): List<Employee>
}

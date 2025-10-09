package com.ust_internal.employeeappraisalpromotionsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.autoconfigure.domain.EntityScan

@SpringBootApplication
@EnableJpaRepositories("com.ust_internal.employeeappraisalpromotionsystem.repository")
@EntityScan("com.ust_internal.employeeappraisalpromotionsystem.entity")
class EmployeeAppraisalPromotionSystemApplication

fun main(args: Array<String>) {
    runApplication<EmployeeAppraisalPromotionSystemApplication>(*args)
}

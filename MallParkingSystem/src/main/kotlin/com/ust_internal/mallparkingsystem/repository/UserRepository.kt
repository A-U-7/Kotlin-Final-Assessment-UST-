package com.ust_internal.mallparkingsystem.repository

import com.ust_internal.mallparkingsystem.entity.User
import com.ust_internal.mallparkingsystem.entity.UserType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun findByEmailAndIsActive(email: String, isActive: Boolean): User?

    fun findByUserType(userType: UserType): List<User>

    fun findByIsActive(isActive: Boolean): List<User>

    @Query("SELECT u FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    fun findUsersCreatedBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<User>
}

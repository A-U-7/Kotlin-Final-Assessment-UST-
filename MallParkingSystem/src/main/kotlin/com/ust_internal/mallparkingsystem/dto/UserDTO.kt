package com.ust_internal.mallparkingsystem.dto

import com.ust_internal.mallparkingsystem.entity.UserType
import java.time.LocalDateTime

data class UserDTO(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val userType: UserType,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CreateUserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val userType: UserType = UserType.CUSTOMER
)

data class UpdateUserRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val userType: UserType? = null,
    val isActive: Boolean? = null
)

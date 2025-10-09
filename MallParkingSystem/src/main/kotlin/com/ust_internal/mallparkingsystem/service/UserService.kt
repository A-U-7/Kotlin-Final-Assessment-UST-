package com.ust_internal.mallparkingsystem.service

import com.ust_internal.mallparkingsystem.dto.CreateUserRequest
import com.ust_internal.mallparkingsystem.dto.UpdateUserRequest
import com.ust_internal.mallparkingsystem.dto.UserDTO
import com.ust_internal.mallparkingsystem.entity.User
import com.ust_internal.mallparkingsystem.exception.ResourceAlreadyExistsException
import com.ust_internal.mallparkingsystem.exception.ResourceNotFoundException
import com.ust_internal.mallparkingsystem.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserDTO> {
        return userRepository.findAll().map { toDTO(it) }
    }

    fun getUserById(id: Long): UserDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
        return toDTO(user)
    }

    fun getUserByEmail(email: String): UserDTO? {
        return userRepository.findByEmail(email)?.let { toDTO(it) }
    }

    fun createUser(request: CreateUserRequest): UserDTO {
        // Check if user with email already exists
        userRepository.findByEmail(request.email)?.let {
            throw ResourceAlreadyExistsException("User already exists with email: ${request.email}")
        }

        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            phoneNumber = request.phoneNumber,
            userType = request.userType
        )

        val savedUser = userRepository.save(user)
        return toDTO(savedUser)
    }

    fun updateUser(id: Long, request: UpdateUserRequest): UserDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }

        val updatedUser = User(
            id = user.id,
            firstName = request.firstName ?: user.firstName,
            lastName = request.lastName ?: user.lastName,
            email = user.email, // Email should not be changed
            phoneNumber = request.phoneNumber ?: user.phoneNumber,
            userType = request.userType ?: user.userType,
            isActive = request.isActive ?: user.isActive,
            createdAt = user.createdAt,
            updatedAt = LocalDateTime.now()
        )

        val savedUser = userRepository.save(updatedUser)
        return toDTO(savedUser)
    }

    fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }

        userRepository.delete(user)
    }

    fun getUsersByType(userType: com.ust_internal.mallparkingsystem.entity.UserType): List<UserDTO> {
        return userRepository.findByUserType(userType).map { toDTO(it) }
    }

    fun getActiveUsers(): List<UserDTO> {
        return userRepository.findByIsActive(true).map { toDTO(it) }
    }

    private fun toDTO(user: User): UserDTO {
        return UserDTO(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phoneNumber = user.phoneNumber,
            userType = user.userType,
            isActive = user.isActive,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}

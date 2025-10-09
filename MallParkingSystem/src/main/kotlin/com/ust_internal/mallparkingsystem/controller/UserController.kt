package com.ust_internal.mallparkingsystem.controller

import com.ust_internal.mallparkingsystem.dto.CreateUserRequest
import com.ust_internal.mallparkingsystem.dto.UpdateUserRequest
import com.ust_internal.mallparkingsystem.dto.UserDTO
import com.ust_internal.mallparkingsystem.entity.UserType
import com.ust_internal.mallparkingsystem.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDTO> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<UserDTO> {
        val user = userService.getUserByEmail(email)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserDTO> {
        val user = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserDTO> {
        val user = userService.updateUser(id, request)
        return ResponseEntity.ok(user)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/type/{userType}")
    fun getUsersByType(@PathVariable userType: UserType): ResponseEntity<List<UserDTO>> {
        val users = userService.getUsersByType(userType)
        return ResponseEntity.ok(users)
    }

    @GetMapping("/active")
    fun getActiveUsers(): ResponseEntity<List<UserDTO>> {
        val users = userService.getActiveUsers()
        return ResponseEntity.ok(users)
    }
}

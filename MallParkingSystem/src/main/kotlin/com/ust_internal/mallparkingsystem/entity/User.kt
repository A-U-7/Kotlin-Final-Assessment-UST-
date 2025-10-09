package com.ust_internal.mallparkingsystem.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    open var firstName: String,

    @Column(nullable = false)
    open var lastName: String,

    @Column(nullable = false, unique = true)
    open var email: String,

    @Column(nullable = false)
    open var phoneNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var userType: UserType = UserType.CUSTOMER,

    @Column(nullable = false)
    open var isActive: Boolean = true,

    @Column(nullable = false)
    open val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    open var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    // Default constructor for JPA
    constructor() : this(
        id = null,
        firstName = "",
        lastName = "",
        email = "",
        phoneNumber = "",
        userType = UserType.CUSTOMER,
        isActive = true,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    override fun toString(): String {
        return "User(id=$id, firstName='$firstName', lastName='$lastName', email='$email')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (phoneNumber != other.phoneNumber) return false
        if (userType != other.userType) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + userType.hashCode()
        result = 31 * result + isActive.hashCode()
        return result
    }
}

enum class UserType {
    CUSTOMER, ADMIN, STAFF
}

package com.ust_internal.mallparkingsystem.exception

class ResourceNotFoundException(message: String) : RuntimeException(message)

class ResourceAlreadyExistsException(message: String) : RuntimeException(message)

class InvalidOperationException(message: String) : RuntimeException(message)

class ParkingSpotUnavailableException(message: String) : RuntimeException(message)

class VehicleAlreadyParkedException(message: String) : RuntimeException(message)

class UserNotActiveException(message: String) : RuntimeException(message)

class ValidationException(message: String) : RuntimeException(message)

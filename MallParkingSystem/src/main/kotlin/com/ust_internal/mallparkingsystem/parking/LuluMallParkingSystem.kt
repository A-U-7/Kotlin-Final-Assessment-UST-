package com.ust_internal.mallparkingsystem.parking

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

// Enums for vehicle types
enum class VehicleType {
    CAR, BIKE, EMPLOYEE_VEHICLE
}

// Data class for vehicles in parking
data class ParkedVehicle(
    val id: String,
    val type: VehicleType,
    val licensePlate: String,
    val entryTime: Long = System.currentTimeMillis()
)

// Vetoable delegate for slot management
class SlotManager(private val maxSlots: Int) {
    private var currentCount = 0

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return currentCount
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value > maxSlots) {
            throw IllegalStateException("Cannot exceed maximum slots: $maxSlots")
        }
        currentCount = value
    }
}

// Parking Management System
class LuluMallParkingSystem {
    // Fixed slot limits using vetoable delegates
    var carSlots: Int by Delegates.vetoable(0) { _, oldValue, newValue ->
        if (newValue <= 50 && newValue >= 0) true else {
            println("Vetoed: Cannot set car slots to $newValue (max: 50)")
            false
        }
    }

    var bikeSlots: Int by Delegates.vetoable(0) { _, oldValue, newValue ->
        if (newValue <= 100 && newValue >= 0) true else {
            println("Vetoed: Cannot set bike slots to $newValue (max: 100)")
            false
        }
    }

    var employeeSlots: Int by Delegates.vetoable(0) { _, oldValue, newValue ->
        if (newValue <= 30 && newValue >= 0) true else {
            println("Vetoed: Cannot set employee slots to $newValue (max: 30)")
            false
        }
    }

    // Parking list to store all parked vehicles
    private val parkingList = mutableListOf<ParkedVehicle>()

    // Function to add single vehicle with slot validation
    fun parkVehicle(vehicle: ParkedVehicle): Boolean {
        return when (vehicle.type) {
            VehicleType.CAR -> {
                if (carSlots < 50) {
                    carSlots++
                    parkingList.add(vehicle)
                    println("Car parked successfully. License: ${vehicle.licensePlate}")
                    true
                } else {
                    println("Car parking full! Cannot park ${vehicle.licensePlate}")
                    false
                }
            }
            VehicleType.BIKE -> {
                if (bikeSlots < 100) {
                    bikeSlots++
                    parkingList.add(vehicle)
                    println("Bike parked successfully. License: ${vehicle.licensePlate}")
                    true
                } else {
                    println(" Bike parking full! Cannot park ${vehicle.licensePlate}")
                    false
                }
            }
            VehicleType.EMPLOYEE_VEHICLE -> {
                if (employeeSlots < 30) {
                    employeeSlots++
                    parkingList.add(vehicle)
                    println("Employee vehicle parked successfully. License: ${vehicle.licensePlate}")
                    true
                } else {
                    println("Employee parking full! Cannot park ${vehicle.licensePlate}")
                    false
                }
            }
        }
    }

    // Function with default arguments for bulk adding vehicles
    fun bulkAddVehicles(
        vehicles: List<ParkedVehicle>,
        delayBetweenVehicles: Long = 1000L // Default 1 second delay
    ): Int {
        var successCount = 0

        for ((index, vehicle) in vehicles.withIndex()) {
            try {
                if (parkVehicle(vehicle)) {
                    successCount++
                }

                // Add delay between vehicles (except for the last one)
                if (index < vehicles.size - 1) {
                    Thread.sleep(delayBetweenVehicles)
                }
            } catch (e: Exception) {
                println(" Error parking vehicle ${vehicle.licensePlate}: ${e.message}")
            }
        }

        println(" Bulk operation completed. Successfully parked: $successCount/${vehicles.size} vehicles")
        return successCount
    }

    // Function to remove vehicle from parking
    fun removeVehicle(licensePlate: String): Boolean {
        val vehicle = parkingList.find { it.licensePlate == licensePlate }

        return if (vehicle != null) {
            when (vehicle.type) {
                VehicleType.CAR -> carSlots--
                VehicleType.BIKE -> bikeSlots--
                VehicleType.EMPLOYEE_VEHICLE -> employeeSlots--
            }

            parkingList.remove(vehicle)
            println(" Vehicle removed successfully. License: $licensePlate")
            true
        } else {
            println(" Vehicle not found with license: $licensePlate")
            false
        }
    }

    // Use map/filter to print current parking statistics
    fun printParkingStatistics() {
        val carCount = parkingList.filter { it.type == VehicleType.CAR }.size
        val bikeCount = parkingList.filter { it.type == VehicleType.BIKE }.size
        val employeeCount = parkingList.filter { it.type == VehicleType.EMPLOYEE_VEHICLE }.size

        println("\n LULU MALL PARKING STATISTICS")
        println("-".repeat(40))
        println(" Cars parked: $carCount/50")
        println(" Bikes parked: $bikeCount/100")
        println(" Employee vehicles parked: $employeeCount/30")
        println("-".repeat(40))
        println(" Total vehicles parked: ${parkingList.size}")

        // Show slot availability
        println(" SLOT AVAILABILITY")
        println(" Car slots available: ${50 - carCount}")
        println(" Bike slots available: ${100 - bikeCount}")
        println(" Employee slots available: ${30 - employeeCount}")
    }

    // Get current parking list
    fun getParkingList(): List<ParkedVehicle> = parkingList.toList()

    // Check if parking is full for a specific type
    fun isParkingFull(vehicleType: VehicleType): Boolean {
        return when (vehicleType) {
            VehicleType.CAR -> carSlots >= 50
            VehicleType.BIKE -> bikeSlots >= 100
            VehicleType.EMPLOYEE_VEHICLE -> employeeSlots >= 30
        }
    }

    // Reset all parking (for testing purposes)
    fun resetParking() {
        parkingList.clear()
        carSlots = 0
        bikeSlots = 0
        employeeSlots = 0
        println(" All parking has been reset")
    }
}

// Main function demonstrating the system
fun main() {
    val parkingSystem = LuluMallParkingSystem()

    println(" LULU MALL PARKING MANAGEMENT SYSTEM")
    println("-".repeat(40))

    // Demonstrate vetoable delegates
    println("\n Testing vetoable delegates (should be vetoed):")
    try {
        parkingSystem.carSlots = 60  // Should be vetoed
    } catch (e: Exception) {
        println("Caught exception: ${e.message}")
    }

    // Add some vehicles
    println("\n Adding vehicles to parking:")

    val car1 = ParkedVehicle("V001", VehicleType.CAR, "ABC123")
    val car2 = ParkedVehicle("V002", VehicleType.CAR, "XYZ789")
    val bike1 = ParkedVehicle("V003", VehicleType.BIKE, "BIKE001")
    val employee1 = ParkedVehicle("V004", VehicleType.EMPLOYEE_VEHICLE, "EMP001")

    parkingSystem.parkVehicle(car1)
    parkingSystem.parkVehicle(car2)
    parkingSystem.parkVehicle(bike1)
    parkingSystem.parkVehicle(employee1)

    // Demonstrate bulk adding with default arguments
    println("\n Bulk adding vehicles (with 500ms delay):")
    val bulkVehicles = listOf(
        ParkedVehicle("V005", VehicleType.CAR, "CAR001"),
        ParkedVehicle("V006", VehicleType.BIKE, "BIKE002"),
        ParkedVehicle("V007", VehicleType.EMPLOYEE_VEHICLE, "EMP002"),
        ParkedVehicle("V008", VehicleType.CAR, "CAR002")
    )

    parkingSystem.bulkAddVehicles(bulkVehicles, 500L)

    // Print statistics using map/filter
    println("\n Current parking statistics:")
    parkingSystem.printParkingStatistics()

    // Demonstrate removing a vehicle
    println("\n Removing a vehicle:")
    parkingSystem.removeVehicle("CAR001")

    // Final statistics
    println("\n Final parking statistics:")
    parkingSystem.printParkingStatistics()

    // Demonstrate parking full scenario
    println("\n Testing full parking scenario:")
    // Try to add more cars than available
    repeat(50) { i ->
        val vehicle = ParkedVehicle("V${i + 10}", VehicleType.CAR, "FULL$i")
        parkingSystem.parkVehicle(vehicle)
    }

    println("\n Final system state:")
    parkingSystem.printParkingStatistics()
}

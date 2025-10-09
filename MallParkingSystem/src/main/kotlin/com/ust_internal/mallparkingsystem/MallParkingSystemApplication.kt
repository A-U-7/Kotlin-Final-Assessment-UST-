package com.ust_internal.mallparkingsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MallParkingSystemApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<MallParkingSystemApplication>(*args)
        }
    }
}

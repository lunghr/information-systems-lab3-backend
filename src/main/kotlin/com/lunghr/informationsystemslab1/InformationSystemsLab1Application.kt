package com.lunghr.informationsystemslab1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InformationSystemsLab1Application

fun main(args: Array<String>) {
//    val dotenv = Dotenv.configure().load()
//    dotenv.entries().forEach { entry ->
//        System.setProperty(entry.key, entry.value)
//    }
//    System.out.println("DB_URL: " + System.getenv("DB_URL"));
    runApplication<InformationSystemsLab1Application>(*args)
}

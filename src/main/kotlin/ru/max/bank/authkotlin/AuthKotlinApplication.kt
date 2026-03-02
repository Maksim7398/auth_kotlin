package ru.max.bank.authkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan(basePackages = ["ru.max.bank"])
@SpringBootApplication(scanBasePackages = ["ru.max.bank"])
class AuthKotlinApplication

fun main(args: Array<String>) {
    runApplication<AuthKotlinApplication>(*args)
}

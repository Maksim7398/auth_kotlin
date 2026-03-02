package ru.max.bank.authkotlin

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import ru.max.bank.authkotlin.config.properties.BaseProperties

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackageClasses = [BaseProperties::class])
class AuthApplication {

    fun main(args: Array<String>) {
        SpringApplication.run(AuthApplication::class.java, *args)
    }
}
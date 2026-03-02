package ru.max.bank.authkotlin.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import ru.max.bank.authkotlin.config.properties.BaseProperties.Companion.PROPERTIES

@Validated
@ConfigurationProperties(value = PROPERTIES)
class BaseProperties{
    companion object {
        const val PROPERTIES = "max.bank.auth"
    }
}
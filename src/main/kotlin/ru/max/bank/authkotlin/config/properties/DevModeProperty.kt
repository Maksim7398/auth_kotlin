package ru.max.bank.authkotlin.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.max.bank.authkotlin.config.properties.BaseProperties.Companion.PROPERTIES

@ConfigurationProperties(value = "$PROPERTIES.dev")
data class DevModeProperty(
    val mode: Boolean
)

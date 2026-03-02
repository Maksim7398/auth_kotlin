package ru.max.bank.authkotlin.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.max.bank.authkotlin.config.properties.BaseProperties.Companion.PROPERTIES

@ConfigurationProperties(prefix = "$PROPERTIES.keycloak.client")
data class KeycloakClientProperties(
    val keycloakClientId: String,
    val keycloakClientSecret: String,
)

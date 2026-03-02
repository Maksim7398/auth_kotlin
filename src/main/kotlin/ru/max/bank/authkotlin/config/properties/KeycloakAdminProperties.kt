package ru.max.bank.authkotlin.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.max.bank.authkotlin.config.properties.BaseProperties.Companion.PROPERTIES

@ConfigurationProperties(value = "$PROPERTIES.keycloak.admin")
data class KeycloakAdminProperties(

    val adminRealm: String,

    val clientId: String,

    val username: String,

    val password: String,

    val url: String,

    val realm: String,

    val clientSecret: String
) {
}
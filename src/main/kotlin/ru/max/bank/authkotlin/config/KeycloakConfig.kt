package ru.max.bank.authkotlin.config

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.max.bank.authkotlin.config.properties.KeycloakAdminProperties

@Configuration
class KeycloakConfig(
    private val keycloakAdminProperties: KeycloakAdminProperties
) {
    @Bean
    fun keyCloak(): Keycloak? {
        val build = KeycloakBuilder.builder()
            .serverUrl(keycloakAdminProperties.url)
            .realm(keycloakAdminProperties.adminRealm)
            .clientId(keycloakAdminProperties.clientId)
            .username(keycloakAdminProperties.username)
            .password(keycloakAdminProperties.password)
            .grantType(OAuth2Constants.PASSWORD)
            .clientSecret(keycloakAdminProperties.clientSecret)
            .build()
        return build
    }
}
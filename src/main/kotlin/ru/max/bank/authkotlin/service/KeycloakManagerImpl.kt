package ru.max.bank.authkotlin.service

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.UsersResource
import org.springframework.stereotype.Service
import ru.max.bank.authkotlin.config.properties.KeycloakAdminProperties

@Service
class KeycloakManagerImpl(
    private val keycloak: Keycloak,
    private val keycloakAdminProperties: KeycloakAdminProperties
) : KeycloakManager{

    override fun getUserResource(): UsersResource {
        val realm = keycloakAdminProperties.realm
        val realmResource = keycloak.realm(realm)
        return realmResource.users()
    }
}
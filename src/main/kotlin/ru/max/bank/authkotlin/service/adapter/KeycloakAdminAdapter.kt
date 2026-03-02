package ru.max.bank.authkotlin.service.adapter

import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import java.util.*

interface KeycloakAdminAdapter {

    fun findByUsername(email: String?): Optional<UserRepresentation>

    fun createUser(createRequest: UserRepresentation?): Boolean

    fun updateUser(updateRequest: UserRepresentation?)

    fun findByExternalId(externalId: UUID?): Optional<UserRepresentation>

    fun resetPassword(user: UserRepresentation?, credentials: CredentialRepresentation?)
}
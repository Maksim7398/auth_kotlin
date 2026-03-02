package ru.max.bank.authkotlin.service.adapter

import jakarta.ws.rs.core.Response
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service
import ru.max.bank.authkotlin.exception.InternalServerException.Companion.serverError
import ru.max.bank.authkotlin.service.KeycloakManager
import java.util.Optional
import java.util.UUID

@Service
class KeycloakAdminAdapterImpl (
    private val keycloakManager: KeycloakManager
) : KeycloakAdminAdapter {

    companion object {
        private const val SEARCH_QUERY_FORMAT = "%s:%s"
    }

    override fun findByUsername(email: String?): Optional<UserRepresentation> {
        return try {
            val user = keycloakManager.getUserResource().search(email, true).firstOrNull()
            Optional.ofNullable(user)
        } catch (e: Exception) {
//            log.error("Keycloak connection failed", e)
            throw e
        }
    }

    override fun createUser(createRequest: UserRepresentation?): Boolean {
        return runCatching {
            keycloakManager.getUserResource().create(createRequest).use { response ->
                response.statusInfo.family == Response.Status.Family.SUCCESSFUL
            }
        }.getOrElse {
            throw serverError("Не удалось создать аккаунт пользователя, попробуйте позже")
        }
    }

    override fun updateUser(updateRequest: UserRepresentation?) {
        runCatching {
            getUserResourceById(updateRequest!!.id).update(updateRequest)
        }.onFailure {
            throw serverError("Не удалось обновить аккаунт пользователя, попробуйте позже")
        }
    }

    override fun findByExternalId(externalId: UUID?): Optional<UserRepresentation> {
        return runCatching {
            val query = SEARCH_QUERY_FORMAT.format("EXTERNAL_ID", externalId)
            val usersResource = keycloakManager.getUserResource()
            usersResource.searchByAttributes(query).firstOrNull()
        }.fold(
            onSuccess = { Optional.ofNullable(it) },
            onFailure = {
                throw serverError("Не удалось найти аккаунт пользователя, попробуйте позже")
            }
        )
    }

    override fun resetPassword(user: UserRepresentation?, credentials: CredentialRepresentation?) {
        runCatching {
            getUserResourceById(user!!.id).resetPassword(credentials)
        }.onFailure {
            throw serverError("Не удалось сбросить пароль пользователя, попробуйте позже")
        }
    }

    private fun getUserResourceById(userId: String): UserResource {
        return keycloakManager.getUserResource().get(userId)
    }
}
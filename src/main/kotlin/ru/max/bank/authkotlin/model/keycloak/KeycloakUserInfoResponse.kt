package ru.max.bank.authkotlin.model.keycloak

import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakUserInfoResponse(

    val email: String,
    @JsonProperty(value = "external_id")
    val externalId: String,
    @JsonProperty(value = "user_permissions")
    val userPermission: List<String>,
)

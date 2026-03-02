package ru.max.bank.authkotlin.model.keycloak

import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakLogoutRequest(
    @JsonProperty("client_id")
    val clientId: String? = null,
    @JsonProperty("client_secret")
    val clientSecret: String? = null,
    @JsonProperty("refresh_token")
    val refreshToken: String? = null,
)

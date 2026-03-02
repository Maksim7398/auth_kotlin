package ru.max.bank.authkotlin.model.keycloak

import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.oauth2.sdk.GrantType

data class KeycloakRefreshRequest(

    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String,
    @JsonProperty("grant_type")
    val grantType: String,
    @JsonProperty("refresh_token")
    val refreshToken: String? = null
)

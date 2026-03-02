package ru.max.bank.authkotlin.model.keycloak

import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakClientCredentialsRequest(
    @JsonProperty("grant_type")
    val grantType: String,
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String,
){

}

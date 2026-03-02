package ru.max.bank.authkotlin.model.keycloak

import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakTokenExchangeRequest(
    @JsonProperty("grant_type")
    var grantType: String,
    @JsonProperty("client_id")
    var clientId: String,
    @JsonProperty("client_secret")
    var clientSecret: String,
    @JsonProperty("subject_token")
    var subjectToken: String,
    @JsonProperty("request_subject")
    var requestedSubject: String,
)

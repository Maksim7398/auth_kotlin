package ru.max.bank.authkotlin.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class RegistrationRequest(
    @JsonProperty("externalId")
    val externalId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String
)

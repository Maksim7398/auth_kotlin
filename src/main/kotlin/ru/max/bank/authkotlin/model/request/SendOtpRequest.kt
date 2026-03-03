package ru.max.bank.authkotlin.model.request

import jakarta.validation.constraints.NotBlank

data class SendOtpRequest(
    @NotBlank
    val phoneNumber: String,
)

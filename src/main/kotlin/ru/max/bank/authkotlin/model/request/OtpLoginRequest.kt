package ru.max.bank.authkotlin.model.request

data class OtpLoginRequest(
    val email: String,
    val operationCode: String
)

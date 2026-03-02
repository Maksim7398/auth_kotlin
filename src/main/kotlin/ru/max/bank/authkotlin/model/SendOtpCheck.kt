package ru.max.bank.authkotlin.model

data class SendOtpCheck(
    val phoneNumber: String,
    val otp: String,
)

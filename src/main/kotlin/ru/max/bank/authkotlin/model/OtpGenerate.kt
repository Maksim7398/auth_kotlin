package ru.max.bank.authkotlin.model

import ru.max.bank.otp.enums.FlowType

data class OtpGenerate(
    val receiver: String,
    val flowType: FlowType
)

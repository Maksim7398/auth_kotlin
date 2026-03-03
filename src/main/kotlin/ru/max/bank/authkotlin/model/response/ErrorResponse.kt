package ru.max.bank.authkotlin.model.response

import java.time.OffsetDateTime

data class ErrorResponse(
    val timestamp: OffsetDateTime,
    val status: Int,
    val error: String,
    val code: String?,
    val message: String,
    val path: String?,
    val details: Map<String, Any?>? = null
)


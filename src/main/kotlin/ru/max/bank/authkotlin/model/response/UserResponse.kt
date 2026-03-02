package ru.max.bank.authkotlin.model.response
import java.util.UUID

data class UserResponse(

    val email: String,
    val externalId: UUID,
    val permissions: List<String>,
)

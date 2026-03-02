package ru.max.bank.authkotlin.model

import java.util.UUID

data class UserPermissions(
    val externalId: UUID,
    val permissions: List<String>
)

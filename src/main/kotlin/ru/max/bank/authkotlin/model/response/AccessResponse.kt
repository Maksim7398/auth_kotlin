package ru.max.bank.authkotlin.model.response

data class AccessResponse(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String?,
    val refreshTokenExpiresIn: Long?,
)

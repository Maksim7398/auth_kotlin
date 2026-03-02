package ru.max.bank.authkotlin.model.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на обновление токена")
data class RefreshRequest(

    @Schema(description = "Refresh-токен сессии", requiredMode = Schema.RequiredMode.REQUIRED)
    val refreshToken: String,
)

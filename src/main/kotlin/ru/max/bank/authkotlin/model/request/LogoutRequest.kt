package ru.max.bank.authkotlin.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Запрос на логаут пользователя")
data class LogoutRequest(

    @NotBlank
    @Schema(description = "Refresh-токен сессии", requiredMode = Schema.RequiredMode.REQUIRED)
    val refreshToken: String,
)

package ru.max.bank.authkotlin.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Schema(description = "Запрос на авторизацию пользователя по паролю")
data class AuthRequest(

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9!#$%&*+\\-=?^_~]+(.[A-Za-z0-9!#$%&*+\\-=?^_~]+)*@(?![.\\-])[A-Za-z0-9-]+(.[A-Za-z0-9-]+)*(.[A-Za-z]{2,})$")
    @Schema(description = "Почта пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    val email: String,

    @NotBlank
    @Schema(description = "Пароль пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    val password: String
)

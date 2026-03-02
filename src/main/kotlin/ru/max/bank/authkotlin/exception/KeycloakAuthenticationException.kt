package ru.max.bank.authkotlin.exception

import org.springframework.http.HttpStatus

class KeycloakAuthenticationException (
    val errorCode: AuthErrorCode,
    val errorMessage: String,
    val status: HttpStatus
) : RuntimeException(errorMessage) {

    companion object {

        fun authenticationFailed(message: String): KeycloakAuthenticationException =
            KeycloakAuthenticationException(
                AuthErrorCode.AUTH_FAILED,
                message,
                HttpStatus.UNAUTHORIZED
            )
    }
}

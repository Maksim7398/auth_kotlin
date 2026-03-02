package ru.max.bank.authkotlin.exception

import org.springframework.http.HttpStatus

class InternalServerException(
    message: String,
    val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : RuntimeException(message){

    companion object {
        fun serverError(message: String): InternalServerException {
            throw InternalServerException(message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
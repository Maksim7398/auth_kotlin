package ru.max.bank.authkotlin.exception

import org.springframework.http.HttpStatus

class BadRequestException(errorCode: String, errorMessage: String, status: HttpStatus) :
    BaseException(errorCode, errorMessage, status) {

    companion object {
        fun invalidMessage(message: String, vararg args: Any?) =
            BadRequestException("BAD_REQUEST", message.format(*args), HttpStatus.BAD_REQUEST)
    }
}
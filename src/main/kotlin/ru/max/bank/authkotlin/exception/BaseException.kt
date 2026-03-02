package ru.max.bank.authkotlin.exception

import org.springframework.http.HttpStatus


open class BaseException(
    val errorCode: String,
    val errorMessage: String,
    val status: HttpStatus
)  : RuntimeException(errorMessage){
}
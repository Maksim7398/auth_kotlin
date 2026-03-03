package ru.max.bank.authkotlin.service

import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.SendOtpRequest
import ru.max.bank.authkotlin.model.response.SendOtpResponse

interface OtpService {

    suspend fun sendOtpToUserByPhoneNumber(request: SendOtpRequest): SendOtpResponse

    suspend fun checkOtpSentUserByPhoneNumber(otpCheck: SendOtpCheck): Boolean
}
package ru.max.bank.authkotlin.service

import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.SendOtpRequest
import ru.max.bank.authkotlin.model.response.SendOtpResponse

interface OtpService {

    fun sendOtpToUserByPhoneNumber(request: SendOtpRequest): SendOtpResponse

    fun checkOtpSentUserByPhoneNumber(otpCheck: SendOtpCheck): Boolean
}
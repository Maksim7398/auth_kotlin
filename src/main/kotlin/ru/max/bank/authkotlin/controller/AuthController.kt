package ru.max.bank.authkotlin.controller

import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.RefreshRequest
import ru.max.bank.authkotlin.model.request.RegistrationRequest
import ru.max.bank.authkotlin.model.request.SendOtpRequest
import ru.max.bank.authkotlin.model.response.AccessResponse
import ru.max.bank.authkotlin.model.response.SendOtpResponse

interface AuthController {

    fun loginByOtp(otpCheck: SendOtpCheck): AccessResponse?

    fun register(registrationRequest: RegistrationRequest)

    fun sendOtpToUserByPhoneNumber(request: SendOtpRequest): SendOtpResponse

    fun refresh(request: RefreshRequest?): AccessResponse?
}
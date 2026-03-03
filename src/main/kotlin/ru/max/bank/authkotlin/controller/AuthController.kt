package ru.max.bank.authkotlin.controller

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.LogoutRequest
import ru.max.bank.authkotlin.model.request.RefreshRequest
import ru.max.bank.authkotlin.model.request.RegistrationRequest
import ru.max.bank.authkotlin.model.request.SendOtpRequest
import ru.max.bank.authkotlin.model.response.AccessResponse
import ru.max.bank.authkotlin.model.response.SendOtpResponse

@Validated
interface AuthController {

    @PostMapping(value = ["/api/v1/auth/login/otp"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun loginByOtp(@Valid otpCheck: SendOtpCheck): AccessResponse?

    @PostMapping(value = ["/api/v1/auth/register"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun register(@Valid registrationRequest: RegistrationRequest)

    @PostMapping(value = ["/api/v1/auth/send-otp"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun sendOtpToUserByPhoneNumber(@Valid request: SendOtpRequest): SendOtpResponse

    @PostMapping(value = ["/api/v1/auth/refresh"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun refresh(@Valid request: RefreshRequest): AccessResponse?

    @PostMapping(value = ["/api/v1/auth/logout"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun logout(@Valid request: LogoutRequest)
}
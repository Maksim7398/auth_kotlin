package ru.max.bank.authkotlin.service

import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.AuthRequest
import ru.max.bank.authkotlin.model.request.LogoutRequest
import ru.max.bank.authkotlin.model.request.RefreshRequest
import ru.max.bank.authkotlin.model.request.RegistrationRequest
import ru.max.bank.authkotlin.model.request.SendOtpRequest
import ru.max.bank.authkotlin.model.response.AccessResponse
import ru.max.bank.authkotlin.model.response.SendOtpResponse
import ru.max.bank.authkotlin.model.response.UserResponse

interface AuthService {

    suspend fun sendOtpToUserByPhoneNumber(request: SendOtpRequest): SendOtpResponse

    fun register(registrationRequest: RegistrationRequest?)

    fun login(request: AuthRequest?): AccessResponse?

    suspend fun loginByOtp(otpCheck: SendOtpCheck): AccessResponse?

    fun logout(request: LogoutRequest)

    fun refresh(request: RefreshRequest): AccessResponse?

    fun getUserInfo(accessToken: String?): UserResponse?
}
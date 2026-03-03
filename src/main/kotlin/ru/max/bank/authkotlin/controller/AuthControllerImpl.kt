package ru.max.bank.authkotlin.controller

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.LogoutRequest
import ru.max.bank.authkotlin.model.request.RefreshRequest
import ru.max.bank.authkotlin.model.request.RegistrationRequest
import ru.max.bank.authkotlin.model.request.SendOtpRequest
import ru.max.bank.authkotlin.model.response.AccessResponse
import ru.max.bank.authkotlin.model.response.SendOtpResponse
import ru.max.bank.authkotlin.service.AuthService

@RestController
class AuthControllerImpl(
    private val authService: AuthService,
) : AuthController {

    override suspend fun loginByOtp(@RequestBody otpCheck: SendOtpCheck): AccessResponse? {
        return authService.loginByOtp(otpCheck)
    }

    override fun register(@RequestBody registrationRequest: RegistrationRequest) {
        return authService.register(registrationRequest)
    }

    override suspend fun sendOtpToUserByPhoneNumber(@RequestBody request: SendOtpRequest): SendOtpResponse {
        return authService.sendOtpToUserByPhoneNumber(request)
    }

    override fun refresh(@RequestBody request: RefreshRequest): AccessResponse? {
        return authService.refresh(request)
    }

    override fun logout(@RequestBody request: LogoutRequest) {
        return authService.logout(request)
    }
}
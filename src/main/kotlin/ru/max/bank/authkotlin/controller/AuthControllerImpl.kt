package ru.max.bank.authkotlin.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.max.bank.authkotlin.model.SendOtpCheck
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

    @PostMapping(value = ["/api/v1/auth/login/otp"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun loginByOtp(@RequestBody otpCheck: SendOtpCheck): AccessResponse? {
        return authService.loginByOtp(otpCheck)
    }

    @PostMapping(value = ["/api/v1/auth/register"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun register(@RequestBody registrationRequest: RegistrationRequest) {
        return authService.register(registrationRequest)
    }

    @PostMapping(value = ["/api/v1/auth/send-otp"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun sendOtpToUserByPhoneNumber(@RequestBody request: SendOtpRequest): SendOtpResponse {
        return authService.sendOtpToUserByPhoneNumber(request)
    }

    @PostMapping(value = ["/api/v1/auth/refresh"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun refresh(@RequestBody request: RefreshRequest?): AccessResponse? {
        return authService.refresh(request)
    }
}
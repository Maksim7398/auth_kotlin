package ru.max.bank.authkotlin.service

import org.springframework.stereotype.Service
import ru.max.bank.authkotlin.exception.BadRequestException.Companion.invalidMessage
import ru.max.bank.authkotlin.exception.KeycloakAuthenticationException.Companion.authenticationFailed
import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.*
import ru.max.bank.authkotlin.model.response.AccessResponse
import ru.max.bank.authkotlin.model.response.SendOtpResponse
import ru.max.bank.authkotlin.model.response.UserResponse
import ru.max.bank.authkotlin.service.adapter.KeycloakAdapter
import ru.max.bank.authkotlin.service.adapter.KeycloakAdminAdapter
import ru.max.bank.authkotlin.toUserRepresentation

@Service
class AuthServiceImpl(
    private val keycloakAdapter: KeycloakAdapter,
    private val keycloakAdminAdapter: KeycloakAdminAdapter,
    private val otpService: OtpService
) : AuthService {

    override suspend fun sendOtpToUserByPhoneNumber(request: SendOtpRequest): SendOtpResponse {
        return otpService.sendOtpToUserByPhoneNumber(request)
    }

    override fun register(registrationRequest: RegistrationRequest?) {
        val phoneNumber = registrationRequest?.phoneNumber
        val userByEmail = keycloakAdminAdapter.findByUsername(phoneNumber)
        if (userByEmail.isPresent) {
            throw invalidMessage("Пользователь с таким номером телефона '%s' уже существует", phoneNumber)
        }

        val userRepresentation = registrationRequest!!.toUserRepresentation()
        userRepresentation.isEmailVerified = true
        userRepresentation.isEnabled = true

        val userAttributes = mapOf(
            "EXTERNAL_ID" to listOf(registrationRequest.externalId)
        )
        userRepresentation.attributes = userAttributes

        if (!keycloakAdminAdapter.createUser(userRepresentation)) {
            throw invalidMessage("Ошибка при создании аккаунта пользователя")
        }
    }

    override fun login(request: AuthRequest?): AccessResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun loginByOtp(otpCheck: SendOtpCheck): AccessResponse? {
        val result = otpService.checkOtpSentUserByPhoneNumber(otpCheck)
        if (result) {
            val findByUsername = keycloakAdminAdapter.findByUsername(otpCheck.phoneNumber)
            if (findByUsername.isPresent) {
                val accessToken: org.keycloak.representations.AccessTokenResponse? =
                    keycloakAdapter.exchangeForUser(findByUsername.get().id)
                return AccessResponse(
                    accessToken!!.token, accessToken.expiresIn,
                    accessToken.refreshToken, accessToken.refreshExpiresIn
                )
            }
            else{
                throw authenticationFailed("Ошибка при попытке авторизации")
            }
        }else{
            throw authenticationFailed("Введён неверный код")
        }
    }

    override fun logout(request: LogoutRequest) {
        keycloakAdapter.logout(request.refreshToken)
    }

    override fun refresh(request: RefreshRequest): AccessResponse? {
        val refresh = keycloakAdapter.refresh(request.refreshToken)
        return AccessResponse(
            refresh!!.token, refresh.expiresIn,
            refresh.refreshToken, refresh.refreshExpiresIn
        )
    }

    override fun getUserInfo(accessToken: String?): UserResponse? {
        TODO("Not yet implemented")
    }


}
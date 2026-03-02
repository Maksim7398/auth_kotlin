package ru.max.bank.authkotlin.service.adapter

import org.keycloak.representations.AccessTokenResponse
import ru.max.bank.authkotlin.model.keycloak.KeycloakUserInfoResponse

interface KeycloakAdapter {

    fun login(email: String?, password: String?): AccessTokenResponse?

    fun exchangeForUser(userId: String?): AccessTokenResponse?

    fun logout(refreshToken: String?)

    fun refresh(refreshToken: String?): AccessTokenResponse?

    fun getUser(accessToken: String?): KeycloakUserInfoResponse?
}
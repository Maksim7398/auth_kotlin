package ru.max.bank.authkotlin.external

import org.keycloak.representations.AccessTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import ru.max.bank.authkotlin.model.keycloak.KeycloakUserInfoResponse

@FeignClient(name = "keycloak", url = "\${max.bank.auth.feign.keycloak-url}")
interface KeycloakClient {

    @PostMapping(value = ["/token"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getToken(@RequestBody form: MultiValueMap<String, *>): AccessTokenResponse?

    @PostMapping(value = ["/logout"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun logout(@RequestBody form: MultiValueMap<String, *>)

    @PostMapping(value = ["/token"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun refreshToken(@RequestBody form: MultiValueMap<String, *>): AccessTokenResponse?

    @GetMapping(value = ["/userinfo"])
    fun getUser(@RequestHeader(value = "Authorization") token: String?): KeycloakUserInfoResponse?

    @PostMapping(value = ["/token"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getClientCredentialsToken(@RequestBody form: MultiValueMap<String, *>): AccessTokenResponse?

    @PostMapping(value = ["/token"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun exchangeToken(@RequestBody form: MultiValueMap<String, *>): AccessTokenResponse?
}
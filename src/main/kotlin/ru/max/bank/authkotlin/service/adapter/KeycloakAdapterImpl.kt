package ru.max.bank.authkotlin.service.adapter

import feign.FeignException
import org.keycloak.representations.AccessTokenResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import ru.max.bank.authkotlin.config.properties.KeycloakClientProperties
import ru.max.bank.authkotlin.exception.InternalServerException.Companion.serverError
import ru.max.bank.authkotlin.exception.KeycloakAuthenticationException.Companion.authenticationFailed
import ru.max.bank.authkotlin.external.KeycloakClient
import ru.max.bank.authkotlin.model.keycloak.KeycloakUserInfoResponse

@Service
class KeycloakAdapterImpl(
    private val keycloakClient: KeycloakClient,
    private val keycloakClientProperties: KeycloakClientProperties,
) : KeycloakAdapter {

    private val log = LoggerFactory.getLogger(KeycloakAdapterImpl::class.java)

    companion object {
        const val GRANT_TYPE_PASSWORD: String = "password"
        const val GRANT_TYPE_REFRESH_TOKEN: String = "refresh_token"
        const val GRANT_TYPE_CLIENT_CREDENTIALS: String = "client_credentials"
        const val GRANT_TYPE_TOKEN_EXCHANGE: String =
            "urn:ietf:params:oauth:grant-type:token-exchange"
        const val TOKEN_TYPE_SUBJECT: String =
            "urn:ietf:params:oauth:token-type:access_token"
        const val TOKEN_TYPE_ACCESS_TOKEN: String =
            "urn:ietf:params:oauth:token-type:refresh_token"
    }

    override fun login(
        email: String?,
        password: String?
    ): AccessTokenResponse? {
        return try {
            val form = LinkedMultiValueMap<String, String>().apply {
                add("username", email!!)
                add("password", password!!)
                add("client_id", keycloakClientProperties.keycloakClientId)
                add("client_secret", keycloakClientProperties.keycloakClientSecret)
                add("grant_type", GRANT_TYPE_PASSWORD)
            }

            keycloakClient.getToken(form)
        } catch (exception: FeignException.FeignClientException) {
            log.warn(
                "Keycloak login failed (status={}, clientId={}, username={})",
                exception.status(),
                keycloakClientProperties.keycloakClientId,
                email,
                exception
            )
            throw authenticationFailed("Ошибка при попытке авторизации")
        } catch (exception: FeignException) {
            log.error(
                "Keycloak login error (clientId={}, username={})",
                keycloakClientProperties.keycloakClientId,
                email,
                exception
            )
            throw serverError("Не удалось осуществить авторизацию, попробуйте позже")
        }
    }

    /**
     * Выполняет token exchange в Keycloak, возвращая access token от имени пользователя с указанным идентификатором.
     * <p>
     * Метод использует client credentials текущего сервиса для получения сервисного токена,
     * а затем обменивает его на пользовательский токен через grant type
     * {@code urn:ietf:params:oauth:grant-type:token-exchange}.
     *
     * @param userId идентификатор пользователя в Keycloak, для которого требуется получить токен
     * @return {@link AccessTokenResponse} с токеном, выпущенным для данного пользователя
     */
    override fun exchangeForUser(userId: String?): AccessTokenResponse? {
        val notNullUserId = userId
            ?: throw authenticationFailed("Не передан идентификатор пользователя")

        return try {
            val serviceAccessToken = getServiceAccessToken()
            val exchangeForm = buildTokenExchangeForm(serviceAccessToken, notNullUserId)

            keycloakClient.exchangeToken(exchangeForm)
        } catch (exception: FeignException.FeignClientException) {
            log.warn(
                "Keycloak token exchange failed (status={}, clientId={}, userId={})",
                exception.status(),
                keycloakClientProperties.keycloakClientId,
                notNullUserId,
                exception
            )
            throw authenticationFailed("Ошибка при попытке получить токен пользователя")
        } catch (exception: FeignException) {
            log.error(
                "Keycloak token exchange error (clientId={}, userId={})",
                keycloakClientProperties.keycloakClientId,
                notNullUserId,
                exception
            )
            throw serverError("Не удалось осуществить авторизацию, попробуйте позже")
        }
    }

    private fun getServiceAccessToken(): String {
        val clientCredentialsForm = LinkedMultiValueMap<String, String>().apply {
            add("client_id", keycloakClientProperties.keycloakClientId)
            add("client_secret", keycloakClientProperties.keycloakClientSecret)
            add("grant_type", GRANT_TYPE_CLIENT_CREDENTIALS)
        }

        val response = keycloakClient.getClientCredentialsToken(clientCredentialsForm)
        return response?.token
            ?: throw serverError("Не удалось получить сервисный access token от Keycloak")
    }

    private fun buildTokenExchangeForm(
        serviceAccessToken: String,
        userId: String,
    ): LinkedMultiValueMap<String, String> {
        return LinkedMultiValueMap<String, String>().apply {
            add("client_id", keycloakClientProperties.keycloakClientId)
            add("client_secret", keycloakClientProperties.keycloakClientSecret)
            add("grant_type", GRANT_TYPE_TOKEN_EXCHANGE)
            add("subject_token", serviceAccessToken)
            add("subject_token_type", TOKEN_TYPE_SUBJECT)
            add("requested_subject", userId)
            add("requested_token_type", TOKEN_TYPE_ACCESS_TOKEN)
        }
    }

    override fun logout(refreshToken: String?) {
        return try {
            val form = LinkedMultiValueMap<String, String>().apply {
                add("refresh_token", refreshToken)
                add("client_id", keycloakClientProperties.keycloakClientId)
                add("client_secret", keycloakClientProperties.keycloakClientSecret)
            }

            keycloakClient.logout(form)
        } catch (exception: FeignException.FeignClientException) {
            log.warn(
                "Keycloak logout failed (status={}, clientId={})",
                exception.status(),
                keycloakClientProperties.keycloakClientId,
                exception
            )
            throw authenticationFailed("Ошибка при попытке выхода")
        } catch (exception: FeignException) {
            log.error(
                "Keycloak logout error (clientId={})",
                keycloakClientProperties.keycloakClientId,
                exception
            )
            throw serverError("Не удалось осуществить выход, попробуйте позже")
        }
    }

    override fun refresh(refreshToken: String?): AccessTokenResponse? {

        return try {
            val form = LinkedMultiValueMap<String, String>().apply {
                add("refresh_token", refreshToken)
                add("client_id", keycloakClientProperties.keycloakClientId)
                add("client_secret", keycloakClientProperties.keycloakClientSecret)
                add("grant_type", GRANT_TYPE_REFRESH_TOKEN)
            }

            keycloakClient.refreshToken(form)
        } catch (exception: FeignException.FeignClientException) {
            log.warn(
                "Keycloak refresh failed (status={}, clientId={})",
                exception.status(),
                keycloakClientProperties.keycloakClientId,
                exception
            )
            throw authenticationFailed("Ошибка при попытке обновления токена")
        } catch (exception: FeignException) {
            log.error(
                "Keycloak refresh error (clientId={})",
                keycloakClientProperties.keycloakClientId,
                exception
            )
            throw serverError("Не удалось обновить токен, попробуйте позже")
        }
    }

    override fun getUser(accessToken: String?): KeycloakUserInfoResponse? {
        return try {
            keycloakClient.getUser(accessToken)
        } catch (exception: FeignException.FeignClientException) {
            log.warn(
                "Keycloak getUser failed (status={}, clientId={})",
                exception.status(),
                keycloakClientProperties.keycloakClientId,
                exception
            )
            throw authenticationFailed("Ошибка при попытке получения информации о пользователе")
        } catch (exception: FeignException) {
            log.error(
                "Keycloak getUser error (clientId={})",
                keycloakClientProperties.keycloakClientId,
                exception
            )
            throw serverError("Не удалось получить информацию о пользователе, попробуйте позже")
        }
    }
}
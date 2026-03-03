## auth-kotlin

Сервис аутентификации на базе **Keycloak** (OIDC/OAuth2) и **OTP** (отправка/проверка через SOAP).

## Основные термины (Keycloak)

- **Realm (реалм)**: изолированное пространство пользователей, ролей, клиентов и настроек. В URL токенов всегда фигурирует реалм:  
  `/realms/<realm>/protocol/openid-connect/token`.
- **Client (клиент)**: приложение/сервис, которое запрашивает токены у Keycloak.
  - **confidential client**: имеет `client_secret` (сервер-сервер).
  - **public client**: без секрета (SPA/mobile).
- **Service account (сервисный аккаунт клиента)**: “технический пользователь” для confidential клиента, используется в `client_credentials`.
- **Client scopes / Scopes (скоупы)**: наборы прав/claims, которые попадают в токен (например `profile`, `email`, `roles`).
- **Roles (роли)**:
  - **realm roles**: глобальные в рамках реалма
  - **client roles**: привязаны к конкретному клиенту
- **Mappers (мапперы)**: правила, как поля пользователя/атрибуты/роли попадают в токен (claims).
- **Token exchange (обмен токенов)**: механизм, позволяющий обменять один токен на другой (например получить токен “как пользователь” имея сервисный токен).

## Основные термины (OAuth2 / OIDC)

- **Authorization Server**: сервер, который выпускает токены (Keycloak).
- **Resource Server**: сервис, который принимает access token и защищает API.
- **Access Token**: короткоживущий токен доступа к API.
- **Refresh Token**: токен для получения нового access token без повторной аутентификации.
- **ID Token (OIDC)**: токен идентичности (обычно нужен фронту), в этом проекте основной акцент на access/refresh.
- **Grant Type**: тип потока получения токена.

## Используемые grant types в проекте

- **`client_credentials`**: получить сервисный токен от имени сервиса (по `client_id` + `client_secret`).
- **`refresh_token`**: обновить access token по refresh token.
- **`urn:ietf:params:oauth:grant-type:token-exchange`**: получить токен “как пользователь” после успешной OTP-проверки (impersonation/token-exchange).

Важно: endpoint Keycloak для токенов ожидает **`application/x-www-form-urlencoded`** (поля формы в теле запроса), а не JSON.

## Как устроен OTP flow в этом проекте

1. Клиент вызывает `/api/v1/auth/send-otp` — сервис отправляет OTP через SOAP (см. `OtpClient`).
2. Клиент вводит OTP и вызывает `/api/v1/auth/login/otp`.
3. Сервис проверяет OTP (SOAP) и находит пользователя в Keycloak.
4. Сервис получает **service token** (`client_credentials`), затем делает **token exchange** и возвращает токены клиенту.

## Основные термины и поведение (Spring Security)

Spring Security строится вокруг цепочки фильтров и объектов “кто пользователь и что ему можно”.

- **SecurityFilterChain**: цепочка фильтров, через которую проходит каждый HTTP запрос.
- **Authentication**: “кто ты” (результат аутентификации). Обычно содержит principal (пользователь) и authorities (права/роли).
- **Authorization**: “что тебе можно” (проверка прав/ролей на endpoint/метод).
- **SecurityContext**: контейнер, где Spring хранит текущий `Authentication` на время обработки запроса.
- **Stateless**: режим без HTTP-сессий. Каждый запрос должен нести свою авторизацию (обычно Bearer JWT).
- **CSRF**: защита от подделки запросов (обычно нужна для cookie-сессий). Для stateless API часто отключают.
- **CORS**: политика браузера для кросс-доменных запросов (настраивается отдельно, зависит от фронта).
- **Method Security**: проверки на уровне методов (`@PreAuthorize`, `@PostAuthorize`) — включается `@EnableMethodSecurity`.

### Что это значит в этом проекте

См. `src/main/kotlin/ru/max/bank/authkotlin/config/SecurityConfiguration.kt`:

- **CORS и CSRF отключены**
- **Сессии отключены** (`STATELESS`)
- **Все запросы разрешены** (`permitAll`), т.е. Spring Security сейчас фактически не блокирует доступ к API

Если позже захочешь защищать ресурсы access token’ом от Keycloak, обычно включают режим **Resource Server (JWT)** и задают правила доступа (например, закрыть всё кроме `/api/v1/auth/**`).

## Про token exchange в Keycloak

Чтобы token exchange работал, Keycloak должен:

- **поддерживать token-exchange** (фича должна быть включена на стороне Keycloak)
- **разрешать** token exchange для конкретного клиента/сервисного аккаунта (права/политики)

Если Keycloak отвечает:
- `Missing form parameter: grant_type` — запрос отправлен не как форма.
- `unsupported_grant_type` — token exchange выключен/не поддерживается/запрещён конфигурацией.

## Где смотреть реализацию

- **Keycloak**: `src/main/kotlin/ru/max/bank/authkotlin/service/adapter/KeycloakAdapterImpl.kt`
- **SOAP OTP**: `src/main/kotlin/ru/max/bank/authkotlin/external/OtpClient.kt`
- **OTP сервис**: `src/main/kotlin/ru/max/bank/authkotlin/service/OtpServiceImpl.kt`
- **Глобальная обработка ошибок**: `src/main/kotlin/ru/max/bank/authkotlin/controller/advice/GlobalExceptionHandler.kt`


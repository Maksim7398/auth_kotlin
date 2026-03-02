package ru.max.bank.authkotlin.service

import org.springframework.stereotype.Service
import ru.max.bank.authkotlin.PhoneNumberUtils
import ru.max.bank.authkotlin.config.properties.DevModeProperty
import ru.max.bank.authkotlin.external.OtpClient
import ru.max.bank.authkotlin.model.OtpGenerate
import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.SendOtpRequest
import ru.max.bank.authkotlin.model.response.SendOtpResponse
import ru.max.bank.authkotlin.service.adapter.KeycloakAdminAdapter
import ru.max.bank.otp.enums.FlowType
import ru.max.bank.otp.enums.OtpCheckStatus
import ru.max.bank.otp.model.request.OtpCheckRequest
import ru.max.bank.otp.model.request.OtpSendRequest

@Service
class OtpServiceImpl (
    private val keycloakAdminAdapter: KeycloakAdminAdapter,
    private val devModeProperty: DevModeProperty,
    private val otpClient: OtpClient,

    ) : OtpService {

    override fun sendOtpToUserByPhoneNumber(request: SendOtpRequest): SendOtpResponse {
        val userPhoneNumber: String = request.phoneNumber
        val placeHolder: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        val otpGenerate = OtpGenerate(userPhoneNumber, FlowType.AUTHENTICATION)

        // Static otp code for application store users
        val sendOtp = otpClient.sendOtp(OtpSendRequest(FlowType.AUTHENTICATION, userPhoneNumber))

//        placeHolder.put(SMS_OTP, otpCode)
//        notificationServiceClient.sendOtp(otpMapper.toOtpCreateRequest(otp, otpCode, placeHolder))
//        otp.setCode(otpCode)
//        otpStarterService.persist(otp)

        return SendOtpResponse(phoneNumber = sendOtp.phoneNumber);
    }

    override fun checkOtpSentUserByPhoneNumber(otpCheck: SendOtpCheck): Boolean {
//        temporarilyBlockedAccountService.validateAccountNotBlockedTemporarily(request.phoneNumber())

        val phoneNumber: String = PhoneNumberUtils.cleanPhoneNumber(otpCheck.phoneNumber)

        val otpCheckRequest =
            OtpCheckRequest(phoneNumber, FlowType.AUTHENTICATION, otpCheck.otp)

        val checkOtp = otpClient.checkOtp(otpCheckRequest)

        if (OtpCheckStatus.APPROVED == checkOtp.otpCheckStatus) {
            return true
        } else {
            return false
        }
    }


//        val otpValidate: OtpValidate =
//            OtpValidate(phoneNumber, deviceId, request.code(), OtpServiceImpl.AUTH_FLOW_TYPE)
//
//        otpStarterService.compareOtpCode(otpValidate)//будем проверять на стороне клиента
//
//        val userRegistration: UserRegistrationCheckResponse =
//            userService.checkUserRegistration(phoneNumber, deviceId)
//
//        if (keycloakAdminAdapter.findByUsername(phoneNumber).isPresent()) {
//            val tokenExchangeResponse: TokenExchangeResponse =
//                keycloakService.exchangeToken(phoneNumber)
//
//            return CheckOtpResponse.builder()
//                .phoneNumber(phoneNumber)
//                .isRegistered(userRegistration.getIsRegistered())
//                .accessToken(tokenExchangeResponse.getAccessToken())
//                .refreshToken(tokenExchangeResponse.getRefreshToken())
//                .userId(userRegistration.getId())
//                .build()
//        } else {
//            val userRepresentation: UserRepresentation = UserRepresentation()
//            userRepresentation.setUsername(phoneNumber)
//
//            userRepresentation.setEnabled(true)
//            userRepresentation.setEmailVerified(false)
//
//            keycloakService.createUser(userRepresentation)
//
//            val tokenExchangeResponse: TokenExchangeResponse =
//                keycloakService.exchangeToken(phoneNumber)
//            return CheckOtpResponse.builder()
//                .phoneNumber(phoneNumber)
//                .isRegistered(userRegistration.getIsRegistered())
//                .accessToken(tokenExchangeResponse.getAccessToken())
//                .refreshToken(tokenExchangeResponse.getRefreshToken())
//                .userId(userRegistration.getId())
//                .build()
//        }
//    }
}
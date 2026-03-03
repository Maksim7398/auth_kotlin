package ru.max.bank.authkotlin.service

import org.springframework.stereotype.Service
import ru.max.bank.authkotlin.PhoneNumberUtils
import ru.max.bank.authkotlin.external.OtpClient
import ru.max.bank.authkotlin.model.SendOtpCheck
import ru.max.bank.authkotlin.model.request.SendOtpRequest
import ru.max.bank.authkotlin.model.response.SendOtpResponse
import ru.max.bank.otp.enums.FlowType
import ru.max.bank.otp.enums.OtpCheckStatus
import ru.max.bank.otp.model.request.OtpCheckRequest
import ru.max.bank.otp.model.request.OtpSendRequest

@Service
class OtpServiceImpl(
    private val otpClient: OtpClient,

    ) : OtpService {

    override suspend fun sendOtpToUserByPhoneNumber(request: SendOtpRequest): SendOtpResponse {
        val userPhoneNumber: String = request.phoneNumber
        val sendOtp = otpClient.sendOtpSuspend(OtpSendRequest(FlowType.AUTHENTICATION, userPhoneNumber))

        return SendOtpResponse(phoneNumber = sendOtp.phoneNumber)
    }

    override suspend fun checkOtpSentUserByPhoneNumber(otpCheck: SendOtpCheck): Boolean {
        val phoneNumber: String = PhoneNumberUtils.cleanPhoneNumber(otpCheck.phoneNumber)
        val otpCheckRequest =
            OtpCheckRequest(phoneNumber, FlowType.AUTHENTICATION, otpCheck.otp)
        val checkOtp = otpClient.checkOtpSuspend(otpCheckRequest)

        return OtpCheckStatus.APPROVED == checkOtp.otpCheckStatus
    }
}
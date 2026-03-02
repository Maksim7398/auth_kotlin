package ru.max.bank.authkotlin.external

import org.springframework.stereotype.Service
import org.springframework.ws.client.core.WebServiceTemplate
import ru.max.bank.otp.api.OtpServiceClient
import ru.max.bank.otp.model.request.OtpCheckRequest
import ru.max.bank.otp.model.request.OtpSendRequest
import ru.max.bank.otp.model.response.OtpCheckResponse
import ru.max.bank.otp.model.response.OtpSendResponse

@Service
class OtpClient(
    private val webServiceTemplate: WebServiceTemplate,
) : OtpServiceClient {

    override fun sendOtp(request: OtpSendRequest): OtpSendResponse {
        return webServiceTemplate.marshalSendAndReceive(request) as OtpSendResponse
    }

    override fun checkOtp(request: OtpCheckRequest): OtpCheckResponse {
        return webServiceTemplate.marshalSendAndReceive(request) as OtpCheckResponse
    }
}
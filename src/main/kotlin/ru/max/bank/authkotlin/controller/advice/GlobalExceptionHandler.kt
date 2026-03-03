package ru.max.bank.authkotlin.controller.advice

import feign.FeignException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.max.bank.authkotlin.exception.BaseException
import ru.max.bank.authkotlin.exception.CommonErrorCode
import ru.max.bank.authkotlin.exception.InternalServerException
import ru.max.bank.authkotlin.exception.KeycloakAuthenticationException
import ru.max.bank.authkotlin.model.response.ErrorResponse
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException, request: HttpServletRequest): Pair<Int, ErrorResponse> {
        val status = ex.status
        return status.value() to buildErrorResponse(
            status = status,
            code = ex.errorCode,
            message = ex.errorMessage,
            request = request,
        )
    }

    @ExceptionHandler(KeycloakAuthenticationException::class)
    fun handleKeycloakAuthenticationException(
        ex: KeycloakAuthenticationException,
        request: HttpServletRequest
    ): Pair<Int, ErrorResponse> {
        val status = ex.status
        return status.value() to buildErrorResponse(
            status = status,
            code = ex.errorCode.name,
            message = ex.errorMessage,
            request = request,
        )
    }

    @ExceptionHandler(InternalServerException::class)
    fun handleInternalServerException(ex: InternalServerException, request: HttpServletRequest): Pair<Int, ErrorResponse> {
        val status = ex.status
        return status.value() to buildErrorResponse(
            status = status,
            code = status.name,
            message = ex.message ?: "Internal server error",
            request = request,
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, request: HttpServletRequest): Pair<Int, ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        val details = mapOf(
            "fieldErrors" to ex.bindingResult
                .fieldErrors
                .groupBy(FieldError::getField)
                .mapValues { (_, errs) -> errs.mapNotNull { it.defaultMessage } }
        )

        return status.value() to buildErrorResponse(
            status = status,
            code = CommonErrorCode.VALIDATION_ERROR.name,
            message = "Validation failed",
            request = request,
            details = details
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException, request: HttpServletRequest): Pair<Int, ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        val details = mapOf(
            "violations" to ex.constraintViolations.map {
                mapOf(
                    "property" to it.propertyPath.toString(),
                    "message" to it.message
                )
            }
        )

        return status.value() to buildErrorResponse(
            status = status,
            code = CommonErrorCode.VALIDATION_ERROR.name,
            message = "Validation failed",
            request = request,
            details = details
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, request: HttpServletRequest): Pair<Int, ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        return status.value() to buildErrorResponse(
            status = status,
            code = CommonErrorCode.BAD_REQUEST.name,
            message = ex.mostSpecificCause.message ?: "Malformed JSON request",
            request = request,
        )
    }

    @ExceptionHandler(FeignException::class)
    fun handleFeignException(ex: FeignException, request: HttpServletRequest): Pair<Int, ErrorResponse> {
        val status = HttpStatus.BAD_GATEWAY
        val details = mapOf(
            "upstreamStatus" to ex.status(),
        )

        return status.value() to buildErrorResponse(
            status = status,
            code = CommonErrorCode.UPSTREAM_ERROR.name,
            message = "Ошибка при обращении к внешнему сервису",
            request = request,
            details = details
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleAny(ex: Exception, request: HttpServletRequest): Pair<Int, ErrorResponse> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return status.value() to buildErrorResponse(
            status = status,
            code = CommonErrorCode.INTERNAL_ERROR.name,
            message = ex.message ?: "Unexpected error",
            request = request,
        )
    }

    private fun buildErrorResponse(
        status: HttpStatus,
        code: String?,
        message: String,
        request: HttpServletRequest,
        details: Map<String, Any?>? = null,
    ): ErrorResponse {
        return ErrorResponse(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            code = code,
            message = message,
            path = request.requestURI,
            details = details,
        )
    }
}


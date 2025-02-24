package dev.hyunec.overloadadmin.clientuser.exception

import dev.hyunec.overloadadmin.common.exception.GlobalExceptionHandler.ErrorResponse
import dev.hyunec.overloadadmin.support.exception.toResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class ClientUserExceptionHandler {
    private val log = KotlinLogging.logger {}

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ClientUserException::class)
    fun handleInvalidResidentNumberException(e: ClientUserException): ErrorResponse {
        return e.toResponse().also {
            log.error { "traceId=${it.traceId}, exceptionLocation=${e.stackTrace[0].className}:${e.stackTrace[0].lineNumber}" }
            log.error { e }
        }
    }
}

package dev.hyunec.townhall.common.exception

import com.fasterxml.jackson.annotation.JsonInclude
import dev.hyunec.townhall.support.exception.toResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant
import java.util.*

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = KotlinLogging.logger {}

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ErrorResponse {
        return e.toResponse().also {
            log.error { "traceId=${it.traceId}, exceptionLocation=${e.stackTrace[0].className}:${e.stackTrace[0].lineNumber}" }
            log.error { e }
        }
    }

    /**
     * 표준 예외 응답 객체
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ErrorResponse(
        val code: String,
        val message: String?,
        val data: Any? = null,
    ) {
        val timestamp: Instant = Instant.now()
        val traceId: String = UUID.randomUUID().toString()
    }
}

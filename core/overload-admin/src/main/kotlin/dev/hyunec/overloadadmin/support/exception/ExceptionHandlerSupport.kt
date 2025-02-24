package dev.hyunec.overloadadmin.support.exception

import dev.hyunec.overloadadmin.common.exception.GlobalExceptionHandler.ErrorResponse

fun Exception.toResponse(): ErrorResponse {
    return ErrorResponse(code = this.javaClass.simpleName, message = this.message)
}

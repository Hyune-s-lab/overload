package dev.hyunec.townhall.support.exception

import dev.hyunec.townhall.common.exception.GlobalExceptionHandler.ErrorResponse

fun Exception.toResponse(): ErrorResponse {
    return ErrorResponse(code = this.javaClass.simpleName, message = this.message)
}

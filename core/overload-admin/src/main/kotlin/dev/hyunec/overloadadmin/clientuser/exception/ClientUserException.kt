package dev.hyunec.overloadadmin.clientuser.exception

sealed class ClientUserException(message: String) : RuntimeException(message) {
    class UnexpectedException(message: String) : ClientUserException(message)
}

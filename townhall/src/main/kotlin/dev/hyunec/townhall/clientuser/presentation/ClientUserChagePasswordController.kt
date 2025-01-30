package dev.hyunec.townhall.clientuser.presentation

import dev.hyunec.townhall.clientuser.domain.ClientUserService
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ClientUserChagePasswordController(
    private val clientUserService: ClientUserService
) {

    @PutMapping("/api/v1/client-user/users/password")
    fun register(@RequestBody request: Request) {
        request.run { clientUserService.changePassword(email, newPassword) }
    }

    data class Request(
        val email: String,
        val newPassword: String,
    )
}

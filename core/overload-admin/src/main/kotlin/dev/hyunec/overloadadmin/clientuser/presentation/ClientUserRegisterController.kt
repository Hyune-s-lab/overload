package dev.hyunec.overloadadmin.clientuser.presentation

import dev.hyunec.overloadadmin.clientuser.domain.ClientUserService
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ClientUserRegisterController(
    private val clientUserService: ClientUserService
) {

    @PostMapping("/api/v1/client-user/users")
    fun register(@RequestBody request: Request) {
        clientUserService.register(request.toUserRepresentation())
    }

    data class Request(
        val email: String,
        val password: String,

        val name: String,

        val isEnabled: Boolean,
        val isEmailVerified: Boolean,
    ) {
        fun toUserRepresentation(): UserRepresentation {
            return UserRepresentation().apply {
                username = this@Request.email
                email = this@Request.email
                isEnabled = this@Request.isEnabled
                isEmailVerified = this@Request.isEmailVerified
                attributes = mapOf("name" to listOf(this@Request.name))
                credentials = listOf(
                    CredentialRepresentation().apply {
                        type = CredentialRepresentation.PASSWORD
                        value = this@Request.password
                        isTemporary = false
                    }
                )
            }
        }
    }
}

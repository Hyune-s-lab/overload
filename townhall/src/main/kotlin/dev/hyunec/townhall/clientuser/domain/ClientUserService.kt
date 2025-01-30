package dev.hyunec.townhall.clientuser.domain

import dev.hyunec.townhall.clientuser.exception.ClientUserException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service

@Service
class ClientUserService(
    private val realmResource: RealmResource
) {
    private val log = KotlinLogging.logger {}

    fun register(user: UserRepresentation) {
        realmResource.users().create(user).use {
            when (it.status) {
                201 -> log.debug { "User registered successfully: ${it.location}" }
                else -> throw ClientUserException.UnexpectedException(
                    "Failed to register user: ${it.status} - ${it.readEntity(String::class.java)}"
                )
            }
        }
    }

    /**
     * email 은 unique 하기 때문에 1개만 리턴한다.
     */
    fun findByEmail(email: String): UserRepresentation {
        return realmResource.users().searchByEmail(email, true)[0]
            ?: throw ClientUserException.UnexpectedException("User not found: $email")
    }
}

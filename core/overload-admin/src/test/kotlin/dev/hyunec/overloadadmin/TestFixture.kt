package dev.hyunec.overloadadmin

import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import kotlin.random.Random

object TestFixture {
    fun user() = UserRepresentation().apply {
        email = email()
        isEnabled = true
        isEmailVerified = true
        attributes = mapOf("name" to listOf("Test User"))
        credentials = listOf(
            CredentialRepresentation().apply {
                type = CredentialRepresentation.PASSWORD
                value = password()
                isTemporary = false
            }
        )
    }

    fun email() = "test_${Random.nextInt(1000)}@test.com"
    fun password() = "password_${Random.nextInt(1000)}"
}

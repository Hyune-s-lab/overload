package dev.hyunec.townhall.integrationtest.domain.clientuser.domain.clientuser

import dev.hyunec.townhall.IntegrationTestSupport
import dev.hyunec.townhall.TestFixture
import dev.hyunec.townhall.clientuser.domain.ClientUserService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters

class ClientUserServiceIT(
    val clientUserService: ClientUserService
) : IntegrationTestSupport() {

    val testUser = UserRepresentation().apply {
        email = TestFixture.email()
        isEnabled = true
        isEmailVerified = true
        attributes = mapOf("name" to listOf("Test User"))
        credentials = listOf(
            CredentialRepresentation().apply {
                type = CredentialRepresentation.PASSWORD
                value = TestFixture.password()
                isTemporary = false
            }
        )
    }

    @Test
    fun `usecase1) 회원 가입, 회원 조회, service client 로 회원 로그인`() {
        // 1. 회원 가입
        clientUserService.register(testUser)

        // 2. 회원 조회
        clientUserService.findByEmail(testUser.email).run {
            email shouldBe testUser.email
        }

        // 3. service client 로 회원 로그인
        val loginRequest = BodyInserters
            .fromFormData("username", testUser.email)
            .with("password", testUser.credentials.first().value)
            .with("grant_type", "password")
            .with("client_id", serviceClient.id)
            .with("client_secret", serviceClient.secret)

        val tokenUri = "/realms/${keycloakConfigProperties.realm}/protocol/openid-connect/token"

        webTestClient.post()
            .uri(tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(loginRequest)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .consumeWith { response ->
                val responseBody = response.responseBody?.toString(Charsets.UTF_8) ?: "No Response Body"
                log.info { "Exchange Response:\n$responseBody" }
            }
            .jsonPath("$.access_token").isNotEmpty
    }
}

package dev.hyunec.townhall.integrationtest.domain.clientuser.domain.clientuser

import dev.hyunec.townhall.IntegrationTestSupport
import dev.hyunec.townhall.TestFixture.user
import dev.hyunec.townhall.clientuser.domain.ClientUserService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters

class ClientUserServiceIT(
    private val clientUserService: ClientUserService
) : IntegrationTestSupport() {

    @Test
    fun `usecase1) 회원 가입, 회원 조회, service client 로 회원 로그인`() {
        val testUser = user()

        // 1. 회원 가입
        clientUserService.register(testUser)

        // 2. 회원 조회
        clientUserService.findByEmail(testUser.email).run {
            email shouldBe testUser.email
        }

        // 3. service client 로 회원 로그인
        val loginBody = testUser.run {
            loginBody(username = email, password = credentials.first().value)
        }

        loginWebTestClient
            .body(loginBody)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .consumeWith { response ->
                val responseBody = response.responseBody?.toString(Charsets.UTF_8) ?: "No Response Body"
                log.info { "Exchange Response:\n$responseBody" }
            }
            .jsonPath("$.access_token").isNotEmpty
    }

    private fun loginBody(
        username: String,
        password: String,
        grantType: String = "password",
        clientId: String = serviceClient.id,
        clientSecret: String = serviceClient.secret
    ) = BodyInserters
        .fromFormData("username", username)
        .with("password", password)
        .with("grant_type", grantType)
        .with("client_id", clientId)
        .with("client_secret", clientSecret)
}

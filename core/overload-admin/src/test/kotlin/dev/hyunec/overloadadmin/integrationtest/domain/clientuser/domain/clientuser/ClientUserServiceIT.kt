package dev.hyunec.overloadadmin.integrationtest.domain.clientuser.domain.clientuser

import dev.hyunec.overloadadmin.IntegrationTestSupport
import dev.hyunec.overloadadmin.TestFixture.user
import dev.hyunec.overloadadmin.clientuser.domain.ClientUserService
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

    @Test
    fun `usecase2) 회원 가입, service client 로 회원 로그인, 비밀번호 변경, 로그인(변경 전 비밀번호), 로그인(변경 후 비밀번호`() {
        val testUser = user()

        // 1. 회원 가입
        clientUserService.register(testUser)

        // 2. service client 로 회원 로그인
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

        // 3. 비밀번호 변경
        val newPassword = "newPassword"
        clientUserService.changePassword(testUser.email, newPassword)

        // 4. service client 로 회원 로그인 (변경 전 비밀번호)
        loginWebTestClient
            .body(loginBody)
            .exchange()
            .expectStatus().isUnauthorized

        // 5. service client 로 회원 로그인 (변경 후 비밀번호)
        val loginBodyAfterChangePassword = testUser.run {
            loginBody(username = email, password = newPassword)
        }

        loginWebTestClient
            .body(loginBodyAfterChangePassword)
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

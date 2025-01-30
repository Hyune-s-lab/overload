package dev.hyunec.townhall

import dev.hyunec.townhall.common.config.KeycloakConfigProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles(value = ["integration"])
@TestMethodOrder(MethodOrderer.MethodName::class)
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = AutowireMode.ALL)
abstract class IntegrationTestSupport {

    @Autowired
    protected lateinit var keycloakConfigProperties: KeycloakConfigProperties
    protected lateinit var webTestClient: WebTestClient

    protected val log = KotlinLogging.logger {}

    protected val faker = Faker()
    protected val serviceClient = Client(
        id = "gateway-service",
        secret = "RAMzyGp5X6AZEV2EVn962wKbZVxp7C6W"
    )

    @BeforeAll
    fun setupWebTestClient() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl(keycloakConfigProperties.serverUrl)
            .build()
    }

    data class Client(
        val id: String,
        val secret: String
    )
}

package dev.hyunec.overloadadmin.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "keycloak")
class KeycloakConfigProperties {
    lateinit var serverUrl: String
    lateinit var realm: String
    lateinit var clientId: String
    lateinit var clientSecret: String
}

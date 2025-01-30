package dev.hyunec.townhall.common.config

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakConfig(
    private val properties: KeycloakConfigProperties
) {
    @Bean
    fun keycloak(): Keycloak {
        return properties.run {
            KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()
        }
    }

    @Bean
    fun realmResource(keycloak: Keycloak): RealmResource {
        return keycloak.realm(properties.realm)
    }
}
